package com.mycompany;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@Import(RestDocsDemoApplication.class)
@WebAppConfiguration
public class RestDocsDemoApplicationTests {

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	private RestDocumentationResultHandler document;

	@Before
	public void setUp() {
		this.document = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(this.restDocumentation))
				.alwaysDo(this.document)
				.build();
		createSamplePerson("Mary", "Queen");
		createSamplePerson("John", "Doe");
		createSamplePerson("George", "King");
	}

	@Test
	public void listPeople() throws Exception {
		this.mockMvc.perform(
				get("/people").accept(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk())
		.andDo(document("index",responseFields(fieldWithPath("[].id").description("The persons' ID"),
				fieldWithPath("[].firstName").description("The persons' first name"),
				fieldWithPath("[].lastName").description("The persons' last name"))))
		;
	}

	@Test
	public void updatePerson()  throws Exception {
		RestDocsDemoApplication.Person person = new RestDocsDemoApplication.Person("Mary", "Queen II");
		this.mockMvc.perform(put("/people/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(person.toJson()))
				.andExpect(status().is(204));
	}

	private RestDocsDemoApplication.Person createSamplePerson(String firstName, String lastName) {
		return personRepository.save(new RestDocsDemoApplication.Person(firstName, lastName));
	}

}