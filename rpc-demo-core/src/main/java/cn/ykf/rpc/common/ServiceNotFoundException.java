package cn.ykf.rpc.common;

/**
 * 未发现服务异常
 *
 * @author YuKaiFan
 * @date 2022/12/5
 * @blog <a href="https://code4j.co">https://code4j.co</a>
 */
public class ServiceNotFoundException extends RuntimeException {

    public ServiceNotFoundException() {
        super();
    }

    public ServiceNotFoundException(String serviceName) {
        super(String.format("Could not find instance of [%s]", serviceName));
    }
}
