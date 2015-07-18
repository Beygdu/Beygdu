package is.arnastofnun.parser;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Arnar Jonsson
 * @since 27.1.2015
 * @version 0.4
 *
 * WordResult is an application-unique class
 */
public class WordResult implements Serializable {

    private String searchWord;
    private String description;

    /**
     * multiHitDescriptions : Array of Unicode representations of icelandic words that met the
     * search word of the BinParser
     * multiHitIds : Array of database ids correlating to icelandic words that met the
     * search word of the BinParser
     */
    private String[] multiHitDescriptions;
    private int[] multiHitIds;

    private String title;
    private String warning;

    /**
     * An ArrayList of Blocks
     * If the search result of the HTMLParser returns a valid search the content of the
     * callback is divided into 3 objects.
     * The simplest one is called Tables. Each Tables contain a single table object filled with
     * its unique results.
     * The tables belong to a SubBlock object. A SubBlock is a Tables group, containing unique Tables
     * of the same category.
     * The SubBlocks belong to a Block object. A Block object is a SubBlock group, containing unique
     * SubBlocks of similar category.
     */
    private ArrayList<Block> result = new ArrayList<Block>();

    /**
     * WordResult - An object that stores and beautifies result from BinParsers (HTMLParser).
     * It categorizes the results and readies them for display by the utilization fragments of the
     * application
     */
    public WordResult() {

    }

    /////
    // Get/Set Methods
    /////

    public void setDescription(String dscrp) {
        this.description = dscrp;
    }

    /**
     * @return Alert-description of the Bin search results, if any
     */
    public String getDescription() {
        return this.description;
    }

    public void setSearchWord(String sWord) {
        this.searchWord = sWord;
    }

    /**
     * @return String representation of the Bin search word
     */
    public String getSearchWord() {
        return this.searchWord;
    }

    public void setMultiHitDescriptions(String[] multiHitDescriptions) {
        this.multiHitDescriptions = multiHitDescriptions;
    }

    /**
     * @return String representations of the Bin search word, if any
     */
    public String[] getMultiHitDescriptions() {
        return this.multiHitDescriptions;
    }

    /**
     * @return Ids correlating to the Bin search word, if any,
     */
    public int[] getMultiHitIds() {
        return this.multiHitIds;
    }

    /**
     * @return Header of the search
     */
    public String getTitle() {
        return this.title;
    }


    public void setMultiHitIds(int[] multiHitIds) {
        this.multiHitIds = multiHitIds;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Warning message from the Bin search result, if any
     */
    public String getWarning() {
        return this.warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    /**
     * @return The content of the result displaying activity, if any
     */
    public ArrayList<Block> getBlocks() {
        return this.result;
    }

    public void setBlocks(ArrayList<Block> blocks) {
        this.result = blocks;

    }




}
