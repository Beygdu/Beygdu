package unittestcases;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Arnar Jonsson
 * @since 21.11.2014
 * @version 0.1
 *
 */
public class OurFunctions {

	/**
	 * Emtpy Constructor. The Object holds public version of the private functions which where selected
	 * for unit testing
	 */
	public OurFunctions() {
		//Do nothing
	}
	
	/**
	 * @param a String
	 * @return If "a" contains a space then everything before the first space is removed from "a",
	 * else it returns the original string
	 */
	public String destroyPointer(String a) {

		return a.substring(a.indexOf(" ")+1, a.length());

	}
	
	/**
	 * @param a String
	 * @return If "a" contains one or more spaces, everything before the second space is removed from "a",
	 * else it returns the original string
	 */
	public String destroyDoublePointer(String a) {
		return destroyPointer(destroyPointer(a));
	}
	
	/**
	 * @param a ArrayList<String>
	 * @return The ArrayList in reverse order
	 */
	public ArrayList<String> reverseList(ArrayList<String> a) {
		ArrayList<String> backwardsList = new ArrayList<String>();
		
		for( int i = a.size()-1; i > -1; i-- ) {
			backwardsList.add(a.get(i));
		}
		
		return backwardsList;
		
	}
	
	/**
	 * @param a String
	 * @param starts ArrayList<Integer>
	 * @return An ArrayList of strings which consists
	 */
	public ArrayList<String> constructArguments(String a, ArrayList<Integer> starts) {
		ArrayList<String> returnList = new ArrayList<String>();
		
		for( int i = starts.size()-1; i > -1; i-- ) {
			returnList.add(a.substring(starts.get(i)+2, a.length()));
			a = a.substring(0, starts.get(i)+1);
		}
		returnList.add(a);
		
		return reverseList(returnList);
	}
	
	/**
	 * @param a String
	 * @param caseId int
	 * @return An ArrayList consisting of the string "a" or its substring, if it contained a space (\\s)
	 */
	public ArrayList<String> constructResultsFromRows(String a, int caseId) {
		
		ArrayList<String> returnList = new ArrayList<String>();
		
		if( caseId == 1 ) {
			Pattern pattern = Pattern.compile("[^/]\\s[^/]");
			Matcher matcher = pattern.matcher(a);
			
			ArrayList<Integer> starts = new ArrayList<Integer>();
			
			while( matcher.find() ) {
				starts.add(matcher.start());
			}
			
			returnList = constructArguments(a, starts);
		}
		
		return returnList;
	}
	
	/**
	 * @param a String
	 * @return If "a" contains the substring "th", returns the empty string, else returns "a"
	 */
	public String constructSoTitle(String a) {
		String returnString = "";
		if(a.contains("th")) {
			returnString = destroyPointer(a);
		}
		return returnString;
	}
	
	/**
	 * @param a String
	 * @param bTitle String
	 * @return The empty string if "bTitle" contains illegal substrings of verb titles, else "a"
	 */
	public String checkSoNeededTitle(String a, String bTitle) {
		
		if( bTitle.contains("Boðháttur") || bTitle.contains("Sagnbót") || bTitle.contains("nútíðar") ) {
			return "";
		}
		
		return a;
	}

	/**
	 * @param a String
	 * @return True if "a" contains illegal results for nouns, false otherwise
	 */
	public boolean isLegalRawNo(String a) {
		return (a.contains("tr") && (a.contains("Eintala") || a.contains("Fleirtala"))) ||
				(a.length() <= 3) || (a.contains("án greinis með greini"));
	}
	
	/**
	 * @param a String
	 * @return True if "a" contains illegal results for adjectives, false otherwise
	 */
	public boolean isIllegalLoRaw(String a) {
		return (a.contains("tr") && (a.contains("Eintala") || a.contains("Fleirtala"))) ||
				(a.contains("Karlkyn") && a.contains("Kvenkyn") && a.contains("Hvorugkyn"));
	}
	
	/**
	 * @param a String
	 * @return True if "a" contains illegal results for verbs, false otherwise
	 */
	public boolean isIllegalSoRaw(String a) {
		return (a.contains("tr") && (a.contains("Nútíð") || a.contains("Þátíð"))) ||
				a.contains("Et. Ft.") || a.contains(". pers") || (a.length() <= 3) ||
				(a.contains("th") && (a.contains("Germynd") || a.contains("Miðmynd"))) ||
				(a.contains("tr") && (a.contains("Eintala") || a.contains("Fleirtala"))) ||
				a.contains("Karlkyn Kvenkyn Hvorugkyn") || a.contains("pers");
	}
	
	
	
}