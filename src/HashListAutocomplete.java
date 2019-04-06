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

	public HashListAutocomplete(String[] terms, double[] weights) 
	{
		if (terms == null || weights == null)
			throw new NullPointerException("One or more arguments null");
		initialize(terms, weights);
	}
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