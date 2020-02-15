/**
 * 
 */
package client;

import java.math.BigInteger;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


/**
 * @author Federico
 *
 */

@SpringBootApplication
@EnableAutoConfiguration
public class PrimeClient implements CommandLineRunner{
	
	public static final int MAX_THREAD_NO = 100;
	public static final int RANGE = 1000;
	public static final String proto = "http";
	public static final String server = "localhost";
	public static final int port = 8081;
	public static final String SERVER_URL = proto + "://" + server + ":" + port + "/prime/";
	
	private Logger log;
	
	public static void main(String [] args) {
		SpringApplication app = new SpringApplication(PrimeClient.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		log = Logger.getAnonymousLogger();
		RestTemplate cln = new RestTemplate();
		ResponseEntity<String> resp = cln.getForEntity(SERVER_URL+"last", String.class);
		BigInteger last = new BigInteger(resp.getBody(), 16);
		BigInteger current = new BigInteger("3");
		log.log(Level.INFO, "Last prime find: " + last);
		TreeSet<BigInteger> tree = new TreeSet<BigInteger>();
		// Init search tree
		log.log(Level.INFO, "Generating numbers table from " + last + " to "+ last.add(new BigInteger("" + ((2*RANGE)+1))));
		for(int i = 1; i < RANGE; i++) {
			BigInteger tmp = new BigInteger(""+((2*i)));
			tree.add(last.add(tmp));
		}
		log.log(Level.INFO, "Table generated");
		log.log(Level.INFO, "Looking for prev found numbers");
		// Remove just find numbers
		ExecutorService pool = Executors.newCachedThreadPool();
		while (current.compareTo(new BigInteger("-1")) != 0) {
			resp = cln.getForEntity(SERVER_URL+current.toString(16)+"/next", String.class);
			current = new BigInteger(resp.getBody(), 16);
			pool.submit(new PrimeRemoverTask(current, tree));
		}
		log.log(Level.INFO, "Waiting for pool shutdown");
		pool.shutdown();
		try {
			current = tree.first();
		}catch (NoSuchElementException e) {
			log.log(Level.INFO, "No primes found");
			return;
		}
		log.log(Level.INFO, "Remainig elements: " + tree.size());
		while (current.compareTo(tree.last()) <= 0) {
			long i = 2;
			BigInteger mul = current.multiply(new BigInteger(""+i));
			while (mul.compareTo(tree.last()) <= 0) {
				tree.remove(mul);
				i++;
				mul = current.multiply(new BigInteger(""+i));
			}
			current = tree.higher(current);
			if(current == null)
				break;
			log.log(Level.INFO, "Add number: " + current);
			cln.put(SERVER_URL + current.toString(16), "");
		}
		System.exit(0);
	}
}
