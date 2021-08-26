package io.github.jzdayz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.ServerSocket;

@SpringBootApplication
@MapperScan("io.github.jzdayz.mapper")
public class CodeGeneratorApplication {

    static {
        for (int i = 10000; i < 60000; i++) {
            try {
                ServerSocket serverSocket = new ServerSocket(i);
                serverSocket.close();
                System.setProperty("server.port", String.valueOf(i));
                break;
            } catch (Exception e) {
                // ignore
            }
        }
    }

    public static void main(String[] args) {
        System.getProperties().list(System.out);
        SpringApplication.run(CodeGeneratorApplication.class, args);
    }

}
