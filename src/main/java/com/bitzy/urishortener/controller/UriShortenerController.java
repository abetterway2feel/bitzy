package com.bitzy.urishortener.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

@RestController
public class UriShortenerController {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping("/shorten")
  public String create(@RequestBody String book) {
    return "http://localhost/ABC123";
  }

  @GetMapping("/{key}")
  public String getUriForKey(@PathVariable String key) {
    return "http://somethingverylong.com/with/lots/of/sub/paths/too";
  }

}

/**
 * @Autowired private BookRepository bookRepository;
 * 
 * @GetMapping public Iterable findAll() { return bookRepository.findAll(); }
 * 
 *             @GetMapping("/title/{bookTitle}") public List
 *             findByTitle(@PathVariable String bookTitle) { return
 *             bookRepository.findByTitle(bookTitle); }
 * 
 *             @GetMapping("/{id}") public Book findOne(@PathVariable Long id) {
 *             return bookRepository.findById(id)
 *             .orElseThrow(BookNotFoundException::new); }
 * 
 * @PostMapping
 * @ResponseStatus(HttpStatus.CREATED) public Book create(@RequestBody Book
 *                                     book) { return bookRepository.save(book);
 *                                     }
 * 
 *                                     @DeleteMapping("/{id}") public void
 *                                     delete(@PathVariable Long id) {
 *                                     bookRepository.findById(id)
 *                                     .orElseThrow(BookNotFoundException::new);
 *                                     bookRepository.deleteById(id); }
 * 
 *                                     @PutMapping("/{id}") public Book
 *                                     updateBook(@RequestBody Book
 *                                     book, @PathVariable Long id) { if
 *                                     (book.getId() != id) { throw new
 *                                     BookIdMismatchException(); }
 *                                     bookRepository.findById(id)
 *                                     .orElseThrow(BookNotFoundException::new);
 *                                     return bookRepository.save(book); }
 */