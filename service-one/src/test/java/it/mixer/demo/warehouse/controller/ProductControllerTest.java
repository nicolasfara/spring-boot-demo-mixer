package it.mixer.demo.warehouse.controller;

import it.mixer.demo.warehouse.model.Product;
import it.mixer.demo.warehouse.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockitoBean
    private ProductRepository productRepository;

    private final Product sampleProduct1 = new Product("1", "SKU-001", "Widget A", 10, "A1");
    private final Product sampleProduct2 = new Product("2", "SKU-002", "Widget B", 20, "B2");

    @Test
    void getAllProducts() {
        when(productRepository.findAll()).thenReturn(Flux.just(sampleProduct1, sampleProduct2));

        webClient.get()
                .uri("/api/v1/products")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Product.class)
                .hasSize(2)
                .contains(sampleProduct1, sampleProduct2);
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findById("1")).thenReturn(Mono.just(sampleProduct1));

        webClient.get()
                .uri("/api/v1/products/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.name").isEqualTo("Widget A");
    }

    @Test
    void getProductById_NotFound() {
        when(productRepository.findById("999")).thenReturn(Mono.empty());

        webClient.get()
                .uri("/api/v1/products/999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createProduct() {
        Product newProduct = new Product(null, "SKU-NEW", "New Widget", 5, "C3");
        Product savedProduct = new Product("3", "SKU-NEW", "New Widget", 5, "C3");

        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(savedProduct));

        webClient.post()
                .uri("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newProduct)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("3")
                .jsonPath("$.name").isEqualTo("New Widget");
    }

    @Test
    void updateProduct_Success() {
        Product updatedInfo = new Product(null, "SKU-UPDATED", "Updated Widget", 15, "A1");
        Product updatedProduct = new Product("1", "SKU-UPDATED", "Updated Widget", 15, "A1");

        when(productRepository.findById("1")).thenReturn(Mono.just(sampleProduct1));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(updatedProduct));

        webClient.put()
                .uri("/api/v1/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedInfo)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Updated Widget")
                .jsonPath("$.quantity").isEqualTo(15);
    }

    @Test
    void updateProduct_NotFound() {
        Product updatedInfo = new Product(null, "SKU-UPDATED", "Updated Widget", 15, "A1");

        when(productRepository.findById("999")).thenReturn(Mono.empty());

        webClient.put()
                .uri("/api/v1/products/999")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedInfo)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteProduct() {
        when(productRepository.deleteById("1")).thenReturn(Mono.empty());

        webClient.delete()
                .uri("/api/v1/products/1")
                .exchange()
                .expectStatus().isNoContent();

        Mockito.verify(productRepository).deleteById("1");
    }
}
