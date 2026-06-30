package it.mixer.demo.warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.mixer.demo.domain.product.Product;
import it.mixer.demo.domain.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Warehouse Inventory", description = "Operations pertaining to product inventory in the warehouse")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(
            summary = "List all products",
            description = "Retrieves a complete list of all products currently in the warehouse inventory."
    )
    public Flux<Product> getAllProducts() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a product by ID",
            description = "Retrieves details of a specific product based on its unique ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product found"),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    public Mono<ResponseEntity<Product>> getProductById(
            @Parameter(description = "ID of the product to retrieve", required = true)
            @PathVariable String id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Add a new product",
            description = "Creates a new product record in the inventory. The ID will be auto-generated."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Product successfully created")
    })
    public Mono<Product> createProduct(
            @Parameter(description = "Product object to add to the inventory", required = true)
            @RequestBody Product product) {
        return repository.save(product);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing product",
            description = "Updates the details of an existing product based on its ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product successfully updated"),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    public Mono<ResponseEntity<Product>> updateProduct(
            @Parameter(description = "ID of the product to update", required = true)
            @PathVariable String id,
            @Parameter(description = "Updated product object", required = true)
            @RequestBody Product product) {
        return repository.findById(id)
                .flatMap(existing -> {
                    Product updated = new Product(id, product.sku(), product.name(),
                            product.quantity(), product.location());
                    return repository.save(updated);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete a product",
            description = "Removes a product from the warehouse inventory by its ID."
    )
    @ApiResponse(responseCode = "204", description = "Product successfully deleted")
    public Mono<Void> deleteProduct(
            @Parameter(description = "ID of the product to delete", required = true)
            @PathVariable String id) {
        return repository.deleteById(id);
    }
}
