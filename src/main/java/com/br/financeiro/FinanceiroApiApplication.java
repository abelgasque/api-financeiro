package com.br.financeiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

import com.br.financeiro.config.property.FinanceiroApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(FinanceiroApiProperty.class)
public class FinanceiroApiApplication {
	
	private static ApplicationContext APPLICATION_CONTEXT;
	
	public static void main(String[] args) {
		APPLICATION_CONTEXT = SpringApplication.run(FinanceiroApiApplication.class, args);
	}

	public static <T> T getBean(Class<T> type) {
		return APPLICATION_CONTEXT.getBean(type);
	}
}
