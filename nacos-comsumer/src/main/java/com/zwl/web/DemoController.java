package com.zwl.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/20
 */
@RestController
@RefreshScope
public class DemoController {

  @Value("${server.desc}")
  String desc;

  @Value("${nacos.config}")
  String config;

  @RequestMapping("/desc")
  public String getDesc() {
    return desc;
  }

  @GetMapping("/config")
  public String getConfig() {
    return config;
  }

  @Autowired RestTemplate restTemplate;


  /**
   * 使用服务名调用provider
   * @param str 参数
   * @return
   */
  @GetMapping("each/{str}")
  public String each(@PathVariable String str) {
    return restTemplate.getForObject("http://nacos-provider/each/" + str, String.class);
  }


}
