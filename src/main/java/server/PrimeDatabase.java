package server;

import java.math.BigInteger;

import org.springframework.stereotype.Component;

@Component
public interface PrimeDatabase {
	
	public void insert(BigInteger n);
	
	public BigInteger getNext(BigInteger n);
	
	public BigInteger getLast();
	
	public BigInteger [] getAll();

}
