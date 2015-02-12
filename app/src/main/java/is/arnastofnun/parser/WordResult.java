package is.arnastofnun.parser;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Arnar Jonsson
 * @since 20.11.2014
 * @version 0.4
 */
public class WordResult implements Serializable {

	private String type;
	private String title;
	private String note;
	
	private ArrayList<Block> blocks = new ArrayList<Block>();
	
	private ArrayList<ArrayList<String>> dump;
	
	/**
	 * @param type The type of the result
	 * @param title The title of the result
	 * @param note A note describing specific results
	 * @param dump A crude list of lists containing raw results
	 */
	public WordResult(String type, String title, String note, ArrayList<ArrayList<String>> dump) {
		
		this.type = type;
		this.title = title;
		this.note = note;
		this.dump = dump;
		
		populateBlockList();
	}

	/**
	 * @return The type of the results
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @return The Title of the result
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @return The note describing specific results
	 */
	public String getNote() {
		return this.note;
	}
	
	/**
	 * @return A list of Blocks which contain refined results
	 */
	public ArrayList<Block> getBlocks() {
		return this.blocks;
	}
	
	
	private String destroyPointer(String a) {
		return a.substring(a.indexOf(" ")+1, a.length());
	}
	
	private String destroyDoublePointer(String a) {
		return destroyPointer(destroyPointer(a));
	}
	
	private ArrayList<String> reverseList(ArrayList<String> a) {
		ArrayList<String> backwardsList = new ArrayList<String>();
		
		for( int i = a.size()-1; i > -1; i-- ) {
			backwardsList.add(a.get(i));
		}
		
		return backwardsList;
		
	}
	
	private ArrayList<String> constructArguments(String a, ArrayList<Integer> starts) {
		ArrayList<String> returnList = new ArrayList<String>();
		
		for( int i = starts.size()-1; i > -1; i-- ) {
			returnList.add(a.substring(starts.get(i)+2, a.length()));
			a = a.substring(0, starts.get(i)+1);
		}
		returnList.add(a);
		
		return reverseList(returnList);
	}
	
