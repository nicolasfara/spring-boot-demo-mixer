package it.mixer.demo2.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.mixer.demo.domain.product.Product;
import it.mixer.demo2.config.WebClientConfig;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceClientIntegrationTest {

    private static MockWebServer mockBackEnd;
    private ProductServiceClient productServiceClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
        
        // Simulate what the WebClientConfig does
        WebClient webClient = new WebClientConfig().serviceOneWebClient(WebClient.builder(), baseUrl);
        productServiceClient = new ProductServiceClient(webClient);
    }

    @Test
    void fetchProductByIdReturnsProductWhenServiceOneRespondsOk() throws Exception {
        // Arrange
        Product mockProduct = new Product("123", "SKU-001", "Mocked Product", 10, "Aisle 1");
        
        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockProduct))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        // Act & Assert
        StepVerifier.create(productServiceClient.fetchProductById("123"))
                .expectNextMatches(product -> 
                        product.id().equals("123") 
                        && product.name().equals("Mocked Product")
                )
                .verifyComplete();

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("GET");
        assertThat(recordedRequest.getPath()).isEqualTo("/api/v1/products/123");
    }
    
    @Test
    void fetchAllProductsReturnsFluxOfProductsWhenServiceOneRespondsOk() throws Exception {
        // Arrange
        Product[] mockProducts = new Product[]{
            new Product("1", "SKU-1", "Product 1", 10, "Aisle 1"),
            new Product("2", "SKU-2", "Product 2", 20, "Aisle 2")
        };
        
        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockProducts))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        // Act & Assert
        StepVerifier.create(productServiceClient.fetchAllProducts())
                .expectNextMatches(p -> p.id().equals("1"))
                .expectNextMatches(p -> p.id().equals("2"))
                .verifyComplete();

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("GET");
        assertThat(recordedRequest.getPath()).isEqualTo("/api/v1/products");
    }
}
