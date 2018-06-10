
package edu.mum.coffee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import edu.mum.coffee.domain.Product;
import edu.mum.coffee.domain.ProductType;
import edu.mum.coffee.service.ProductService;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/product")
	public String productHome(Model model) {
		model.addAttribute("product", new Product());
		model.addAttribute("productTypes", ProductType.values());
		return "product";
	}
	
	@PostMapping("/product")
	public String createProduct(@ModelAttribute Product product) {
		System.out.println("In post product--->" + product);
		productService.save(product);
		return "redirect:/products";
	}
	
//	@PostMapping("/product/{id}")
//	public String update(@PathVariable("id") int productId, @ModelAttribute Product product) {
//		System.out.println("In post product");
//		Product productToUpdate = productService.getProduct(productId);
//		productService.save(productToUpdate);
//		return "redirect:/products";
//	}
	
	@GetMapping("/products")
	public String productList(Model model) {
		List<Product> products = productService.getAllProduct();
		model.addAttribute("productList", products);
		return "productList";
	}
	
	@GetMapping("/delete/{productId}")
	public String deleteProduct(Product product, @PathVariable("productId") int productId) {
		Product productToDelete = productService.getProduct(productId);
		productService.delete(productToDelete);
		return "redirect:/products";
	}
	
	@GetMapping("/product/{id}")
	public String updateProduct(@PathVariable("id") int productId, Model model) {
		System.out.println("In get product");
		model.addAttribute("product", productService.getProduct(productId));
		model.addAttribute("productTypes", ProductType.values());
		return "product";
	}
	
}
