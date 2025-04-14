package ponomarev.dev.listbooks.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookCreation(
        @NotBlank(message = "Vendor code is required")
        String vendorCode,
        @NotBlank(message = "Title is required")
        String title,
        @NotNull(message = "Year is required")
        @Min(value = 1800, message = "Year must be more than 1800")
        @Max(value = 2025, message = "Year can't be more than 2025")
        Integer year,
        @NotBlank(message = "Brand is required")
        String brand,
        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock can't be negative")
        Integer stock,
        @NotNull(message = "Price is required")
        @Min(value = 0, message = "Price can't be negative")
        Double price
) {
}
