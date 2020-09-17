package main;

import java.util.ArrayList;

import main.Utils.Struct2;

public class AnalysisUtils {
	private AnalysisUtils() {
		throw new AssertionError("This class should not be instantiable");
	}
	
	/**
	 * A container for a <code>String</code> and an <code>int</code> - <code>sequence</code> and <code>spacing</code> respectively.<br>
	 * <code>sequence</code> sequence of characters, or piece of text, that was repeated.
	 * <code>spacing</code> is the distance (in num chars) between the two occurances.
	 * @see <a href="https://en.wikipedia.org/wiki/Kasiski_examination">https://en.wikipedia.org/wiki/Kasiski_examination</a>
	 */
	public static class RepeatedSequence {
		public String sequence;
		public int spacing;
		
		public RepeatedSequence(String seq, int spacing) {
			sequence = seq;
			this.spacing = spacing;
		}
	}
	
	/**
	 * A container for an <code>int</code> and a <code>float</code> - <code>keylength</code> and <code>ioc</code> respectively.<br>
	 * <code>keylength</code> is the keyword length that was tested.<br>
	 * <code>ioc</code> is the average index of coincidence for each column (a column here being if you were to split the text into the same number of columns as letters in the keyword, and write the text into it in rows)
	 * @see <a href="https://en.wikipedia.org/wiki/Vigen%C3%A8re_cipher#Friedman_test">https://en.wikipedia.org/wiki/Vigenère_cipher#Friedman_test</a>
	 */
	public static class IOCForKeyLen {
		public int keylength;
		public float ioc;
		
		public IOCForKeyLen(int keylen, float ioc) {
			keylength = keylen;
			this.ioc = ioc;
		}
	}
	
	/**
	 * Searches the input text and finds repeated sections of the text. It bundles each repeat into an ArrayList that is returned as a result.<br><br>
	 * In the ArrayList, there is a separate entry for each repetition - so if, for example, "ABC" was repeated 3 times in the text, the returned ArrayList would have 3 entries describing the repetition between the 1st & 2nd, the 1st & 3rd, and the 2nd & 3rd
	 * @param text - The ciphertext to be examined. This should be stripped of all whitespaces/non-letter-characters such as full stops, commas, tabs and spaces
	 * @return An ArrayList of RepeatedSequences (basically just a container for a String and an int, which are the repeated piece of text and the distance between the occurances respectively)
	 * @see RepeatedSequence
	 * @see <a href="https://en.wikipedia.org/wiki/Kasiski_examination">https://en.wikipedia.org/wiki/Kasiski_examination</a>
	 */
	public static ArrayList<RepeatedSequence> KasiskiTest(String text) {
		ArrayList<RepeatedSequence> sequences = new ArrayList<RepeatedSequence>();
		for(int i = 0; i < text.length() - 1; i++) {
			ArrayList<Struct2<String, Integer[]>> list = Utils.findAllOccurrences(text, i);
			for(Struct2<String, Integer[]> entry : list) {
				if(entry.getObj1().length() <= 1) {
					continue; // We don't want to test for single letters. That will just dilute the actually useful data
				}
				final Integer[] positions = entry.getObj2();
				for(int j = 0; j < positions.length; j++) { // Calculate the spacing between every possible pairing
					for(int k = j + 1; k < positions.length; k++) {
						if(j != k) {
							int prev = positions[j];
							int curr = positions[k];
							sequences.add(new RepeatedSequence(entry.getObj1(), curr - prev));
						}
					}
				}
			}
		}
		return sequences;
	}
	
	/**
	 * Uses the probability Kp that any two randomly chosen source language letters from the are the same (around 0.067 for English) and the probability of a coincidence for a uniform random selection from the alphabet Kr (1/26 = 0.0385 for English) to estimate the key length, using equation:<br>
	 * KeywordLength = (Kp - Kr) / (Ko - Kr)<br>
	 * Where:<br>
	 * Ko = SUM(i = 0 to c, ni(ni - 1)) / N(N - 1)
	 * @param text - The ciphertext to be examined. This should be stripped of all whitespaces/non-letter-characters such as full stops, commas, tabs and spaces
	 * @return The most likely keyword length, as calculated by use of the above equation
	 * @see <a href="https://en.wikipedia.org/wiki/Vigen%C3%A8re_cipher#Friedman_test">https://en.wikipedia.org/wiki/Vigenère_cipher#Friedman_test</a>
	 */
	// The only reason I'm not calling is FriedmanTest is cause then I'll have all my methods in this class starting with K which is nice
	public static int KappaTest(String text) {
		return 0; // TODO: Implement Kappa Test
	}
	
	/**
	 * Estimates the length of the keyword by calculating the average index of coincidence for each keyword length, IOC closest to the average IOC for plain English is the most likely, but this method returns all of them.<br><br>
	 * Method: Copy the ciphertext into rows of a matrix with as many columns as an assumed key length and then to calculate the average index of coincidence with each column considered separately.
	 * @param text - The ciphertext to be examined. This should be stripped of all whitespaces/non-letter-characters such as full stops, commas, tabs and spaces
	 * @return An ArrayList of IOCForKeyLens (Basically just a container for an int and a float, which are the keyword length and IOC respectively)
	 * @see IOCForKeyLen
	 * @see <a href="https://en.wikipedia.org/wiki/Kasiski_examination">https://en.wikipedia.org/wiki/Kasiski_examination</a>
	 * @see <a href="https://en.wikipedia.org/wiki/Index_of_coincidence">https://en.wikipedia.org/wiki/Index_of_coincidence</a>
	 */
	public static ArrayList<IOCForKeyLen> KappaTest2(String text) {
		return null;
	}
}