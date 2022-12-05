package cn.ykf.rpc.core;

import cn.ykf.rpc.utils.ConcurrentHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 简易的注册中心
 *
 * @author YuKaiFan
 * @date 2022/12/5
 * @blog <a href="https://code4j.co">https://code4j.co</a>
 */
public class ServiceRegistry {

    /** 服务实例集合，key 为服务名，value 为服务地址（IP:PORT） */
    private final Map<String, Set<String>> servicesMap = new ConcurrentHashMap<>();
    private final List<ServiceListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * 添加监听器
     *
     * @param listener 服务监听器
     */
    public void addListener(ServiceListener listener) {
        listeners.add(listener);
    }

    /**
     * 获取服务地址集合
     *
     * @param serviceName 服务名称
     * @return 服务地址集合，可能为空
     */
    public Set<String> getServiceAddresses(String serviceName) {
        return servicesMap.get(serviceName);
    }

    /**
     * 服务注册
     *
     * @param serviceName    服务名
     * @param serviceAddress 服务地址
     */
    public void register(String serviceName, String serviceAddress) {
        Set<String> addresses = servicesMap.computeIfAbsent(serviceName, k -> new ConcurrentHashSet<>());

        if (!addresses.add(serviceAddress)) {
            System.out.printf("The service [%s](%s) has already been registered.%n", serviceName, serviceAddress);
            return;
        }

        System.out.printf("The service [%s](%s) has successfully been registered.%n", serviceName, serviceAddress);

        // 通知监听器
        for (ServiceListener listener : listeners) {
            listener.onServiceUp(serviceName, serviceAddress);
        }
    }

    /**
     * 取消注册
     *
     * @param serviceName    服务名
     * @param serviceAddress 服务地址
     */
    public void unregister(String serviceName, String serviceAddress) {
        Set<String> addresses = servicesMap.get(serviceName);
        if (addresses == null) {
            System.out.printf("The service [%s] has not been registered yet.%n", serviceName);
            return;
        }

        if (!addresses.remove(serviceAddress)) {
            System.out.printf(
                    "The service [%s](%s) does not exist in service instances set, maybe it has already been removed.%n",
                    serviceName, serviceAddress);
            return;
        }

        System.out.printf("The service [%s](%s) has successfully been unregistered.%n", serviceName, serviceAddress);

        // 通知监听器
        for (ServiceListener listener : listeners) {
            listener.onServiceDown(serviceName, serviceAddress);
        }
    }


}