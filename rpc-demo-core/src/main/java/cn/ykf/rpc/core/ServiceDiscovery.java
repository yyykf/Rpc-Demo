package cn.ykf.rpc.core;

import cn.ykf.rpc.common.ServiceNotFoundException;
import java.util.Date;
import java.util.Random;
import java.util.Set;

/**
 * 服务发现
 *
 * @author YuKaiFan
 * @date 2022/12/5
 * @blog <a href="https://code4j.co">https://code4j.co</a>
 */
public class ServiceDiscovery {

    private static final ThreadLocal<Random> RANDOM_THREAD_LOCAL = ThreadLocal.withInitial(
            () -> new Random(System.currentTimeMillis()));

    private final ServiceRegistry registry;

    public ServiceDiscovery(ServiceRegistry registry) {
        this.registry = registry;
    }

    /**
     * 查找服务地址
     *
     * @param serviceName 服务名称
     * @return 服务地址
     */
    public String discovery(String serviceName) {
        Set<String> addresses = registry.getServiceAddresses(serviceName);
        if (addresses == null || addresses.isEmpty()) {
            throw new ServiceNotFoundException(serviceName);
        }

        try {
            // 随机负载
            int index = RANDOM_THREAD_LOCAL.get().nextInt(addresses.size());
            return addresses.toArray(new String[0])[index];
        }finally {
            RANDOM_THREAD_LOCAL.remove();
        }
    }
}
