package ponomarev.dev.listbooks.api;

public record BookDto(
        Long id,
        String vendorCode,
        String title,
        Integer year,
        String brand,
        Integer stock,
        Double price
) {
}
