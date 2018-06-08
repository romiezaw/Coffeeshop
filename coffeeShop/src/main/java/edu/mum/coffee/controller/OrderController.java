package edu.mum.coffee.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import edu.mum.coffee.domain.Order;
import edu.mum.coffee.domain.Person;
import edu.mum.coffee.service.OrderService;
import edu.mum.coffee.service.PersonService;

@Controller
public class OrderController {
	@Autowired
	private OrderService orderService;

	@Autowired
	private PersonService personService;

	@GetMapping("/order")
	public String orderHome(Model model) {
		model.addAttribute("order", new Order());
		return "order";
	}

	@GetMapping("/order/{orderId}")
	public String orderDetail(@PathParam("orderId") int id, Model model) {
		model.addAttribute("order", orderService.findById(id));
		return "order";
	}

	@GetMapping("/orders")
	public String orderList(Model model) {
		model.addAttribute("orders", orderService.findAll());
		return "orderList";
	}
	
	@PostMapping("/order/{orderId}")
	public String updateOrder(@PathParam("orderId") Long id, @ModelAttribute Order order) {
		orderService.save(order);
		return "orderList";
	}
	
	@PostMapping("/placeOrder")
	public String createOrder(HttpSession session, Authentication authentication , @ModelAttribute Person person) {
		Object orderObj = session.getAttribute("orderCart");
		if (orderObj == null) {
			orderObj = new Order();
			session.setAttribute("orderCart", orderObj);
		}
		Order order = (Order) orderObj;
		order.setOrderDate(new Date());

		//UserAdapter userAdapter = (UserAdapter) authentication.getPrincipal();
		List<Person> persons = personService.findByEmail("ymzaw@mum.edu");
				//findByEmail(userAdapter.getUser().getEmail());
		order.setPerson(persons.get(0));
		orderService.save(order);

		session.removeAttribute("orderCart");
		return "redirect:/";
	}

	
}
