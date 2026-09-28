package com.bibliotech.backend;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
@RestController
public class BibliotechBackendApplication {

	@PostConstruct

	// para deixar todas as mensagens em inglês //
	public void init() {
		Locale.setDefault(Locale.US);
		TimeZone.setDefault(TimeZone.getTimeZone("America/Fortaleza"));
	}

	public static void main(String[] args) {
		SpringApplication.run(BibliotechBackendApplication.class, args);
	}

	@GetMapping
	public String index() {
		return "Hello World";
	}

	@Bean
	public LocaleResolver localeResolver() {
		FixedLocaleResolver resolver = new FixedLocaleResolver();
		resolver.setDefaultLocale(Locale.US);
		return resolver;
	}
}
