package com.yassine.web.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.yassine.web.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  List<Category> findAllByOrderByNameAsc();

  Optional<Category> findByName(String name);

  @Query("SELECT c FROM Category c ORDER BY RAND() LIMIT 12")
  List<Category> findRandomCategories();


  @Query("SELECT c.id, COUNT(b) " + "FROM Book b LEFT JOIN b.category c " + "GROUP BY c.id")
  List<Object[]> getCategoryBookCounts();

  @Query("SELECT c FROM Category c WHERE UPPER(c.name) LIKE CONCAT('%',UPPER(?1),'%')")
  List<Category> search(String keyword);

}
