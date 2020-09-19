package main;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class Utils {
	private Utils() {
		throw new AssertionError("This class should not be instantiable");
	}
	
	/**
	 * A class holding 2 immutable objects of specified types
	 * @param <T1> - Type of object 1
	 * @param <T2> - Type of object 2
	 */
	public static class Struct2<T1, T2> {
		private final T1 ob1;
		private final T2 ob2;
		
		public Struct2(T1 param1, T2 param2) {
			ob1 = param1;
			ob2 = param2;
		}
		
		public T1 getObj1() {
			return ob1;
		}
		
		public T2 getObj2() {
			return ob2;
		}
	}
	
	public static class CharHolder {
		public final char ch;
		public final boolean hasChar;
		public final boolean isCaps;
		
		public CharHolder(char ch) {
			this.ch = Character.toLowerCase(ch);
			this.isCaps = Character.isUpperCase(ch);
			hasChar = true;
		}
		
		public CharHolder(boolean isCaps) {
			ch = 0;
			this.isCaps= isCaps;
			hasChar = false;
		}
		
		public char getChar() {
			assert hasChar;
			if(isCaps) {
				return Character.toUpperCase(ch);
			} else {
				return ch;
			}
		}
	}
	
	/**
	 * This method will find and return all occurrences of repeated sequences of characters, taking characters starting at the index specified by <code>startingWith</code><br>
	 * <br>
	 * This method will look at the character at <code>startingWith</code> and look for all other occurrences of it.
	 * Then, it'll look at the next letter (the one at <code>startingWith + 1</code>) and then look at all of the previous occurrences and see if they're followed by the same character.
	 * If a sequence of characters is not followed by the next letter, then it's added to the array to be returned, along with all sequences that also match the letters it contains.
	 * And it'll repeat until there is only one sequence of characters left.
	 * <br>
	 * <br>
	 * Seems to work perfectly but there's almost definitely a case where it doesn't
	 * @param str - The string to search
	 * @param startingWith - The character to start the search at
	 * @return An ArrayList of sequences at positions
	 */
	public static ArrayList<Struct2<String, Integer[]>> findAllOccurrences(String str, int startingWith) { // This is going to be a trifle more complicated than I initially thought
		ArrayList<Struct2<String, Integer[]>> list = new ArrayList<Struct2<String, Integer[]>>();
		char lookingFor = str.charAt(startingWith);
		StringBuilder sequence = new StringBuilder();
		sequence.append(lookingFor);
		ArrayList<Integer> occurrenceIndexes = new ArrayList<Integer>(); // Includes the starting index
		
		// Find all occurrences of the first letter
		for(int i = 0; i < str.length(); i++) {
			if(str.charAt(i) == lookingFor) {
				occurrenceIndexes.add(i);
			}
		}
		
		for(int i = startingWith + 1; i < str.length(); i++) { // Loop from startingWith to the end of the string
			lookingFor = str.charAt(i);
			int fromStart = i - startingWith; // fromStart is the current index in the sequence - add this onto the index of the start to get the current character being examined. Start at 1
			ArrayList<Integer> stoppedIndexes = new ArrayList<Integer>();
			for(int j = 0; j < occurrenceIndexes.size(); j++) { // And for every iteration through occurrenceIndexes, check the next character from each occurrence
				int index = occurrenceIndexes.get(j).intValue() + fromStart;
				boolean failed = true;
				if(index < str.length()) {
					if(str.charAt(index) == lookingFor) { // If this occurance's next character is the character we're looking for
						failed = false;
					}
				}
				
				if(failed) {
					stoppedIndexes.add(occurrenceIndexes.get(j).intValue()); // Change it to int so it gets copied
				}
			}
			
			if(stoppedIndexes.size() != 0) {
				appendSequences(list, occurrenceIndexes, stoppedIndexes, sequence);
				stoppedIndexes.clear();
				if(occurrenceIndexes.size() == 0) {
					break;
				}
			}
			
			if(occurrenceIndexes.size() != 0) {
				sequence.append(lookingFor);
//				System.out.println("Length: " + occurrenceIndexes.size());
			}
		}
		
		return list;
	}
	
	/**
	 * This method adds an entry to <code>target</code> of all the sequences in occurrenceIndexes, which is the ArrayList that contains all the indexes of the occurrences of the sequence, while stoppedIndexes is all the indexes where the sequence has broken.
	 * All indexes in stoppedIndexes will be removed from occurrenceIndexes.
	 * @param target
	 * @param occurrenceIndexes
	 * @param builder
	 */
	private static void appendSequences(ArrayList<Struct2<String, Integer[]>> target, ArrayList<Integer> occurrenceIndexes, ArrayList<Integer> stoppedIndexes, StringBuilder sequence) {
		if(target != null && occurrenceIndexes != null) {
			Integer[] indexArr = new Integer[occurrenceIndexes.size()];
			indexArr = occurrenceIndexes.toArray(indexArr);
			String sequ = sequence.toString();
			target.add(new Struct2<String, Integer[]>(sequ, indexArr));
			if(stoppedIndexes != null) {
				occurrenceIndexes.removeAll(stoppedIndexes);
			}
		}
	}
	
	/**
	 * This method concatenates two arrays
	 * @see <a href="https://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java">https://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java</a>
	 * @param <T>
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T> T[] concat(T[] first, T[] second) {
		if(first == null) return second;
		if(second == null) return first;
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
	
	/**
	 * Returns a String array representing the numbers counting from start (inclusive) to end (exclusive)
	 * @param start
	 * @param end
	 * @return
	 */
	public static String[] count(int start, int end) {
		if(start >= end) {
			return null;
		} else {
			String[] strArr = new String[end - start];
			int currentVal = start;
			for(int i = 0; i < end - start; i++) {
				strArr[i] = String.valueOf(currentVal);
				currentVal++;
			}
			return strArr;
		}
	}
	
	public static int count(char ch, String str) {
		int count = 0;
		for(int i = 0; i < str.length(); i++) {
			if(str.charAt(i) == ch) {
				count++;
			}
		}
		return count;
	}
	
	public static JPanel makeTable(Object[][] data, String[] columnNames) {
		return makeTable(data, columnNames, null);
	}
	
	public static JPanel makeTable(Object[][] data, String[] columnNames, int[] columnWidths) {
		JPanel container = new JPanel();
		
		JTable table = new JTable(new NonEditableTableModel(data, columnNames));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		
		if(columnWidths != null) {
			TableColumn column = null;
			for (int i = 0; i < Math.min(columnNames.length, columnWidths.length); i++) {
				column = table.getColumnModel().getColumn(i);
			    column.setPreferredWidth(columnWidths[i]);
			}
		}
		
		return container;
	}
	
	public int[] repeat(int num, int count) {
		int[] arr = new int[count];
		for(int i = 0; i < arr.length; i++) {
			arr[i] = num;
		}
		return arr;
	}
}