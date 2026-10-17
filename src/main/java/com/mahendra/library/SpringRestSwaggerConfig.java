package com.mahendra.library;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebMvc
public class SpringRestSwaggerConfig extends WebMvcConfigurationSupport{
	@Bean
    public Docket api() { 
        Docket docket =  new Docket(DocumentationType.SWAGGER_2);
		docket.apiInfo(apiInfo())  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.ant("/api/**"))
          .build();             
		return docket;                              
    }
	
	private ApiInfo apiInfo(){
		return new ApiInfoBuilder().title("Mahendra's Book Library (Spring REST Demo)")
			.description("A Demo for Spring REST with Spring DataJPA and Swagger2, This project uses In-Memory H2 Database.").build();
	}

	
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
		"classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/",
		"classpath:/resources/static/"};

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		  registry.addResourceHandler("/**")
		  .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
  
	}

}
