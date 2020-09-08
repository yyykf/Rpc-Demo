package cn.ykf.rpc.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * RPC框架最简单实现 v1.0
 *
 * @author YuKaiFan <1092882580@qq.com>
 * @date 2020-09-08
 */
public class MyRpcFramework {

    /**
     * 服务提供方用于暴露接口
     *
     * @param service 待暴露的接口
     * @param port    暴露的端口，供服务消费端使用
     */
    public static void export(final Object service, int port) throws Exception {
        ServerSocket server = new ServerSocket(port);
        // 轮询
        while (true) {
            // 等待连接
            Socket socket = server.accept();
            // 开启线程完成服务调用
            new Thread(() -> {
                try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                     ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());) {
                    // 按规定好的顺序进行反序列化，先方法名
                    String methodName = input.readUTF();
                    // 参数类型
                    Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                    // 参数
                    Object[] arguments = (Object[]) input.readObject();
                    System.out.println("服务端的 " + methodName + " 将被调用...");
                    // 反射获取待调用方法
                    Method method = service.getClass().getMethod(methodName, parameterTypes);
                    // 调用方法
                    Object result = method.invoke(service, arguments);
                    // 返回结果
                    output.writeObject(result);
                } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    System.out.println("调用服务出错...");
                    e.printStackTrace();
                }

            }).start();
        }
    }


    /**
     * 引用服务
     *
     * @param serviceClass 待引用服务类
     * @param host         服务提供方地址
     * @param port         服务被暴露端口
     * @param <T>          引用服务类型
     * @return 引用服务
     */
    public static <T> T refer(Class<T> serviceClass, final String host, final int port) {
        // 动态代理，去服务提供方调用具体实现的方法
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 连接服务提供方
                Socket socket = new Socket(host, port);
                // 序列化
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                // 先方法名
                output.writeUTF(method.getName());
                // 参数类型
                output.writeObject(method.getParameterTypes());
                // 参数
                output.writeObject(args);
                // 读取结果
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                return input.readObject();
            }
        });
    }
}