	private ArrayList<String> constructResultsFromRows(String a, int caseId) {
		
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
	
	private boolean isIllegalSoRaw(String a) {
		return (a.contains("tr") && (a.contains("Nútíð") || a.contains("Þátíð"))) ||
				a.contains("Et. Ft.") || a.contains(". pers") || (a.length() <= 3) ||
				(a.contains("th") && (a.contains("Germynd") || a.contains("Miðmynd"))) ||
				(a.contains("tr") && (a.contains("Eintala") || a.contains("Fleirtala"))) ||
				a.contains("Karlkyn Kvenkyn Hvorugkyn") || a.contains("pers");
	}
	
	private String constructSoTitle(String a) {
		String returnString = "";
		if(a.contains("th")) {
			returnString = destroyPointer(a);
		}
		return returnString;
	}
	
	/**
	 * @category Construction of column titles for the verb Tables
	 */
	private String[] constructSoColumnNames(String a, String b) {
		String[] errorArray = { "" };
		
		if( a.contains("Persónuleg notkun - Germynd") || a.contains("Persónuleg notkun - Miðmynd") || 
				a.contains("Ópersónuleg notkun - Germynd") ||
				a.contains("Ópersónuleg notkun - Miðmynd") ) {
			if( b.contains("Nafnháttur") ) {
				return errorArray;
			}
			else {
				String[] temp = { "", "Et.", "Ft." };
				return temp;
			}
		}
		else if( a.contains("Boðháttur") || a.contains("Sagnbót") ) {
			String[] temp = { "", "Germynd", "Miðmynd" };
			return temp;
		}
		else if( a.contains("Lýsingarháttur nútíðar") ) {
			return errorArray;
		}
		else if( a.contains("Lýsingarháttur þátíðar") ) {
			String[] temp = { "", "Karlkyn", "Kvenkyn", "Hvorugkyn" };
			return temp;
		}
		return errorArray;
	}
	
	/**
	 * @category Construction of row titles for the verb Tables
	 */
	private String[] constructSoRowNames(String a, String b) {
		String[] errorArray = { "" };
		
		if( a.contains("(Gervifrumlag)") ) {
			String[] temp = { "", "3. pers." };
			return temp;			
		}
		else if( a.contains("Persónuleg notkun - Germynd") || a.contains("Persónuleg notkun - Miðmynd") || 
				(a.contains("Ópersónuleg notkun - Germynd") && !a.contains("(Gervifrumlag)")) ||
				a.contains("Ópersónuleg notkun - Miðmynd") ) {
			if( b.contains("Nafnháttur") ) {
				return errorArray;
			}
			else {
				String[] temp = { "", "1. pers.", "2. pers.", "3. pers." };
				return temp;
			}
		}
		else if( a.contains("Sagnbót") ) {
			return errorArray;
		}
		else if( a.contains("Boðháttur")  ) {
			String[] temp = { "", "Stýfður", "Et.", "Ft." };
			return temp;
		}
		else if( a.contains("Lýsingarháttur nútíðar") ) {
			return errorArray;
		}
		else if( a.contains("Lýsingarháttur þátíðar") ) {
			String[] temp = { "", "Nf.", "Þf.", "Þgf.", "Ef." };
			return temp;
		}
		
		return errorArray;
	}
	
	private String checkSoNeededTitle(String a, String bTitle) {
		
		if( bTitle.contains("Boðháttur") || bTitle.contains("Sagnbót") || bTitle.contains("nútíðar") ) {
			return "";
		}
		
		return a;
	}
	
	/**
	 * @category Construction of Tables for verbs
	 */
	private Tables constructSoTables(ArrayList<String> a, String bTitle, String sbTitle) {
		
		String nTitle = checkSoNeededTitle(constructSoTitle(a.get(0)), bTitle);
		
		ArrayList<String> content = new ArrayList<String>();
		
		String[] columnNames = constructSoColumnNames(bTitle, sbTitle);
		String[] rowNames = constructSoRowNames(bTitle, sbTitle);
		
		
		
		ArrayList<String> temp;
		
		for( String s : a ) {
			if( s.contains("th") || s.contains("Germynd Miðmynd") ) {
				// Do nothing
			}
			else if( s.contains(".") ) {
				temp = constructResultsFromRows(destroyDoublePointer(s), 1);
				
				for( String t : temp ) {
					content.add(t);
				}
			}
			else if( s.contains("Stýfður") ) {
				content.add(destroyDoublePointer(s));
				content.add("--");
			}
			else {
				content.add(destroyPointer(s));
			}
		}
		
		return new Tables(nTitle, columnNames, rowNames, content);
	}
	
	/**
	 * @category Constructions of SubBlocks for verbs
	 */
	private SubBlock constructSoSubBlock(ArrayList<String> a, String bTitle) {
		
		String nTitle = checkSoNeededTitle(destroyPointer(a.get(0)), bTitle);
		
		ArrayList<Tables> tables = new ArrayList<Tables>();
		
		ArrayList<ArrayList<String>> rawTables = new ArrayList<ArrayList<String>>();
		ArrayList<String> temp = new ArrayList<String>();
		
		int count = 0;
		for( String s : a ) {
			
			if( s.contains("h4") ) {
				// Do nothing
			}
			else if( (s.contains("Germynd Miðmynd") || s.contains("th")) && count > 0 ) {
				rawTables.add(temp);
				temp = new ArrayList<String>();
				count = 0;
				
				temp.add(s);
				count++;
			}
			else {
				temp.add(s);
				count++;
			}
			
		}
		rawTables.add(temp);
		
		for( ArrayList<String> s : rawTables ) {
			
			Tables nT = constructSoTables(s, bTitle, nTitle);
			tables.add(nT);
			
		}
		
		return new SubBlock(nTitle, tables);
	}
	
	/**
	 * @param a ArrayList of raw results
	 * @category Constructions of Blocks for verbs
	 */
	private void constructSoBlock(ArrayList<String> a) {
		
		String nTitle = destroyPointer(a.get(0));
		ArrayList<SubBlock> sB = new ArrayList<SubBlock>();
		
		ArrayList<ArrayList<String>> rawSubBlock = new ArrayList<ArrayList<String>>();
		ArrayList<String> temp = new ArrayList<String>();
		
		int count = 0;
		for( String s : a ) {
			
			if( !isIllegalSoRaw(s) ) {
				if( s.contains("h3") ) {
					// Do nothing
				}
				else if( s.contains("h4") && count >0 ) {
					rawSubBlock.add(temp);
					temp = new ArrayList<String>();
					count = 0;
					
					temp.add(s);
					count++;
				}
				else {
					temp.add(s);
					count++;
				}
			}
		}
		rawSubBlock.add(temp);
		
		for( ArrayList<String> s : rawSubBlock ) {
			
			SubBlock nSB = constructSoSubBlock(s, nTitle);
			sB.add(nSB);
		}
		
		this.blocks.add(new Block(nTitle, sB));
		
	}
	
	/**
	 * @category Construction of Blocks for verbs
	 */
	private void populateSoBlocks() {
		for( ArrayList<String> aList : dump ) {
			
			constructSoBlock(aList);
			
		}
	}
	
	
	private String constructLoTitle(String a) {
		String returnString = "";
		if( !this.title.contains("Töluorð") ) {
			return destroyPointer(a);
		}		
		return returnString;
	}
	
	private boolean isIllegalLoRaw(String a) {
		return (a.contains("tr") && (a.contains("Eintala") || a.contains("Fleirtala"))) ||
				(a.contains("Karlkyn") && a.contains("Kvenkyn") && a.contains("Hvorugkyn"));
	}
	
	/**
	 * @param a ArrayList of raw results
	 * @category Construction of Tables for adjectives
	 */
	private Tables constructLoTables(ArrayList<String> a) {
		
		String nTitle = destroyPointer(a.get(0));
		ArrayList<String> content = new ArrayList<String>();
		String[] columnNames = { "", "Karlkyn", "Kvennkyn", "HvorugKyn" };
		String[] rowNames = { "", "Nf.", "Þf.", "Þgf.", "Ef." };
		
		ArrayList<String> temp = new ArrayList<String>();
		
		for( String s : a ) {
			if( s.contains(".") ) {
				temp = constructResultsFromRows(destroyDoublePointer(s), 1);
				
				for( String t : temp ) {
					content.add(t);
				}
			}			
		}
		
		return new Tables(nTitle, columnNames, rowNames, content);
	}
	
	/**
	 * @param a ArrayList of raw results
	 * @category Construction of SubBlocks for adjective
	 */
	private SubBlock constructLoSubBlock(ArrayList<String> a) {
		
		String nTitle = constructLoTitle(a.get(0));
		ArrayList<Tables> tables = new ArrayList<Tables>();
		
		ArrayList<ArrayList<String>> rawTables = new ArrayList<ArrayList<String>>();
		ArrayList<String> temp = new ArrayList<String>();
		
		int count = 0;
		for( String s : a ) {
			if( s.contains("h4") ) {
				// Do nothing
			}
			else if( s.contains("th") && count >0 ) {
				rawTables.add(temp);
				temp = new ArrayList<String>();
				count = 0;
				
				temp.add(s);
				count++;
			}
			else {
				temp.add(s);
				count++;
			}
		}
		rawTables.add(temp);
		
		for( ArrayList<String> s : rawTables ) {
			
			Tables nT = constructLoTables(s);
			tables.add(nT);
			
		}
		
		return new SubBlock(nTitle, tables);
	}
	
	/**
	 * @param a ArrayList of raw results
	 * @category Construction of Blocks for adjectives
	 */
	private void constructLoBlock(ArrayList<String> a) {
		
		String nTitle = constructLoTitle(a.get(0));
		ArrayList<SubBlock> sB = new ArrayList<SubBlock>();
		
		ArrayList<ArrayList<String>> rawSubBlocks = new ArrayList<ArrayList<String>>();
		ArrayList<String> temp = new ArrayList<String>();
		
		int count = 0;
		for( String s : a ) {
			
			if( !isIllegalLoRaw(s) ) {
				
				if( s.contains("h3") ) {
					// Do nothing
				}
				else if( s.contains("h4") && count > 0 ) {
					rawSubBlocks.add(temp);
					temp = new ArrayList<String>();
					count = 0;
					
					temp.add(s);
					count++;
					
				}
				else {
					temp.add(s);
					count++;
				}
				
			}
			
		}
		rawSubBlocks.add(temp);
		
		for( ArrayList<String> s : rawSubBlocks ) {
			
			SubBlock nSB = constructLoSubBlock(s);
			sB.add(nSB);
		}
		
		this.blocks.add(new Block(nTitle, sB));
	}
	
	/**
	 * @category Constructions of Blocks for adjectives
	 */
	private void populateLoBlocks() {
		for( ArrayList<String> aList : dump ) {
			
			constructLoBlock(aList);
			
		}		
	}
	
	
	private boolean isLegalRawNo(String a) {
		return (a.contains("tr") && (a.contains("Eintala") || a.contains("Fleirtala"))) ||
				(a.length() <= 3) || (a.contains("án greinis með greini"));
	}
	
	/**
	 * @param a ArrayList of raw results
	 * @category Construction of Tables for nouns
	 */
	private Tables constructNoTables(ArrayList<String> a) {
		
		String nTitle = destroyPointer(a.get(0));
		String[] columnNames = { "", "án greinis", "með greini" };
		String[] rowNames = { "", "Nf.", "Þf.", "Þgf.", "Ef." };
		ArrayList<String> content = new ArrayList<String>();
		
		ArrayList<String> temp = new ArrayList<String>();
		
		for( String s : a ) {
			if( s.contains(".") ) {
				temp = constructResultsFromRows(destroyDoublePointer(s), 1);
				
				for( String t : temp ) {
					content.add(t);
				}
			}
		}
		
		return new Tables(nTitle, columnNames, rowNames, content);
	}
	
	/**
	 * @param a ArrayList of raw results
	 * @category Construction of SubBlocks for nouns
	 */
	private void constructNoBlock(ArrayList<String> a) {
		
		String nTitle = "";
		ArrayList<SubBlock> sB = new ArrayList<SubBlock>();
		
		ArrayList<ArrayList<String>> rawData = new ArrayList<ArrayList<String>>();
		ArrayList<String> temp = new ArrayList<String>();
		int count = 0;
		for( String s : a ) {
			
			if( !isLegalRawNo(s) ) {
				
				if( s.contains("th") && count > 0 ) {
					rawData.add(temp);
					temp = new ArrayList<String>();
					count = 0;
					
					temp.add(s);
					count++;
				}
				else {
					temp.add(s);
					count++;
				}
				
			}
			
		}
		rawData.add(temp);
		
		ArrayList<Tables> tables = new ArrayList<Tables>();
		
		for( ArrayList<String> s : rawData ) {
			
			Tables oT = constructNoTables(s);
			tables.add(oT);
			
		}
		
		SubBlock oSB = new SubBlock(nTitle, tables);
		
		sB.add(oSB);
		
		this.blocks.add(new Block(nTitle, sB));
	}
	
	/**
	 * @category Constructions of Blocks for nouns
	 */
	private void populateNoBlocks() {
		
		for( ArrayList<String> aList : dump ) {
			
			constructNoBlock(aList);
			
		}
	}
	
	/**
	 * Constructs Blocks, SubBlocks and Tables for the search results from HTMLParser, depending on
	 * the type of the results
	 */
	private void populateBlockList() {
	    
		if(this.title.contains("Sagnorð")) {
	        populateSoBlocks();
	    }
	      
	    else if(this.title.contains("Lýsingarorð") || this.title.contains("Töluorð")) {
	        populateLoBlocks();
	    }
	      
	    else if(this.title.contains("Atviksorð")){
	        // TODO : make it work?
	    } 
	    else {
	    	populateNoBlocks();
	    }
	}
}
