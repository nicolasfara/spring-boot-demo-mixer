package it.mixer.demo.domain.repository;

import it.mixer.demo.domain.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface defining the standard operations for a Product Repository.
 */
public interface ProductRepository {
    
    Flux<Product> findAll();
    
    Mono<Product> findById(String id);
    
    Mono<Product> save(Product product);
    
    Mono<Void> deleteById(String id);
}
