package com.zwl.client;

import com.zwl.fallback.RemoteServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/21
 */
@FeignClient(
    contextId = "remoteService",
    value = "nacos-provider",
    fallbackFactory = RemoteServiceFallbackFactory.class)
public interface RemoteService {

  @GetMapping("/each/{str}")
  String each(@PathVariable("str") String str);
}
