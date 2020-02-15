/**
 * 
 */
package client;

import java.math.BigInteger;
import java.util.TreeSet;

/**
 * @author Federico
 *
 */
public class PrimeRemoverTask implements Runnable {
	
	private BigInteger current;
	private TreeSet<BigInteger> tree;
	
	public PrimeRemoverTask(BigInteger current, TreeSet<BigInteger> tree) {
		this.current = current;
		this.tree = tree;
	}

	@Override
	public void run() {
		long i = 2;
		BigInteger mul = current.multiply(new BigInteger(""+i));
		while (mul.compareTo(tree.last()) <= 0) {
			tree.remove(mul);
			i++;
			mul = current.multiply(new BigInteger(""+i));
		}
	}

}
