package org.rpc.invoker;

import org.rpc.invoker.rpc.Invoker;
import org.rpc.provider.service.HelloService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author luoliang
 */
@SpringBootApplication
public class InvokerApplication implements CommandLineRunner {
    @Resource
    private Invoker invoker;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(InvokerApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        HelloService helloService = invoker.invoke(HelloService.class, "127.0.0.1", 12345);
        for (int i = 0; i < 10; i++) {
            String hello = helloService.hello("luoliang" + i);
            System.out.println(hello);
            Thread.sleep(1000);
        }
    }
}
