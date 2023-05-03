package com.yassine.web.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yassine.web.model.Book;
import com.yassine.web.model.Category;
import com.yassine.web.repository.BookRepository;
import com.yassine.web.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;


@Service

public class CategoryService {
  @Autowired
  public CategoryRepository categoryRepository;
  @Autowired
  public BookRepository bookRepository;

  public void delete(Category category) {
    categoryRepository.delete(category);
  }

  public void deleteAll() {
    categoryRepository.deleteAll();
  }


  public void deleteById(Long id) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Category not found"));

    List<Book> books = bookRepository.findByCategory(category);
    for (Book book : books) {
      book.setCategory(null);
      bookRepository.save(book);
    }
    categoryRepository.delete(category);
  }

  public List<Category> findAll(String keyword) {
    if (keyword != null)
      return categoryRepository.search(keyword);
    return categoryRepository.findAll();
  }

  public List<Category> findAllByOrderByNameAsc() {
    return categoryRepository.findAllByOrderByNameAsc();
  }

  public Category findById(Long id) {
    Optional<Category> optional = categoryRepository.findById(id);
    Category category = null;
    if (optional.isPresent())
      category = optional.get();
    return category;
  }

  public Category findByName(String name) {
    Optional<Category> optional = categoryRepository.findByName(name);
    Category category = null;
    if (optional.isPresent())
      category = optional.get();
    return category;
  }



  public List<Category> findRandomTwelveCategories() {
    return categoryRepository.findRandomCategories();
  }

  public Category save(Category category) {
    return categoryRepository.save(category);
  }

  public List<Category> saveAll(List<Category> list) {
    return categoryRepository.saveAll(list);

  }


}
