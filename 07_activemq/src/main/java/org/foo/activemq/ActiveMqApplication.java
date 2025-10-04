package org.foo.activemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class ActiveMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActiveMqApplication.class, args);
        System.out.println("ActiveMQ Example Application Started!");
    }
}
