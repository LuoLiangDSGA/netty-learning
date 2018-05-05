package org.rpc.invoker.rpc;

/**
 * Created by IntelliJ IDEA.
 *
 * @author luoliang
 * @date 2018/5/3
 **/
public interface Invoker {
    <T> T invoke(Class<T> interfaceClass, String host, int port);
}
