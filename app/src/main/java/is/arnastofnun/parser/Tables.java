package is.arnastofnun.parser;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Arnar Jonsson
 * @since 21.11.2014
 * @version 0.1
 */
public class Tables implements Serializable {

  private String title;
  
  private String[] columnNames;
  
  private String[] rowNames;
  
  private ArrayList<String> content;
  
  /**
 * @param title The title of the table
 * @param columnNames An array of column titles for the table
 * @param rowNames An array of row titles for the table
 * @param content A list of displayed content of the table
 */
public Tables(String title, String[] columnNames, String[] rowNames, ArrayList<String> content) {
  
    this.title = title;
    this.columnNames = columnNames;
    this.rowNames = rowNames;
    this.content = content;
  
  }
  
  
  /**
 * @return The title of the table
 */
public String getTitle() {
    return this.title;
  }
  

  /**
 * @return An array of the tables column titles
 */
public String[] getColumnNames() {
    return this.columnNames;
  }
  
  /**
 * @return An array of the tables rows titles
 */
  public String[] getRowNames() {
    return this.rowNames;
  }
  

  /**
 * @return An ArrayList of contents in the table
 */
public ArrayList<String> getContent() {
    return this.content;
  }

}
