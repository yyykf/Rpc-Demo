package cn.ykf.rpc;

import cn.ykf.rpc.core.MyRpcFramework;
import cn.ykf.rpc.service.MyService;
import org.junit.Test;

/**
 * 服务消费方测试类
 *
 * @author YuKaiFan <1092882580@qq.com>
 * @date 2020-09-08
 */
public class MyRpcFrameworkTest {

    @Test
    public void testRefer() {
        // 引用服务并调用方法
        MyService service = MyRpcFramework.refer(MyService.class, "127.0.0.1", 23232);
        System.out.println(service.hello("鱼开饭"));
    }
}
