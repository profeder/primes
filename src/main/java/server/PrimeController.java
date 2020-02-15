/**
 * 
 */
package server;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Federico
 *
 */

@RestController
public class PrimeController {
	
	@Autowired
	private PrimeDatabase db;
	
	@PutMapping(path = "/prime/{number}")
	public ResponseEntity <String> putPrime(@PathVariable(name = "number", required = true)String number) {
		BigInteger n = new BigInteger(number,16);
		// salva il numero primo
		db.insert(n);
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	@GetMapping(path="/prime/{number}/next")
	public ResponseEntity<String> nextPrime(@PathVariable(name = "number", required = true)String number){
		BigInteger n = new BigInteger(number, 16); 
		n = db.getNext(n);
		if(n == null) {
			n = new BigInteger("-1");
		}
		return new ResponseEntity<String>(n.toString(16), HttpStatus.OK);
	}
	
	@GetMapping(path = "/prime/last")
	public ResponseEntity<String> lastPrime(){
		BigInteger n = db.getLast();
		return new ResponseEntity<String>(n.toString(16), HttpStatus.OK);
	}

}
