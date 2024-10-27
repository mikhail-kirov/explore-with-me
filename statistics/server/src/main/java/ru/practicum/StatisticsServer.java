package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication
@ComponentScan(
        basePackages = {"ru.practicum"},
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class StatisticsServer {
    public static void main(String[] args) {
        SpringApplication.run(StatisticsServer.class, args);
    }
}
