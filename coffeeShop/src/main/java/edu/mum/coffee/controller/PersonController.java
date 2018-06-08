package edu.mum.coffee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import edu.mum.coffee.domain.Person;
import edu.mum.coffee.service.OrderService;
import edu.mum.coffee.service.PersonService;

@Controller
public class PersonController {

	@Autowired
	private PersonService personService;

	@Autowired
	private OrderService orderService;

	@GetMapping("/person")
	public String createPerson(Model model) {
		model.addAttribute("person", new Person());
		return "person";
	}

	@GetMapping("/person/{personId}")
	public String personDetail(@PathVariable("personId") Long personId, Model model) {
		model.addAttribute("person", personService.findById(personId));
		return "person";
	}

	@PostMapping("/person")
	public String createPerson(@ModelAttribute("person") Person person) {
		personService.savePerson(person);
		return "redirect:/persons";
	}
	

	@GetMapping("/persons")
	public String personList(Model model) {
		model.addAttribute("persons", personService.findAll());
		return "personList";
	}
}
