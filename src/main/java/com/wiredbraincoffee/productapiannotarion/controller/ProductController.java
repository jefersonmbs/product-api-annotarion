package com.wiredbraincoffee.productapiannotarion.controller;

import com.wiredbraincoffee.productapiannotarion.model.Product;
import com.wiredbraincoffee.productapiannotarion.model.ProductEvent;
import com.wiredbraincoffee.productapiannotarion.repository.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;


/**
 * @author Jeferson Martins @Capgemini
 * 23/03/2022
 */

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public Flux< Product > findAll() {
        return productRepository.findAll();
    }

    @GetMapping("{id}")
    public Mono< ? > getProduct(@PathVariable String id) {
        return productRepository.findById(id).map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono< ? > save(@RequestBody Product product) {
        return productRepository.save(product)
                .map(savedProduct  -> ResponseEntity.ok(savedProduct))
                .defaultIfEmpty(ResponseEntity.internalServerError().build());
    }

    @GetMapping("/search/{name}")
    public Flux< Product > findByName(@PathVariable String name) {
        return productRepository.findByNameContaining(name);
    }

    @PutMapping("{id}")
    public Mono< ? > updateProduct(@PathVariable String id, @RequestBody Product product) {
        return productRepository.findById(id).flatMap(existingProduct -> {
            existingProduct.setName(StringUtils.isNotBlank(product.getName()) ? product.getName() : existingProduct.getName());
            existingProduct.setPrice(product.getPrice() != 0L ? product.getPrice() : existingProduct.getPrice());
            return productRepository.save(existingProduct);
        }).map(updatedProduct -> ResponseEntity.ok(updatedProduct))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public Mono< ? > delete(@PathVariable String id) {
        return productRepository.findById(id)
                .flatMap(existingProduct -> productRepository.delete(existingProduct)
                .then(Mono.just(ResponseEntity.ok().build()))
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public Mono< ? > deleteAll() {
        return productRepository.deleteAll()
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux< ProductEvent > getEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> new ProductEvent(sequence, "Product Event " + sequence));
    }
}

