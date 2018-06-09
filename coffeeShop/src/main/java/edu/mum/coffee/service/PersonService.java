package edu.mum.coffee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.mum.coffee.domain.Person;
import edu.mum.coffee.domain.Role;
import edu.mum.coffee.domain.User;
import edu.mum.coffee.repository.PersonRepository;
import edu.mum.coffee.repository.RoleRepository;
import edu.mum.coffee.repository.UserRepository;

@Service
@Transactional
public class PersonService {

	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public Person savePerson(Person person) {
		return personRepository.save(person);
	}

	public List<Person> findByEmail(String email) {
		return personRepository.findByEmail(email);
	}

	public Person findById(Long id) {
		return personRepository.findOne(id);
	}

	public void removePerson(Person person) {
		personRepository.delete(person);
	}

	public List<Person> findAll(){
		return personRepository.findAll();
	}
	
	public void signup(Person person) {
		person.setEnable(true);;

		User user = new User();
		user.setEmail(person.getEmail());
		user.setEnabled(true);
		Role customerRole = roleRepository.findByRole("ROLE_CUSTOMER");
		
		System.out.println("customerRole:" + customerRole);
		user.addRole(customerRole);
		//user.setPassword(person.getPassword());
		user.setPassword(passwordEncoder.encode(person.getPassword()));

		person = personRepository.save(person);
		userRepository.save(user);
	}
}
