package it.mixer.demo2.client;

import it.mixer.demo.domain.product.Product;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductServiceClient {

    private final WebClient webClient;

    public ProductServiceClient(@Qualifier("serviceOneWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<Product> fetchAllProducts() {
        return webClient.get()
                .uri("/api/v1/products")
                .retrieve()
                .bodyToFlux(Product.class);
    }

    public Mono<Product> fetchProductById(String id) {
        return webClient.get()
                .uri("/api/v1/products/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }
}
