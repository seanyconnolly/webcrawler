package io.shaneconnolly.webcrawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebCrawlerApplication implements CommandLineRunner {


	@Autowired
	private Crawler crawler;

	public static void main(String[] args) {
		SpringApplication.run(WebCrawlerApplication.class, args);
	}


	@Override
	public void run(String... args) {
		crawler.crawl(args[0]);
		if(args[0] == null){
			throw new ExitException();
		}
	}
}
