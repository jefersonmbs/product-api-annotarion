package com.wiredbraincoffee.productapiannotarion;

import com.wiredbraincoffee.productapiannotarion.model.Product;
import com.wiredbraincoffee.productapiannotarion.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ProductApiAnnotarionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApiAnnotarionApplication.class, args);
    }

    @Bean
    CommandLineRunner init(ProductRepository repository) {
        return args -> {
            Flux< Product > productFlux = Flux.just(
                    new Product(null, "Black Coffee", 2.75),
                    new Product(null, "Coffee with Cream", 3.25),
                    new Product(null, "Latte", 3.75),
                    new Product(null, "Cappuccino", 4.25))
                    .flatMap(repository::save).log();

            productFlux.thenMany(
                    repository.findAll())
                    .subscribe(System.out::println);
        };
    }

}
