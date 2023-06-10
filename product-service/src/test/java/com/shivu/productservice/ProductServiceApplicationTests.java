package com.shivu.productservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shivu.productservice.dto.ProductRequest;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	//Get the docker image of Mongodb to get it ready for integration test
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@Autowired
	private MockMvc mockMvc;

	//Pojo to JSON and vice-versa
	@Autowired
	ObjectMapper objectMapper;

	//Provide the uri to database
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void contextLoads() {
	
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String prodreqstring = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders
						.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(prodreqstring))
						.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest
					.builder()
					.name("Test")
					.description("Testing the POST API")
					.price(Float.valueOf("20.64")).build();

	}


}
