package cn.ykf.rpc;

import cn.ykf.rpc.core.MyRpcFramework;
import cn.ykf.rpc.core.ServiceDiscovery;
import cn.ykf.rpc.core.ServiceListener;
import cn.ykf.rpc.core.ServiceRegistry;
import cn.ykf.rpc.service.MyService;
import cn.ykf.rpc.service.impl.MyServiceImpl;
import java.util.concurrent.TimeUnit;
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

    @Test
    public void testRegistry() throws Exception {
        // 注册中心
        ServiceRegistry registry = new ServiceRegistry();
        // 监听器
        registry.addListener(new ServiceListener() {
            @Override
            public void onServiceUp(String serviceName, String serviceAddress) {
                System.out.printf("%s is up at %s%n", serviceName, serviceAddress);
            }

            @Override
            public void onServiceDown(String serviceName, String serviceAddress) {
                System.out.printf("%s is down at %s%n", serviceName, serviceAddress);
            }
        });
        // 服务发现
        ServiceDiscovery discovery = new ServiceDiscovery(registry);

        String serviceName = MyService.class.getName();
        int port = 9090;
        // 服务提供者启动
        new Thread(() -> {
            try {
                MyRpcFramework.export(new MyServiceImpl(), port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        // 服务注册
        registry.register(serviceName, "127.0.0.1:" + port);

        TimeUnit.SECONDS.sleep(1);

        // 服务发现
        String address = discovery.discovery(serviceName);
        // 服务消费者启动
        String[] addressInfo = address.split(":");
        MyService service = MyRpcFramework.refer(MyService.class, addressInfo[0], Integer.parseInt(addressInfo[1]));
        System.out.println(service.hello("code4j"));
    }
}
