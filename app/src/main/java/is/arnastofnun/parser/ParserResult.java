package is.arnastofnun.parser;




import java.util.ArrayList;


/**
 * @author Arnar Jonsson
 * @since 18.11.2014
 * @version 0.4
 *
 */
public class ParserResult {

  private String type;

  private String searchWord;
  
  private int id;
  
  private int[] ids;
  private String[] desc;
  
  
  private String title;
  private String note;
  private ArrayList<ArrayList<String>> results;
  

  /**
 *  Empty constructor
 */
public ParserResult() {
  
  }
  
  /**
 * @return The header of the search (search string), if there is any
 */
public String getSearchWord() {
    return this.searchWord;
  }

  /**
 * @param a Sets the header of the search (search string)
 */
public void setSearchWord(String a) {
    this.searchWord = a;
  }

  /**
 * @return The id of the header of the search, if there is any
 */
public int getId() {
    return this.id;
  }

  /**
 * @param a Sets the id of the search
 */
public void setId(int a) {
    this.id = a;
  }

  /**
 * @return The type of the results, if there is any
 */
public String getType() {
    return this.type;
  }
  
  /**
 * @param a Sets the type of the results
 */
public void setType(String a) {
    this.type = a;
  }

  /**
 * @return An array of id's if the search string of multiple hits, if there are any
 */
public int[] getIds() {
    return this.ids;
  }

  /**
 * @param a Sets the array of id's used for multiple result hits
 */
public void setIds(int[] a) {
    this.ids = a;
  }

  /**
 * @return A description of the search result, if there is any
 */
public String[] getDesc() {
    return this.desc;
  }

  /**
 * @param a Sets a description of the search result
 */
public void setDesc(String[] a) {
    this.desc = a;
  }

  /**
 * @return The title string of the results, if there is any
 */
public String getTitle() {
    return this.title;
  }

  /**
 * @param a Sets the title of the results
 */
public void setTitle(String a) {
    this.title = a;
  }

  /**
 * @return The note of the results, if there is any
 */
public String getNote() {
    return this.note;
  }

  /**
 * @param a Sets the note of the results
 */
public void setNode(String a) {
    this.note = a;
  }

  /**
 * @return A list of lists containing raw results categorized by identify substrings, if there are any
 */
public ArrayList<ArrayList<String>> getResults() {
    return this.results;
  }

  /**
 * @param a Sets a list of lists containing raw results categorize by identify substrings
 */
public void setResults(ArrayList<ArrayList<String>> a) {
    this.results = a;
  }
  
  /**
 * @return Object WordResults which contains refined search results categorized by the
 * Block - SubBlock - Tables architecture
 */
public WordResult getWordResult() {
  return null;
  //return new WordResult(this.type, this.title, this.note, this.results);
  
  }

 
}