package com.example.webcrawler;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.example.webcrawler.services.QueueProcessingService;

@SpringBootApplication
public class WebcrawlerApplication implements CommandLineRunner {

    @Autowired
    QueueProcessingService queueProcessor;
    
	@Override
	public void run(String... args) {
		
		if (args.length < 1) {
			System.out.println("useage: java -jar webcrawlerapplication-0.0.1-SNAPSHOT.jar <URL>");
			return;			
		}
		
		queueProcessor.processQueue(args[0]);
	}
	
	public static void main(String[] args) {
		
        SpringApplication app = new SpringApplication(WebcrawlerApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
	}
	
    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("webcrawler-");
        executor.initialize();
        return executor;
    }
}
