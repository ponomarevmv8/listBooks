package ponomarev.dev.listbooks.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record BookUpdate(
        String vendorCode,
        String title,
        @Min(value = 1800, message = "Year must be more than 1800")
        @Max(value = 2025, message = "Year can't be more than 2025")
        Integer year,
        String brand,
        @Min(value = 0, message = "Stock can't be negative")
        Integer stock,
        @Min(value = 0, message = "Price can't be negative")
        Double price
) {
}
