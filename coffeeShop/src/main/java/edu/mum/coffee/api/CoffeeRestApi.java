package edu.mum.coffee.api;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.mum.coffee.adapters.UserAdapter;
import edu.mum.coffee.domain.Order;
import edu.mum.coffee.domain.Orderline;
import edu.mum.coffee.domain.Person;
import edu.mum.coffee.domain.Product;
import edu.mum.coffee.service.OrderService;
import edu.mum.coffee.service.PersonService;
import edu.mum.coffee.service.ProductService;

@RestController
@RequestMapping("/api")
public class CoffeeRestApi {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private PersonService personService;
	
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public List<Product> getAllProduct() {
		List<Product> products = productService.getAllProduct();
		return products;
	}
	
	@RequestMapping(value = "/cart", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void addProductToCart(@RequestBody int productId, HttpSession session) {
		System.out.println("In cart");
		Object orderObj = session.getAttribute("orderCart");
		if (orderObj == null) {
			orderObj = new Order();
			session.setAttribute("orderCart", orderObj);
		}
		Order order = (Order) orderObj;

		Optional<Orderline> orderLine = order.getOrderLines().stream().filter(orderline
				-> orderline.getProduct().getId() == productId).findFirst();
		if (orderLine.isPresent()) {
			Orderline orderline = orderLine.get();
			orderline.setQuantity(orderline.getQuantity() + 1);
		} else {
			Product product = productService.getProduct(productId);
			Orderline orderline  = new Orderline();
			
			System.out.println("product " + product);
			System.out.println("orderLine " + orderline);
			orderline.setProduct(product);
			orderline.setQuantity(1);
			order.addOrderLine(orderline);
		}
	}

	@RequestMapping(value = "/product/{productId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteProduct(@PathVariable("productId") int productId) {
		productService.delete(productService.getProduct(productId));
	}
	
	@PostMapping("/placeOrder")
	public String createOrder(HttpSession session, Authentication authentication , @ModelAttribute Person person) {
		System.out.println("In place order.....");
		Object orderObj = session.getAttribute("orderCart");
		if (orderObj == null) {
			orderObj = new Order();
			session.setAttribute("orderCart", orderObj);
			System.out.println("...orderCart.." + orderObj);
		}
		Order order = (Order) orderObj;
		order.setOrderDate(new Date());
		System.out.println("order : " + order);

		System.out.println("... auth .." + authentication.getPrincipal());
		UserAdapter userAdapter = (UserAdapter) authentication.getPrincipal();
		List<Person> persons = personService.findByEmail(userAdapter.getUser().getEmail());
				//findByEmail("ymzaw@mum.edu");
				
		order.setPerson(persons.get(0));
		orderService.save(order);

		session.removeAttribute("orderCart");
		
		return "success";
	}
	
}
