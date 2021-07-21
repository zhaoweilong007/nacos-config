package com.zwl.fallback;

import com.alibaba.nacos.common.utils.ExceptionUtil;
import com.zwl.client.RemoteService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 失败回调
 *
 * @author ZhaoWeiLong
 * @since 2021/7/21
 */
@Component
@Slf4j
public class RemoteFallBackImpl implements RemoteService {

  @Getter @Setter private Throwable throwable;

  @Override
  public String each(String str) {
    log.error("触发 fallback ：参数{}，exception:\n{}", str, ExceptionUtil.getAllExceptionMsg(throwable));
    return "远程服务错误，请稍后重试";
  }
}
