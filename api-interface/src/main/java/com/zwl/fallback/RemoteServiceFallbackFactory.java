package com.zwl.fallback;

import com.zwl.client.RemoteService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/21
 */
@Component
public class RemoteServiceFallbackFactory implements FallbackFactory<RemoteService> {

  @Override
  public RemoteService create(Throwable throwable) {
    RemoteFallBackImpl remoteFallBack = new RemoteFallBackImpl();
    remoteFallBack.setThrowable(throwable);
    return remoteFallBack;
  }
}
