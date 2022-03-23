package com.wiredbraincoffee.productapiannotarion.controller;

import com.wiredbraincoffee.productapiannotarion.model.Product;
import com.wiredbraincoffee.productapiannotarion.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Jeferson Martins @Capgemini
 * 23/03/2022
 */

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public Flux< Product > findAll() {
        return productRepository.findAll();
    }

    @GetMapping("{id}")
    public Mono<Product> getProduct(@PathVariable String id) {
        return productRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<Product> save(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @GetMapping("/search/{name}")
    public Flux<Product> findByName(@PathVariable String name) {
        return productRepository.findByNameContaining(name);
    }

    @PutMapping("{id}")
    public Mono<Product> updateProduct(@PathVariable String id, @RequestBody Product product) {
        return productRepository.findById(id)
                .flatMap(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setPrice(product.getPrice());
                    return productRepository.save(existingProduct);
                });
    }

    @DeleteMapping("{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return productRepository.findById(id)
                .flatMap(existingProduct -> productRepository.delete(existingProduct));
    }
}

