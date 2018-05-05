package org.rpc.provider;

import org.rpc.provider.rpc.Exporter;
import org.rpc.provider.service.HelloService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author luoliang
 */
@SpringBootApplication
public class ProviderApplication implements CommandLineRunner {
    @Resource
    private HelloService helloService;
    @Resource
    private Exporter exporter;

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        exporter.export(helloService, 12345);
    }
}
