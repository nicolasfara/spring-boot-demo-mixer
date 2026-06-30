package it.mixer.demo.warehouse.repository;

import it.mixer.demo.domain.product.Product;
import it.mixer.demo.domain.repository.ProductRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * A mock production-ready implementation (e.g., this would normally connect to PostgreSQL/MongoDB).
 * Active ONLY when the 'prod' profile is active.
 */
@Repository
@Profile("prod")
public class ProductionProductRepository implements ProductRepository {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ProductionProductRepository.class);

    public ProductionProductRepository() {
        LOGGER.info("Connecting to Production Database...");
    }

    @Override
    public Flux<Product> findAll() {
        // Here you would execute a real SQL/NoSQL query
        return Flux.empty(); 
    }

    @Override
    public Mono<Product> findById(String id) {
        return Mono.empty();
    }

    @Override
    public Mono<Product> save(Product product) {
        return Mono.just(product);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return Mono.empty();
    }
}
