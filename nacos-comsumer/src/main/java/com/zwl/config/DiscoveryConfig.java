package com.zwl.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/20
 */
@Configuration(proxyBeanMethods = false)
public class DiscoveryConfig {

  /**
   * 负载均衡的restTemplate，默认策略轮询
   *
   * @return
   */
  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
