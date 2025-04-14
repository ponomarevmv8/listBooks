package ponomarev.dev.listbooks.domain;

import ponomarev.dev.listbooks.api.BookDto;

public record Book(
        Long id,
        String vendorCode,
        String title,
        Integer year,
        String brand,
        Integer stock,
        Double price
) {

    public BookDto toDto() {
        return new BookDto(id, vendorCode, title, year, brand, stock, price);
    }
}
