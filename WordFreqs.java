import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A class that takes a text file as an input argument and tells you the number of distinct words in that file
 * @author samberling
 *
 */
public class WordFreqs {
	public static void main(String[] args){
		try{
		String delimiter = "[^a-zA-Z'_]+";
		HashTable<String, Integer> dictionary = new HashTable<String, Integer>();

		long start = System.currentTimeMillis();
		makeDictionaryFromFile(args, delimiter, dictionary);
		System.out.println(System.currentTimeMillis()-start + "ms");
		/*Output the total number of distinct words it found*/
		System.out.println("This text contains " + dictionary.size() + " distinct words.");

		/*Offer the user a prompt, to query the exact count of a particular word.*/
		userInteraction(dictionary);
		System.out.println("Goodbye!");
		}
		catch(Exception e){
			System.out.println("Something went wrong, but I'm not throwing any exceptions!");
		}
	}

	private static void makeDictionaryFromFile(String[] args, String delimiter, HashTable<String, Integer> dictionary) {
		try {
			String fileName = args[0];

			/* Count the frequencies of every word in the file */		
			fillDictionary(delimiter, dictionary, fileName);
		}
		catch (FileNotFoundException e) {
			System.err.println("file not found");
			System.exit(0);
		}
		catch(ArrayIndexOutOfBoundsException e){
			System.err.println("No command line argument");
			System.exit(0);
		}
	}

	private static void userInteraction(HashTable<String, Integer> dictionary) {
		System.out.println("Please enter a word to get its frequency, or hit enter to leave.");
		Scanner sc = new Scanner(System.in);
		while(true){
			if(sc.hasNextLine()){
				String word = sc.nextLine();
				word = word.toLowerCase();
				/*Continue until the user just hits enter, at which point it will terminate*/
				if(word.equals("")){
					break;
				}
				/*If the user enters a word perpended with a -, it will delete the word*/
				if(word.startsWith("-")){
					word = word.substring(1);
					if(dictionary.contains(word)){
						dictionary.delete(word);
						System.out.println("\""+word+"\" has been deleted.");
					}
					else{
						System.out.println("\""+word+"\" does not appear, so it was not deleted");
					}
				}
				else{
					if(dictionary.contains(word)){
						int count = dictionary.get(word);
						System.out.println("\""+word+"\" appears " + count + " times.");
					}
					else{
						System.out.println("\""+word+"\" does not appear.");
					}
				}
			}
		}
		sc.close();
	}

	private static void fillDictionary(String delimiter, HashTable<String, Integer> dictionary, String fileName) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(fileName));		
		sc.useDelimiter(delimiter);
//		int numWords = 0;
		while(sc.hasNext()){
//			numWords++;
			String word = sc.next();
			word = word.toLowerCase();
			if(word.startsWith("'")){
				word = word.substring(1);
			}
			if(word.endsWith("'")){
				word = word.substring(0, word.length()-1);
			}
			if(dictionary.contains(word)){
				int count = dictionary.get(word);
				dictionary.put(word, count+1);
			}
			else{
				dictionary.put(word, 1);
			}
		}
		sc.close();
//		System.out.println(numWords + " total words");
	}
}
