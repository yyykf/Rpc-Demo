package cn.ykf.rpc.core;

/**
 * 服务监听器
 *
 * @author YuKaiFan
 * @date 2022/12/5
 * @blog <a href="https://code4j.co">https://code4j.co</a>
 */
public interface ServiceListener {

    /**
     * 服务上线通知
     *
     * @param serviceName    服务名
     * @param serviceAddress 服务地址
     */
    void onServiceUp(String serviceName, String serviceAddress);

    /**
     * 服务下线通知
     *
     * @param serviceName    服务名
     * @param serviceAddress 服务地址
     */
    void onServiceDown(String serviceName, String serviceAddress);
}
