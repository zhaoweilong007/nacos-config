package com.zwl.controller;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/20
 */
@RestController
@RefreshScope
@Slf4j
public class ProviderController {

  @Value("${server.desc}")
  String desc;

  @Value("${nacos.config}")
  String config;

  /** 注入nacos配置服务 */
  @Autowired NacosConfigProperties configProperties;

  @Autowired NacosServiceManager nacosServiceManager;

  /** 注入名称服务 */
  @Autowired NacosDiscoveryProperties discoveryProperties;

  @RequestMapping("/desc")
  public String getDesc() {
    return desc;
  }

  @GetMapping("/config")
  public String getConfig() {
    return config;
  }

  @GetMapping("each/{str}")
  public String each(@PathVariable String str) {
    log.info("接收到参数：{}", str);
    return "hello world discovery " + str;
  }

  @GetMapping("/prop")
  public NacosDiscoveryProperties prop() {
    return discoveryProperties;
  }

  @GetMapping("naming")
  public List<Instance> naming() throws NacosException {
    NamingService namingService =
        nacosServiceManager.getNamingService(discoveryProperties.getNacosProperties());
    return namingService.getAllInstances("nacos-provider");
  }

  @GetMapping("publish")
  public Boolean publish(
      @RequestParam String dataId, @RequestParam String group, @RequestParam String value)
      throws NacosException {
    ConfigService configService = configProperties.configServiceInstance();
    log.info("推送配置信息，dataid:{},group:{},value:{}", dataId, group, value);
    return configService.publishConfig(dataId, group, value);
  }


}
