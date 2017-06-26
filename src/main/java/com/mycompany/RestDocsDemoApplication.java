package com.mycompany;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SpringBootApplication
public class RestDocsDemoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(RestDocsDemoApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(RestDocsDemoApplication.class);
	}


	@RestController
	@RequestMapping("/people")
	public class PersonController {
		@Autowired
		private PersonRepository personRepository;

		@RequestMapping(value = "", method = RequestMethod.GET)
		public Iterable<Person> listPeople() {
			return personRepository.findAll();
		}

		@RequestMapping(value="/{id}", method = RequestMethod.GET)
		public Person getPerson(@PathVariable("id") Long id) {
			return personRepository.findOne(id);
		}

		@RequestMapping(value = "", method = RequestMethod.POST)
		@ResponseStatus(HttpStatus.CREATED)
		public void createPerson(@RequestBody Person person) {
			personRepository.save(new Person(person.getFirstName(), person.getLastName()));
		}

		@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
		@ResponseStatus(HttpStatus.NO_CONTENT)
		public void updatePerson(@PathVariable("id") Long id, @RequestBody Person person) {
			Person existingPerson = personRepository.findOne(id);
			existingPerson.setFirstName(person.getFirstName());
			existingPerson.setLastName(person.getLastName());
			personRepository.save(existingPerson);
		}
	}


	@Entity
	public static class Person {
		@Id
		@GeneratedValue
		private Long id;

		@NotNull
		@Size(min = 1, max = 20)
		private String firstName;

		@NotNull
		@Size(min = 1, max = 50)
		private String lastName;

		public Person() {
		}

		public Person(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String toJson() {
			return new Gson().toJson(this);
		}
	}

}
