package com.magic.config;

import com.magic.factory.FtpClientPooledObjectFactory;
import com.magic.properties.FtpProperties;
import com.magic.service.FtpProcessor;
import com.magic.service.impl.DefaultFtpProcessor;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

/**
 * @author magic
 */
@Configuration
@ConditionalOnClass({GenericObjectPool.class, FTPClient.class})
@ConditionalOnProperty(prefix = "ftp", name = "isopen", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(FtpProperties.class)
public class FtpConfig {

    private Logger log = LoggerFactory.getLogger(FtpConfig.class);

    private final FtpProperties ftpProperties;

    @Autowired
    public FtpConfig(FtpProperties ftpProperties) {
        this.ftpProperties = ftpProperties;
    }

    private ObjectPool<FTPClient> pool;

    /**
     * 预加载FTPClient连接到对象池中
     *
     * @param initialSize
     * @param maxIdle
     */
    private void preLoadingFtpClient(Integer initialSize, int maxIdle) {
        //如果初始化大小为null或者小于等于0，则不执行逻辑
        if (null == initialSize || initialSize <= 0) {
            return;
        }
        int size = Math.min(initialSize, maxIdle);
        try {
            for (int i = 0; i < size; i++) {
                pool.addObject();
            }
        } catch (Exception e) {
            log.error("预加载失败！", (Object) e.getStackTrace());
        }
    }

    /**
     * 销毁方法
     */
    @PreDestroy
    public void destroy() {
        if (null != pool) {
            pool.close();
            log.info("销毁ftp客户端连接池。。。");
        }
    }

    /**
     * 判断不存在业务Service时初始化默认Bean到Spring
     */
    @Bean
    @ConditionalOnMissingBean(FtpProcessor.class)
    public FtpProcessor ftpProcessor() {
        log.info("没有找到ftp处理器，执行创建默认处理器");
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(6000);
        poolConfig.setSoftMinEvictableIdleTimeMillis(50000);
        poolConfig.setTimeBetweenEvictionRunsMillis(30000);
        pool = new GenericObjectPool<>(new FtpClientPooledObjectFactory(ftpProperties), poolConfig);
        preLoadingFtpClient(ftpProperties.getInitialSize(), poolConfig.getMaxIdle());
        DefaultFtpProcessor processor = new DefaultFtpProcessor(ftpProperties);
        processor.setFtpClientPool(pool);
        processor.setHasInit(true);
        return processor;
    }
}
