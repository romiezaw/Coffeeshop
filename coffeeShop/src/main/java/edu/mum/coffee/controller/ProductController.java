
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
		productService.save(product);
		return "redirect:/products";
	}
	
	@GetMapping("/products")
	public String productList(Model model) {
		List<Product> products = productService.getAllProduct();
		System.out.println(products.size());
		model.addAttribute("productList", productService.getAllProduct());
		return "productList";
	}
	
	@PostMapping("/products")
	public String deleteProduct(Product product) {
		productService.delete(product);
		return "redirect:/products";
	}
	
	@GetMapping("/product/{productId}")
	public String updateProduct(@PathVariable("productId") ProductType productType, Model model) {
		model.addAttribute("product", productService.findByProductType(productType));
		model.addAttribute("productTypes", ProductType.values());
		return "product";
	}
}
