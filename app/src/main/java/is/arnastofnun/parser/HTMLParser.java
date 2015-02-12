package is.arnastofnun.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Arnar Jonsson
 * @since 18.11.2014
 * @version 0.6
 *
 */
public class HTMLParser {

  public static ParserResult PR = new ParserResult();

  

  /**
 * @param doc A Jsoup HTML document 
 */
public HTMLParser(Document doc) {
	  
	  constructParserResults(doc);
	  
  }

  /**
 * @return Object ParserResult, contains raw results extracted from the HTML document
 */
public ParserResult getParserResult() {
	  return this.PR;
  }
  
  
//
// Helper Functions
//
  private int constructInt(String a) {
    
    Pattern pattern = Pattern.compile("\\d+");
    Matcher matcher = pattern.matcher(a);
    int start = 0;
    int end = 0;
    while( matcher.find() ) {
      start = matcher.start();
      end = matcher.end();
    }
    
    int result = 0;
    try {
      result = Integer.parseInt(a.substring(start, end));
    }
    catch( NumberFormatException e) {
      result = 0;
    }
    
    return result;
  }

  private int[] manageMultipleIds(String[] a) {
    
    int[] returnValue = new int[a.length];
    int iterations;
    for( int i = 0; i < a.length; i++ ) {
      iterations = constructInt(a[i]);
      returnValue[i] = iterations;
    }
    
    return returnValue;
  }  
  
 
  
//
// Single results
//
  private void constructSingleResult(Document doc) {
    
    String title = doc.getElementsByTag("h2").text();
    
    PR.setTitle(title);
    String warning = "";
    try {
      Elements bad = doc.getElementsByClass("alert");
      warning = bad.text();
    }
    catch( NullPointerException e ) {
      // Do nothing
    }
    
    PR.setNode(warning);
    
    Element body = doc.body();
    
    ArrayList<ArrayList<String>> blockres = new ArrayList<ArrayList<String>>();
    
    int outCounter = 0;
    
    ArrayList<String> oneBlock = new ArrayList<String>();
    
    for( Element e : body.getAllElements() ) {
      
      if( e.tag().toString() == "h3" ) {
	if( outCounter != 0 ) {
	  blockres.add(oneBlock);
	  //testPrinting(oneBlock);
	  oneBlock = new ArrayList<String>();
	  outCounter = 0;
	}
	oneBlock.add("h3 " + e.text());
	outCounter++;
      
      }
      
      if( e.tag().toString() == "h4" ) {
	
	oneBlock.add("h4 " + e.text());
      
      }
      
      if( e.tag().toString() == "th" ) {
	
	oneBlock.add("th " + e.text());
      
      }
      
      if( e.tag().toString() == "tr" ) {
	
	oneBlock.add("tr " + e.text());
      
      }
      
    }
    blockres.add(oneBlock);
    //System.out.println(blockres.size());
    PR.setResults(blockres);
    
    
  }  
  
  
  
//
// Multiple results
//
  private void constructMultipleResults(Document doc) {
    
    String[] ids = new String[doc.select("li").size()];
    String[] desc = new String[doc.select("li").size()];
    
    int count = 0;
    
    for( Element e : doc.select("li") ) {
      desc[count] = e.text();
      
      ids[count] = e.getElementsByTag("a").attr("onClick").toString();
      
      count++;
    }
    
    PR.setDesc(desc);
    
    PR.setIds(manageMultipleIds(ids));
    
    
    
  }  
  
  
  
//
// constructParserResults
//
  private void constructParserResults(Document doc) {
    Element body = doc.body();
    if( body.text().contains("Orðið") && body.text().contains("fannst ekki") && body.text().contains("Ef þú telur að orðið sé fullgilt") ) {
      PR.setType("Word not found");
    }
    else {
      
      if( body.text().contains("Smelltu á það orð sem þú vilt sjá:") ) {
	PR.setType("Multiple results");
	constructMultipleResults(doc);
      }
      
      else {
	PR.setType("Single result");
	constructSingleResult(doc);
      }
    }
  }  
}
  

