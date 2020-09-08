package cn.ykf.rpc;

import cn.ykf.rpc.core.MyRpcFramework;
import cn.ykf.rpc.service.MyService;
import cn.ykf.rpc.service.impl.MyServiceImpl;
import org.junit.Test;

/**
 * 服务提供方测试类
 *
 * @author YuKaiFan <1092882580@qq.com>
 * @date 2020-09-08
 */
public class MyRpcFrameworkTest {

    @Test
    public void testExport() throws Exception {
        // 服务提供方暴露接口，等待调用
        MyService service = new MyServiceImpl();
        MyRpcFramework.export(service, 23232);
    }
}
