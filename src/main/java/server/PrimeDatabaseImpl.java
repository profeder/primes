/**
 * 
 */
package server;

import java.math.BigInteger;
import java.util.TreeSet;

import org.springframework.stereotype.Component;

/**
 * @author Federico
 *
 */
@Component
public class PrimeDatabaseImpl implements PrimeDatabase {
	
	private static TreeSet<BigInteger> db;
	
	private static void init() {
		db = new TreeSet<BigInteger>();
		db.add(new BigInteger("3"));
	}

	@Override
	public void insert(BigInteger n) {
		if(db == null) {
			init();
		}
		db.add(n);
	}

	@Override
	public BigInteger getNext(BigInteger n) {
		if(db == null) {
			init();
		}
		return db.higher(n);
	}

	@Override
	public BigInteger [] getAll() {
		if(db == null) {
			init();
		}
		return (BigInteger[])db.toArray();
	}
	
	@Override
	public BigInteger getLast() {
		if(db == null) {
			init();
		}
		return db.last();
	}

}
