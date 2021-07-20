package com.zwl.config;

import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/20
 **/
@Configuration
@Slf4j
public class NacosConfig {

  @NacosConfigListener(dataId = "test",groupId = "test",timeout = 2000)
  public void onMessage(String config) {
    log.info("监听到 nacos-provider-dev 配置更新：{}", config);
  }

}
