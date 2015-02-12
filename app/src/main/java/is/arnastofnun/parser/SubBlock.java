package is.arnastofnun.parser;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Arnar Jonsson
 * @since 21.11.2014
 * @version 0.1
 */
public class SubBlock implements Serializable {

  private String title;
  
  private ArrayList<Tables> tables;

  /**
 * @param title Title if the SubBlock
 * @param tables A list of tables belonging to the SubBlock
 */
public SubBlock(String title, ArrayList<Tables> tables) {
  
    this.title = title;
    
    this.tables = tables;
  
  }
  
  /**
 * @return Title of the SubBlock
 */
public String getTitle() {
    return this.title;
  }
  
  /**
 * @return A list of the tables belonging to the SubBlock
 */
public ArrayList<Tables> getTables() {
    return this.tables;
  }

  
  

}