package com.yassine.web.controller.admin;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.yassine.web.model.Category;
import com.yassine.web.service.CategoryService;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @GetMapping("/delete/{id}")
  public String deleteCategory(@PathVariable(value = "id") long id) {
    categoryService.deleteById(id);
    return "redirect:/admin/category/";
  }



  @PostMapping("/save")
  public String saveCategory(@ModelAttribute("category") Category category,
      @RequestParam("img") MultipartFile imageFile) throws IOException {
    // save Category to database
    if (!imageFile.isEmpty()) {
      byte[] imageBytes = imageFile.getBytes();
      category.setCoverImage(imageBytes);
    }
    categoryService.save(category);
    return "redirect:/admin/category/";
  }

  @GetMapping("/showNewCategoryForm")
  public String showNewCategoryForm(Model model) {
    // create model attribute to bind form data
    Category category = new Category();
    model.addAttribute("category", category);
    return "admin/add-category";
  }

  /*
   * @GetMapping("/showFormForUpdate/{id}") public String showFormForUpdate(@PathVariable(value =
   * "id") long id, Model model) {
   * 
   * // get Category from the service Category category = categoryService.findById(id);
   * 
   * // set Category as a model attribute to pre-populate the form model.addAttribute("category",
   * category); return "admin/update-category"; }
   */



  @RequestMapping(value = "/update/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
  public String updateCategory(@PathVariable("id") Long id, @RequestParam("name") String name,
      @RequestParam("description") String description, @RequestParam("image") MultipartFile image)
      throws IOException {
    Category category = categoryService.findById(id);
    category.setName(name);
    category.setDescription(description);
    byte[] cover = image.getBytes();
    category.setCoverImage(cover);
    categoryService.save(category);
    return "redirect:/admin/category/";
  }

  // display list of Categorys
  @GetMapping({"", "/"})
  public String viewHomePage(Model model) {
    List<Category> categories = categoryService.findAll(null);

    model.addAttribute("categories", categories);
    return "admin/categories";
  }
}
