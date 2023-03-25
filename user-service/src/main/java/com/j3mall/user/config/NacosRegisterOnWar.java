package com.j3mall.user.config;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.alibaba.cloud.nacos.registry.NacosRegistration;
import java.lang.management.ManagementFactory;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 项目打包war情况下部署外部 Server，注册到 nacos
 */
@Configuration
@Slf4j
public class NacosRegisterOnWar {

    @Autowired
    private Environment env;
    @Autowired
    private NacosRegistration registration;
    @Autowired
    private NacosAutoServiceRegistration nacosAutoServiceRegistration;

    @PostConstruct
    public void nacosServerRegister() {
        if (registration != null) {
            registration.setPort(getTomcatPort());
            nacosAutoServiceRegistration.start();
        }
    }

    public int getTomcatPort() {
        try {
            return getServerPort();
        } catch (Exception e) {
            log.warn("获取端口失败, 恢复到默认端口, ", e);
        }
        return getDefaultPort();
    }

    private int getServerPort() throws MalformedObjectNameException, NullPointerException {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
            Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        String port = objectNames.iterator().next().getKeyProperty("port");
        return Integer.parseInt(port);
    }

    private int getDefaultPort() {
        return env.getProperty("server.port", Integer.class, 8080);
    }
}

