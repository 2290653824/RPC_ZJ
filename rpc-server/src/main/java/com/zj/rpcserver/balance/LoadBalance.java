package com.zj.rpcserver.balance;

import java.util.List;

/**
 * @author zhengjian
 * @date 2023-06-09 21:44
 */
public interface LoadBalance {

    String loadBalance(List<String> list);
}
