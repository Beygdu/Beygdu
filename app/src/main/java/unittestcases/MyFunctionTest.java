package unittestcases;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * @author Arnar Jonsson
 * @since 21.11.2014
 * @version 0.1
 *
 */
public class MyFunctionTest {
	
	OurFunctions func = new OurFunctions();
	
	private ArrayList<String> populateArrayList(String[] a) {
		ArrayList<String> returnList = new ArrayList<String>();
		
		for( int i = 0; i < a.length; i++ ) {
			returnList.add(a[i]);
		}
		
		return returnList;
	}
	
	private ArrayList<Boolean> populateArrayList(boolean[] a) {
		ArrayList<Boolean> returnList = new ArrayList<Boolean>();
		
		for( int i = 0; i < a.length; i++ ) {
			returnList.add(a[i]);
		}
		
		return returnList;
	}
	
	
	/**
	 *  Test for reverseList(ArrayList<String> X) in the class WordResult
	 */
	@Test
	public void testReverseList() {
		String[] res1 = { "1", "2", "3" };
		String[] res2 = { "3", "2", "1" };
		
		ArrayList<String> results = populateArrayList(res1);
		ArrayList<String> expectedResults = populateArrayList(res2);
		
		results = func.reverseList(results);
		
		
		assertEquals(expectedResults, results);
	}
	
	/**
	 * Tests for destroyPointer(String X) in the class WordResult
	 */
	@Test
	public void testDestroyPointer() {
		String result = func.destroyPointer("BadString");
		String expectedResult = "BadString";
		
		assertEquals(expectedResult, result);
		
		result = func.destroyPointer("FirstWord SecondWord");
		expectedResult = "SecondWord";
		
		assertEquals(expectedResult, result);
		
		result = func.destroyPointer(" SecondWord");
		expectedResult = "SecondWord";
		
		assertEquals(expectedResult, result);
		
		result = func.destroyPointer("%^**^% &*&%&*( ^**@%#");
		expectedResult = "&*&%&*( ^**@%#";
		
		assertEquals(expectedResult, result);
	}
	
	/**
	 * Tests for destroyDoublePointer(String X) in the class WordResult
	 */
	@Test
	public void testDestroyDoublePointer() {
		String result = func.destroyPointer("BadString");
		String expectedResult = "BadString";
		
		assertEquals(expectedResult, result);
		
		result = func.destroyDoublePointer("FirstWord SecondWord");
		expectedResult = "SecondWord";
		
		assertEquals(expectedResult, result);
		
		result = func.destroyDoublePointer(" SecondWord");
		expectedResult = "SecondWord";
		
		assertEquals(expectedResult, result);
		
		result = func.destroyDoublePointer("%^**^% &*&%&*( ^**@%#");
		expectedResult = "^**@%#";
		
		assertEquals(expectedResult, result);
	}
	
	/**
	 * Test for constructResultsFromRows(String X, Integer X) in the class WordResult
	 */
	@Test
	public void testConstructResultsFromRows() {
		String[] res = { "Afi Ifa", "afI Afi", "ííííí úúúúú kkkkk", "notsplit/ /hopefullynot", "a b",
				"shouldsplit fiftiffit /shouldnot", "%&//() /()#$ %#$&", "" };
		String[] expres = { "Afi", "Ifa", "afI", "Afi", "ííííí", "úúúúú", "kkkkk", 
				"notsplit/ /hopefullynot", "a", "b",
				"shouldsplit", "fiftiffit /shouldnot", "%&//() /()#$", "%#$&", "" };
		
		ArrayList<String> preDefResults = populateArrayList(res);
		ArrayList<String> expectedResults = populateArrayList(expres);
		
		ArrayList<String> results = new ArrayList<String>();
		
		ArrayList<String> temp;
		
		for( String s : preDefResults ) {
			temp = func.constructResultsFromRows(s, 1);
			
			for( String r : temp ) {
				results.add(r);
			}
		}
		
		assertEquals(expectedResults, results);
		
	}
	
	/**
	 * Test for constructSoTitle(String X) in the class WordResult
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testConstructSoTitle() {
		String[] expectedResults = { "", "pancakes" };
		String[] results = new String[2];
		
		results[0] = func.constructSoTitle("randomstring");
		results[1] = func.constructSoTitle("th pancakes");
		
		assertEquals(expectedResults, results);
	}
	
	/**
	 * Test for checkSoNeededTitle(String X) in the class WordResult
	 */
	@Test
	public void testCheckSoNeededTitle() {
		String[] expectedResults = { "", "randomString" };
		String[] results = new String[2];
		
		results[0] = func.checkSoNeededTitle("randomString" ,"Boðháttur");
		results[1] = func.checkSoNeededTitle("randomString", "randomString");
		
		assertEquals(expectedResults, results);
	}
	
	/**
	 * Test for isLegalNoRaw(String X) in the class WordResult
	 */
	@Test
	public void testIsLegalNoRaw() {
		String[] res = { "tr randomstring Eintala", "tr Fleirtala randomstring", "ha",
				"th randoms Eintala", "th Fleirtala randoms",
				"randomstring án greinis með greini notsorandomstring", "I Like Turtles" };
		boolean[] expres = { true, true, true, false, false, true, false };
		
		ArrayList<Boolean> expectedResults = populateArrayList(expres);
		
		ArrayList<Boolean> results = new ArrayList<Boolean>();
		
		for( int i = 0; i < res.length; i++ ) {
			results.add(func.isLegalRawNo(res[i]));
		}
		
		assertEquals(expectedResults, results);
	}
	
	/**
	 * Test for isIllegalLoRaw(String X) in the class WordResult
	 */
	@Test
	public void testIsIllegalLoRaw() {
		String[] res = { "tr kodo Eintala", "tr Fleirtala webspinner", "ha",
				"th randoms Eintala", "th Fleirtala randoms",
				"random Karlkyn I Like Kvenkyn no so much Hvorugkyn",
				"random legal words" };
		boolean[] expres = { true, true, false, false, false, true, false };
		
		ArrayList<Boolean> expectedResults = populateArrayList(expres);
		
		ArrayList<Boolean> results = new ArrayList<Boolean>();
		
		for( int i = 0; i < res.length; i++ ) {
			results.add(func.isIllegalLoRaw(res[i]));
		}
		
		assertEquals(expectedResults, results);		
	}
	
	/**
	 * Test for isIllegalSoRaw(String X) in the class WordResult
	 */
	@Test
	public void testIsIllegalSoRaw() {
		String[] res = { "tr kodo Eintala", "tr Fleirtala webspinner", "ha",
				"th randoms Germynd", "th Miðmynd randoms",
				"random Karlkyn I Like Kvenkyn no so much Hvorugkyn",
				"random legal words", "tr kodo Nútíð", "tr Þátíð webspinner",
				"randomeText Et. Ft. anotherRandomText", "perswithoutdot", 
				"now. perswithdot", "arandomlegalString" };
		boolean[] expres = { true, true, true, true, true, false, false, true, true, true, true, true, false };
		
		ArrayList<Boolean> expectedResults = populateArrayList(expres);
		
		ArrayList<Boolean> results = new ArrayList<Boolean>();
		
		for( int i = 0; i < res.length; i++ ) {
			results.add(func.isIllegalSoRaw(res[i]));
		}
		
		assertEquals(expectedResults, results);			
	}

}
