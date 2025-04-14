package ponomarev.dev.listbooks.domain;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ponomarev.dev.listbooks.api.BookCreation;
import ponomarev.dev.listbooks.api.BookUpdate;
import ponomarev.dev.listbooks.db.BookEntity;
import ponomarev.dev.listbooks.db.BookRepository;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public List<Book> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(BookEntity::toDomain)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Book create(@Valid BookCreation creation) {
        if(bookRepository.existsByVendorCode(creation.vendorCode())){
            throw new IllegalArgumentException("Vendor code %s already exists".formatted(creation.vendorCode()));
        }
        return bookRepository.save(
                new BookEntity(
                        null,
                        creation.vendorCode(),
                        creation.title(),
                        creation.year(),
                        creation.brand(),
                        creation.stock(),
                        creation.price()
                )
        ).toDomain();
    }

    public Book findById(Long id) {
        var findBook = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book with id %s not found".formatted(id))
        );
        return findBook.toDomain();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Book update(Long id, @Valid BookUpdate bookUpdate) {
        var oldBook = findById(id);
        var updateBookEntity = new BookEntity(
                id,
                bookUpdate.vendorCode() != null ? bookUpdate.vendorCode() : oldBook.vendorCode(),
                bookUpdate.title() != null ? bookUpdate.title() : oldBook.title(),
                bookUpdate.year() != null ? bookUpdate.year() : oldBook.year(),
                bookUpdate.brand() != null ? bookUpdate.brand() : oldBook.brand(),
                bookUpdate.stock() != null ? bookUpdate.stock() : oldBook.stock(),
                bookUpdate.price() != null ? bookUpdate.price() : oldBook.price()
        );
        return bookRepository.save(updateBookEntity).toDomain();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    public Page<Book> findBookEntitiesByTitleAndBrandAndYear(
            String title, String brand, Integer year, int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return bookRepository.findBookEntitiesByTitleAndBrandAndYear(
                title, brand, year, pageable
        ).map(BookEntity::toDomain);
    }
}
