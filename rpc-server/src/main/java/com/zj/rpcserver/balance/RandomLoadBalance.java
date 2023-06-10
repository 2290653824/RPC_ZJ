package com.zj.rpcserver.balance;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * @author zhengjian
 * @date 2023-06-09 21:45
 */
@Component("randomLoadBalance")
public class RandomLoadBalance implements LoadBalance{
    @Override
    public String loadBalance(List<String> list) {
        int len=list.size();
        Random random = new Random();
        return list.get(random.nextInt(len));
    }
}
