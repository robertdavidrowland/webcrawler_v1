package com.example.webcrawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.webcrawler.services.QueueProcessingService;

@SpringBootApplication
public class WebcrawlerApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(WebcrawlerApplication.class);

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
}
