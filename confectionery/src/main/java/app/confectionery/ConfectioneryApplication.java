package app.confectionery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@SpringBootApplication
public class ConfectioneryApplication implements RepositoryRestConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(ConfectioneryApplication.class, args);
    }

}
