package com.zwl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/20
 */
@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
@EnableFeignClients
public class NacosConsumerApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context =
        SpringApplication.run(NacosConsumerApplication.class, args);
    ConfigurableEnvironment environment = context.getEnvironment();
    MutablePropertySources propertySources = environment.getPropertySources();
    log.info(
        "application name:{},prop:\n{}",
        environment.getProperty("spring.application.name"),
        propertySources);
  }
}
