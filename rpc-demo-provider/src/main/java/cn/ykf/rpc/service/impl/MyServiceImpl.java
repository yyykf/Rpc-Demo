package cn.ykf.rpc.service.impl;

import cn.ykf.rpc.service.MyService;

/**
 * 暴露接口实现类
 *
 * @author YuKaiFan <1092882580@qq.com>
 * @date 2020-09-08
 */
public class MyServiceImpl implements MyService {
    @Override
    public String hello(String name) {
        return "Hello World! " + name;
    }
}
