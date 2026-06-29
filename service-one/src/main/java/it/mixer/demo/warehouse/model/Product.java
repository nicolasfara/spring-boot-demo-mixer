package it.mixer.demo.warehouse.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a product in the warehouse inventory")
public record Product(
        @Schema(description = "Unique identifier of the product", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "Stock Keeping Unit", example = "WIDGET-001")
        String sku,

        @Schema(description = "Name of the product", example = "Super Widget")
        String name,

        @Schema(description = "Current quantity in stock", example = "150")
        Integer quantity,

        @Schema(description = "Aisle and bin location in the warehouse", example = "Aisle 4, Bin 2B")
        String location
) {}
