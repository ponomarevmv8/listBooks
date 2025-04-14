package ponomarev.dev.listbooks.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookForm {
    @NotBlank(message = "Vendor code is required")
    private String vendorCode;

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Year is required")
    @Min(value = 1800, message = "Year must be more than 1800")
    @Max(value = 2025, message = "Year can't be more than 2025")
    private Integer year;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock can't be negative")
    private Integer stock;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price can't be negative")
    private Double price;

    public BookForm() {
    }

    public BookForm(String vendorCode, String title, Integer year, String brand, Integer stock, Double price) {
        this.vendorCode = vendorCode;
        this.title = title;
        this.year = year;
        this.brand = brand;
        this.stock = stock;
        this.price = price;
    }

    public BookForm(BookDto bookDto) {
        this.vendorCode = bookDto.vendorCode();
        this.title = bookDto.title();
        this.year = bookDto.year();
        this.brand = bookDto.brand();
        this.stock = bookDto.stock();
        this.price = bookDto.price();
    }

    public BookCreation toBookCreation() {
        return new BookCreation(vendorCode, title, year, brand, stock, price);
    }

    public BookUpdate toBookUpdate() {
        return new BookUpdate(vendorCode, title, year, brand, stock, price);
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
