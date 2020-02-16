package test.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import server.PrimesServer;

@TestMethodOrder(OrderAnnotation.class)
class PrimeServerTest {
	
	@Value("server.port")
	private int port = 8081;
	private SpringApplication app;

	@Test
	@Order(1)
	void checkNextAvailable() {
		app = new SpringApplication(PrimesServer.class);
		app.run("");
		RestTemplate req = new RestTemplate();
		ResponseEntity<String> resp = req.getForEntity("http://localhost:"+ port + "/prime/2/next", String.class);
		assertEquals("3", resp.getBody());
		resp = req.getForEntity("http://localhost:"+ port + "/prime/3/next", String.class);
		assertEquals("-1", resp.getBody());
	}
	
	@Test
	@Order(2)
	void checkPrimeInsert() {
		RestTemplate req = new RestTemplate();
		ResponseEntity<String> resp = req.getForEntity("http://localhost:"+ port + "/prime/3/next", String.class);
		assertEquals("-1", resp.getBody());
		req.put("http://localhost:"+ port + "/prime/5", "");
		resp = req.getForEntity("http://localhost:"+ port + "/prime/3/next", String.class);
		assertEquals("5", resp.getBody());
	}
	
	@Test
	@Order(3)
	void checkLastInsertedPrime() {
		RestTemplate req = new RestTemplate();
		ResponseEntity<String> resp = req.getForEntity("http://localhost:"+ port + "/prime/last", String.class);
		assertEquals("5", resp.getBody());
	}

}
