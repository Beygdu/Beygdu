package is.arnastofnun.beygdu.is.arnastofnun.beygdur.soonToBeAdded.BINParesr;

import java.util.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


// hofundur. Arnar Jonsson
public class BINParser {

  String url = "";
  String searchWord;
  
  private HTMLParser parser;
  
  private WordResult wR = new WordResult();

  public BINParser(String searchWord, int flag) {
  
    constructSearchStringByString(searchWord, flag);
    
    this.parser = new HTMLParser(this.url);
    this.wR.setSearchWord(searchWord);
    
    //constructWordResult();
  
  }
  
  public BINParser(String searchWord, int flag, String[] elements) {
  
    constructSearchStringByString(searchWord, flag);
    
    this.parser = new HTMLParser(this.url, elements);
    this.wR.setSearchWord(searchWord);
    
    constructWordResult(elements);
  
  }
  
  public BINParser(int id) {
  
    constructSearchStringById(id);
    
    this.parser = new HTMLParser(this.url);
    
    //constructWordResult();
  
  }
  
  public BINParser(int id, String[] elements) {
  
    constructSearchStringById(id);
    
    this.parser = new HTMLParser(this.url, elements);
    
    constructWordResult(elements);
  
  }
   
  /////
  // Public Methods
  /////
  
  public String getAsString() {
  
    return this.parser.asString();
  
  }
  
  public ArrayList<Element> getSelectedElements() {
    return this.parser.getSelectedElements();
  }
  
  public WordResult getWordResult() {
    return this.wR;
  }
  
  /////
  // Private Methods
  /////
  
  private void constructSearchStringByString(String searchWord, int flag) {
  
    if(flag == 0) {
      this.url = "http://dev.phpbin.ja.is/ajax_leit.php/" + "?q=" + searchWord;
    }
    else {
      this.url = "http://dev.phpbin.ja.is/ajax_leit.php/" + "?q=" + searchWord + "&ordmyndir=on";
    }
  
  }
  
  private void constructSearchStringById(int id) {
  
    this.url = "http://dev.phpbin.ja.is/ajax_leit.php/?id=" + id;
  
  }
  

  private void constructWordResult(String[] elements) {
  
    // Identify result type
    if( this.parser.containsClass("VO_beygingarmynd") ) {
      this.wR.setDescription("SingleHit");
    }
    else if( this.parser.containsClass("alert") ) {
      this.wR.setDescription("Miss");
    }
    else {
      this.wR.setDescription("MultiHit");
    }
    
    this.wR.constructWordResult(this.parser, elements);
  }

}