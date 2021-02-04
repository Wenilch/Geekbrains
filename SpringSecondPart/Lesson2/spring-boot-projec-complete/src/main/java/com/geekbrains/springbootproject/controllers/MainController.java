package com.geekbrains.springbootproject.controllers;

import com.geekbrains.springbootproject.entities.Product;
import com.geekbrains.springbootproject.services.CategoryService;
import com.geekbrains.springbootproject.services.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class MainController {
	private ProductsService productsService;
	private CategoryService categoryService;

	@Autowired
	public void setProductsService(ProductsService productsService) {
		this.productsService = productsService;
	}

	@Autowired
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping("/info")
	public String showInfoPage(Model model) {
		return "info";
	}

	@GetMapping("/product/edit/{id}")
	public String addProductPage(Model model, @PathVariable("id") Optional<Long> id) {
		Product product = null;
		if (id.isPresent()) {
			product = productsService.findById(id.get());
		}

		if (product == null) {
			product = new Product();
		}
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("product", product);
		return "edit-product";
	}

	// Binding Result после @ValidModel !!!
	@PostMapping("/product/edit")
	public String addProduct(@Valid @ModelAttribute("product") Optional<Product> product, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "edit-product";
		}

		if (product.isPresent()) {
			Product optionalProduct = product.get();
			Product existing = productsService.findByTitle(optionalProduct.getTitle());
			if (existing != null && (optionalProduct.getId() == null || !optionalProduct.getId().equals(existing.getId()))) {
				model.addAttribute("product", product);
				model.addAttribute("productCreationError", "Product title already exists");
				return "edit-product";
			}
			productsService.saveOrUpdate(optionalProduct);
		}
		return "redirect:/";
	}
}
