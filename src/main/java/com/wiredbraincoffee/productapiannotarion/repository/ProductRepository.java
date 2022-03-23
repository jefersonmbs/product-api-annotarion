package com.wiredbraincoffee.productapiannotarion.repository;

import com.wiredbraincoffee.productapiannotarion.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * @author Jeferson Martins @Capgemini
 * 22/03/2022
 */

public interface ProductRepository  extends ReactiveMongoRepository < Product, String> {

   Flux<Product> findByNameContaining(String name);
}
