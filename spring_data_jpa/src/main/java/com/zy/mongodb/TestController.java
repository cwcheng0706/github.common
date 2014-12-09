package com.zy.mongodb;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zy.mongodb.entity.Person;
import com.zy.mongodb.repository.PersonRepository;


@Controller
@RequestMapping(value = "/mongodb")
public class TestController {
	
	private final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private PersonRepository personRepository;
	
	@RequestMapping(value="/save",method=RequestMethod.GET)
	public void testSave() {
		logger.info("保存");
		try{

			mongoTemplate.insert(new Person("zy", 12));
			personRepository.save(new Person("111", 23));
			personRepository.save(new Person("121", 5));
			
			
			List<Person> list = personRepository.findAll();
			if(null != list) {
				for(Person p : list) {
					logger.info(p.toString());
				}
			}
			
		}catch(Exception e) {
			logger.error(e.toString());
		}
//		repository.deleteAll();

		// save a couple of customers
//		repository.save(new Customer1("Alice", "Smith"));
//		repository.save(new Customer1("Bob", "Smith"));
//
//		// fetch all customers
//		System.out.println("Customers found with findAll():");
//		System.out.println("-------------------------------");
//		for (Customer1 customer : repository.findAll()) {
//			logger.info(customer.toString());
//		}
//		System.out.println();
//
//		// fetch an individual customer
//		logger.info("Customer found with findByFirstName('Alice'):");
//		logger.info("--------------------------------");
//		logger.info(repository.findByFirstName("Alice").toString());
//
//		logger.info("Customers found with findByLastName('Smith'):");
//		logger.info("--------------------------------");
//		for (Customer1 customer : repository.findByLastName("Smith")) {
//			logger.info(customer.toString());
//		}

	}
	
	@RequestMapping("/customer/${id}")
	public void findById(@PathVariable String id) {
		logger.info("id:" + id);
		
		
	}
}
