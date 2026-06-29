package it.mixer.demo.warehouse.repository;

import it.mixer.demo.warehouse.model.Product;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of the ProductRepository for demonstration purposes.
 * Active ONLY when the 'local' profile is active.
 */
@Repository
@Profile("local")
public class InMemoryProductRepository implements ProductRepository {

    private final Map<String, Product> database = new ConcurrentHashMap<>();

    public InMemoryProductRepository() throws InterruptedException {
        // Seed some initial data
        save(new Product(null, "SKU-A1", "Standard Box", 500, "Aisle 1"))
                .and(save(new Product(null, "SKU-B2", "Heavy Duty Pallet", 50, "Aisle 9")))
                .and(save(new Product(null, "SKU-C3", "Fragile Glassware", 200, "Aisle 5")));
    }

    @Override
    public Flux<Product> findAll() {
        return Flux.fromIterable(database.values());
    }

    @Override
    public Mono<Product> findById(String id) {
        return Mono.justOrEmpty(database.get(id));
    }

    @Override
    public Mono<Product> save(Product product) {
        String id = product.id() == null ? UUID.randomUUID().toString() : product.id();
        Product savedProduct = new Product(id, product.sku(), product.name(), product.quantity(), product.location());
        database.put(id, savedProduct);
        return Mono.just(savedProduct);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        database.remove(id);
        return Mono.empty();
    }
}
