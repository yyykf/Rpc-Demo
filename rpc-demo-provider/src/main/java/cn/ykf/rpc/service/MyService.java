package cn.ykf.rpc.service;

/**
 * 服务提供端暴露的接口
 *
 * @author YuKaiFan <1092882580@qq.com>
 * @date 2020-09-08
 */
public interface MyService {

    /**
     * output "hello world + name"
     *
     * @param name name
     * @return hello world + name
     */
    String hello(String name);
}
