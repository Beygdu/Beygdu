package is.arnastofnun.parser;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Arnar Jonsson
 * @since 21.11.2014
 * @version 0.1
 */
public class Block implements Serializable {

  private String title;
  
  private ArrayList<SubBlock> sb;
  
  /**
 * @param title Title of the Block
 * @param sb A list of SubBlocks belonging to the Block
 */
public Block(String title, ArrayList<SubBlock> sb) {
    this.title = title;
    this.sb = sb;
  }
  
  /**
 * @return Title of the Block
 */
public String getTitle() {
    return this.title;
  }
  
  /**
 * @return A list of SubBlocks belonging to the Block
 */
public ArrayList<SubBlock> getBlocks() {
    return this.sb;
  }
  
  
}