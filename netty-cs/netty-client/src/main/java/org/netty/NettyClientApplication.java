package org.netty;

import org.netty.client.EchoClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author luoliang
 */
@SpringBootApplication
public class NettyClientApplication {
    private static final int PORT = 22222;
    private static final String HOST = "localhost";

    public static void main(String[] args) throws Exception {
        SpringApplication.run(NettyClientApplication.class, args);
        new EchoClient(HOST, PORT).start();
    }

}
