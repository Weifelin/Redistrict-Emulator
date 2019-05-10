package com.giant.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
@SpringBootApplication does the following:
		- @Configuration tags the class as a source of bean definitions for the application context.
		- @EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans,
		  and various property settings.
	    - Spring Boot adds @EnableWebMvc automatically when it sees spring-webmvc on the classpath.
	      This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet.
		- @ComponentScan tells Spring to look for other components, configurations, and services in the hello package,
		  allowing it to find the controllers.
* */
@SpringBootApplication
public class DemoApplication extends SpringBootServletInitializer {

	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	WebMvcConfigurer webMvcConfigurer(){
		return new WebMvcConfigurer() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/resources/public/**")
						.addResourceLocations("classpath:/public/");
			}
		};
	}


}
