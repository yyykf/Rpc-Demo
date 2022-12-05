package cn.ykf.rpc.core;

import cn.ykf.rpc.utils.ConcurrentHashSet;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    private final ScheduledExecutorService detectExecutor;

    public ServiceRegistry() {
        detectExecutor = Executors.newSingleThreadScheduledExecutor();

        // 每5s检测一下服务是否在线 TODO 2022/12/6 0:42 yukaifan 可以改为心跳机制
        detectExecutor.scheduleWithFixedDelay(this::detect, 1, 3, TimeUnit.SECONDS);
    }

    /**
     * 检测服务是否在线
     */
    private void detect() {
        Iterator<Entry<String, Set<String>>> iterator = servicesMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<String, Set<String>> services = iterator.next();
            Set<String> serviceAddresses = services.getValue();

            Iterator<String> addressIterator = serviceAddresses.iterator();
            String serviceName = services.getKey();
            while (addressIterator.hasNext()) {
                String address = addressIterator.next();
                if (this.telnet(address)) {
                    // 服务在线，继续
                    continue;
                }

                // 服务不可用，移除
                addressIterator.remove();
                System.out.printf("The service [%s] is offline, removed from service instance list.%n", serviceName);

                // 通知监听器
                listeners.forEach(listener -> listener.onServiceDown(serviceName, address));
            }

            // 没有服务在线，移除
            if (serviceAddresses.isEmpty()) {
                iterator.remove();
                System.out.printf("The service [%s] does not exist any online instance, removed it.%n", serviceName);
            }
        }
    }

    /**
     * 测试网络是否连通
     *
     * @param address 服务地址，ip:port
     * @return 是否连通
     */
    private boolean telnet(String address) {
        // 简单使用 socket 判断
        String[] parts = address.split(":");
        // FIXME 2022/12/6 1:20 yukaifan 服务端读取逻辑和这里的逻辑不一致
        try (Socket socket = new Socket(parts[0], Integer.parseInt(parts[1]))) {
            return socket.isConnected();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

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

        System.out.printf("The service [%s](%s) has successfully been unregistered.%n", serviceName,
                serviceAddress);

        // 通知监听器
        for (ServiceListener listener : listeners) {
            listener.onServiceDown(serviceName, serviceAddress);
        }
    }


}