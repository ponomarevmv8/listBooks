package ponomarev.dev.listbooks.api;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ponomarev.dev.listbooks.domain.Book;
import ponomarev.dev.listbooks.domain.BookService;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private static final String SUCCESS_MESSAGE = "successMessage";
    private static final String REDIRECT = "redirect:/books";
    private static final String LAYOUT = "layout";
    private static final String FORM = "book/form";

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String listBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model
    ) {
        String titleParam = title == null ? "" : title;
        String brandParam = brand == null ? "" : brand;

        Page<BookDto> bookDtoPage = bookService.findBookEntitiesByTitleAndBrandAndYear(
                titleParam, brandParam, year, page, size, sortBy, direction
        ).map(Book::toDto);

        model.addAttribute("books", bookDtoPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookDtoPage.getTotalPages());
        model.addAttribute("totalItems", bookDtoPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("title", title);
        model.addAttribute("brand", brand);
        model.addAttribute("year", year);

        model.addAttribute("pageTitle", "Список книг");
        model.addAttribute("currentPageName", "books");
        model.addAttribute("content", "book/list");
        setUserDetails(model);

        return LAYOUT;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddForm(Model model) {
        model.addAttribute("bookForm", new BookForm());
        model.addAttribute("title", "Добавление книги");
        model.addAttribute("sortField", "id");
        model.addAttribute("sortDirection", "ASC");
        model.addAttribute("content", FORM);
        setUserDetails(model);
        return LAYOUT;
    }

    @PostMapping("/add")
    public String createBook(
            @ModelAttribute @Valid BookForm bookForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        BookCreation bookCreation = bookForm.toBookCreation();

        if (bindingResult.hasErrors()) {
            return FORM;
        }

        bookService.create(bookCreation);
        redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Книга успешно добавлена!");
        return REDIRECT;
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(
            @PathVariable Long id, Model model
    ) {
        try {
            BookDto updatedBook = bookService.findById(id).toDto();

            BookForm bookForm = new BookForm(updatedBook);

            model.addAttribute("bookForm", bookForm);
            model.addAttribute("bookId", id);
            model.addAttribute("title", "Редактирование книги");
            model.addAttribute("sortField", "id");
            model.addAttribute("sortDirection", "ASC");
            model.addAttribute("content", FORM);
            setUserDetails(model);

            return LAYOUT;
        } catch (Exception e) {
            return REDIRECT;
        }
    }

    @PostMapping("/edit/{id}")
    public String updateBook(
            @PathVariable Long id,
            @ModelAttribute("bookForm") @Valid BookForm bookForm,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return FORM;
        }

        BookUpdate bookUpdate = bookForm.toBookUpdate();

        bookService.update(id, bookUpdate);

        redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Книга успешо обновлена!");
        return REDIRECT;
    }

    @DeleteMapping("/{id}")
    public String deleteBook(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            bookService.delete(id);
            redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Книга удалена!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        return REDIRECT;
    }

    private void setUserDetails(Model model) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            model.addAttribute("userName", auth.getName());
            model.addAttribute("isAdmin", auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) );
        }
    }

}
