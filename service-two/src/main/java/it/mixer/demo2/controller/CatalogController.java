package it.mixer.demo2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.mixer.demo.domain.product.Product;
import it.mixer.demo2.client.ProductServiceClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/catalog")
@Tag(name = "Catalog Dashboard", description = "Aggregates information from downstream microservices")
public class CatalogController {

    private final ProductServiceClient productServiceClient;

    public CatalogController(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    @GetMapping("/products")
    @Operation(summary = "Get all products from warehouse", 
               description = "Fetches the product catalog from service-one")
    public Flux<Product> getCatalog() {
        return productServiceClient.fetchAllProducts();
    }

    @GetMapping("/products/{id}")
    @Operation(summary = "Get a single product from warehouse", 
               description = "Fetches a specific product from service-one")
    public Mono<Product> getCatalogItem(@PathVariable String id) {
        return productServiceClient.fetchProductById(id);
    }
}
