package org.usedcar.rest.webservice;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UsedCarRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsedCarRestApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("Used Car REST API")
                .version("1.0 SNAPSHOT")
                .description("The Used Car REST API is developed to share Car Ads among pre-authenticated Users.\n" +
                        "The API handles User authentication and Car Ads, the App has the following functions:\n" +
                        "- User can register with name, email and password\n" +
                        "- User can login and logout\n" +
                        "- User can save new Car Ad\n" +
                        "- User can search among car Ads\n" +
                        "- User can delete a Car Ad that is belongs to him/her\n" +
                        "- User can find a Car Ad by its Id\n\n" +
                        "The API is secured with JWT and developed with Spring boot using Java 11 version.\n" +
                        "The API uses in-memory database to persists data\n" +
                        "The API endpoints are described below!")
                .termsOfService("https://usedcar.org/terms/")
                .license(new License().name("Apache 2.0")));
    }
}
