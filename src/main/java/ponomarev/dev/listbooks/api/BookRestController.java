package ponomarev.dev.listbooks.api;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ponomarev.dev.listbooks.domain.Book;
import ponomarev.dev.listbooks.domain.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final BookService bookService;

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> findAll() {
        return ResponseEntity.ok(
                bookService.findAll()
                        .stream()
                        .map(Book::toDto)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id).toDto());
    }

    @PostMapping
    public ResponseEntity<BookDto> create(
            @RequestBody @Valid BookCreation bookCreation
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        bookService.create(bookCreation)
                                .toDto()
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> update(
            @PathVariable Long id,
            @RequestBody @Valid BookUpdate bookUpdate
    ) {
        return ResponseEntity.ok(
                bookService.update(id, bookUpdate).toDto()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookDto>> findBookEntitiesByTitleAndBrandAndYear(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<BookDto> bookDtoPage = bookService.findBookEntitiesByTitleAndBrandAndYear(
                title, brand, year, page, size, sort, direction
        ).map(Book::toDto);
        return ResponseEntity.ok(bookDtoPage.getContent());
    }

}
