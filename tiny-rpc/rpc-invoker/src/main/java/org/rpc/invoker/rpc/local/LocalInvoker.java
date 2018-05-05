package org.rpc.invoker.rpc.local;

import lombok.extern.slf4j.Slf4j;
import org.rpc.invoker.rpc.Invoker;
import org.springframework.stereotype.Service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 *
 * @author luoliang
 * @date 2018/5/3
 **/
@Slf4j
@Service
public class LocalInvoker implements Invoker {
    private static final int MAXPORT = 65535;
    private static final int MINPORT = 0;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T invoke(Class<T> interfaceClass, String host, int port) {
        if (Objects.isNull(interfaceClass)) {
            throw new IllegalArgumentException("Interface class is null");
        }
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("The " + interfaceClass.getName() + " must be interface class!");
        }
        if (Objects.isNull(host) || host.length() == 0) {
            throw new IllegalArgumentException("Host is null!");
        }
        if (port <= MINPORT || port > MAXPORT) {
            throw new IllegalArgumentException("Invalid port " + port);
        }
        log.info("Get remote service {} from server {}:{}", interfaceClass.getName(), host, port);

        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, (proxy, method, args) -> {
            try (Socket socket = new Socket(host, port)) {
                try (ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {
                    output.writeUTF(method.getName());
                    output.writeObject(method.getParameterTypes());
                    output.writeObject(args);
                    try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {
                        Object result = input.readObject();
                        if (result instanceof Throwable) {
                            throw (Throwable) result;
                        }
                        return result;
                    }
                }
            }
        });
    }
}
