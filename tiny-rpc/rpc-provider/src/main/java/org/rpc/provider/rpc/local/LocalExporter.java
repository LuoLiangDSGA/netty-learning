package org.rpc.provider.rpc.local;

import lombok.extern.slf4j.Slf4j;
import org.rpc.provider.rpc.Exporter;
import org.springframework.stereotype.Service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 *
 * @author luoliang
 * @date 2018/5/3
 * <p>
 * 本地暴露实现
 **/
@Slf4j
@Service
public class LocalExporter implements Exporter {
    private static final int MAXPORT = 65535;
    private static final int MINPORT = 0;

    @Override
    public void export(Object service, int port) throws Exception {
        if (Objects.isNull(service)) {
            throw new IllegalArgumentException("Service instance is null");
        }
        if (port <= MINPORT || port > MAXPORT) {
            throw new IllegalArgumentException("Invalid port " + port);
        }

        log.info("Export Service：{} on port {}", service.getClass().getName(), port);
        ServerSocket serverSocket = new ServerSocket(port);
        for (; ; ) {
            try {
                final Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
                        try {
                            String methodName = inputStream.readUTF();
                            Class<?>[] parameterTypes = (Class<?>[]) inputStream.readObject();
                            Object[] arguments = (Object[]) inputStream.readObject();
                            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                            try {
                                Method method = service.getClass().getMethod(methodName, parameterTypes);
                                Object result = method.invoke(service, arguments);
                                output.writeObject(result);
                            } catch (Throwable t) {
                                output.writeObject(t);
                            } finally {
                                output.close();
                            }
                        } finally {
                            socket.close();
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }).start();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
