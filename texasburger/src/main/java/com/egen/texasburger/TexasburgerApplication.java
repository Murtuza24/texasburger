package com.egen.texasburger;

import com.egen.texasburger.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.egen.texasburger.repositories")
public class TexasburgerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TexasburgerApplication.class, args);
    }

}
