import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//**MAKE SURE YOU JAVADOC SO YOU DON'T LOSE POINTS!!!
public class HashListAutocomplete implements Autocompletor 
{
	private static final int MAX_PREFIX = 10;
	private Map<String, List<Term>> myMap;
	private int mySize;

	/**
	 * Throws nullpointerexception if either the terms or weights are null.
	 * @param terms
	 * @param weights
	 */
	public HashListAutocomplete(String[] terms, double[] weights) 
	{
		if (terms == null || weights == null)
			throw new NullPointerException("One or more arguments null");
		initialize(terms, weights);
	}
	
	/**
	 * Implements the Autocompletor interface, quicker implementation of 
	 * topMatches by utilizing a hashlist. Maintains a hashlist containing
	 * every possible prefix up to 10 characters, the key being the prefix and 
	 * the value being a weight sorted list of Term objects with that prefix.
	 * Returns the k words in myTerms with the largest weight which match the given prefix,
	 * in descending weight order. If less than k words exist matching the given
	 * prefix (including if no words exist), then the array instead contains all
	 * those words.
	 */
	@Override
	public List<Term> topMatches(String prefix, int k) 
	{
		// TODO Auto-generated method stub
		if (prefix == null)
			throw new NullPointerException("Null pointer");
		if (!(myMap.containsKey(prefix)))
			return new ArrayList<Term>();
		if (k == 0)
			return new ArrayList<Term>();
		if (k < 0)
			throw new IllegalArgumentException("Illegal value of k:" + k);
		List<Term> a = myMap.get(prefix);
		List<Term> b = a.subList(0, Math.min(k, a.size()));
		return b;
	}
	
	/**
	 * Creates internal state needed to store Term objects
	 * from the parameters. 
	 */
	@Override
	public void initialize(String[] terms, double[] weights) 
	{
		myMap = new HashMap<String, List<Term>>();
		for (int i = 0; i < terms.length; i+= 1) 
		{
			String a = terms[i];
			for (int j = 0; j <= Math.min(MAX_PREFIX, a.length()); j += 1) 
			{
				if (a.length() >= j) {
					String b = a.substring(0, j);
					Term c = new Term(terms[i], weights[i]);
					myMap.putIfAbsent(b, new ArrayList<Term>());
					myMap.get(b).add(c);
				}
			}
		}
		Comparator<Term> a = new Term.ReverseWeightOrder();
		for (String key : myMap.keySet()) {
			Collections.sort(myMap.get(key), a);
		}
	}
	
	/**
	 * Returns size in bytes of all Strings and doubles
	 * stored in implementing class.
	 */
	@Override
	public int sizeInBytes() 
	{
		// TODO Auto-generated method stub	
		if (mySize == 0) 
		{
			for (String key : myMap.keySet()) 
			{
				mySize += key.length() * BYTES_PER_CHAR;
				List<Term> t = myMap.get(key);
				for (int i = 0; i < t.size(); i++) 
				{
					Term a = t.get(i);
					mySize = mySize + BYTES_PER_DOUBLE + BYTES_PER_CHAR*a.getWord().length();
				}
			}
		}
		return mySize;
	}
}