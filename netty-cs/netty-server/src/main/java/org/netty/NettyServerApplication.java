package org.netty;

import org.netty.server.EchoServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author luoliang
 */
@SpringBootApplication
public class NettyServerApplication {
    private static final int PORT = 22222;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(NettyServerApplication.class, args);
        new EchoServer(PORT).start();
    }

}
