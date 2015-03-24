package is.arnastofnun.parser;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

/**
 * Created by Arnar Jónsson on 8.3.2015.
 * Class BinParser
 */
public class BinParser {

    /**
     * searchWord - The string you want to search for
     * url - The constructed search string
     */
    String url = "";
    String searchWord;

    private HTMLParser parser;

    private WordResult wR = new WordResult();

    // Depricated
    public BinParser(String searchWord, int flag) {

        constructSearchStringByString(searchWord, flag);

        this.parser = new HTMLParser(this.url);
        this.wR.setSearchWord(searchWord);

        //constructWordResult();

    }

    /**
     * BinParser - Handler for HTMLParser for the Beygdu application.
     * Uses HTMLParser to create a Jsoup HTML document containing
     * search results from manual user input.
     * The user input should be a representation of an icelandic word.
     * The results are inflections of the icelandic words searched for -
     * if there are any
     * @param searchWord A string representation of an icelandic word
     * @param flag Degree of search, 0 is simple, 1 is advanced
     * @param elements A list of string representations of HTML elements wanted in
     *                 the parsed document
     */
    public BinParser(String searchWord, int flag, String[] elements) {

        constructSearchStringByString(searchWord, flag);

        this.parser = new HTMLParser(this.url, elements);
        this.wR.setSearchWord(searchWord);

        constructWordResult(elements);

    }

    public BinParser(int id) {

        constructSearchStringById(id);

        this.parser = new HTMLParser(this.url);

        //constructWordResult();

    }

    /**
     * BinParser - Handler for HTMLParser for the Beygdu application.
     * Uses HTMLParser to create a Jsoup HTML document containing
     * search results from manual user input.
     * The user input should be a representation of an icelandic word.
     * The results are inflections of the icelandic words searched for -
     * if there are any
     *
     * Not intended for public use
     *
     * @param id Id of an icelandic word according to BIN, belonging to Árnastofnun
     * @param elements A list of string representations of HTML elements wanted in
     *                 the parsed document
     */
    public BinParser(int id, String[] elements) {

        constructSearchStringById(id);

        this.parser = new HTMLParser(this.url, elements);

        constructWordResult(elements);

    }

    /////
    // Public Methods
    /////

    /**
     * @return A string representation of the parsed HTML document
     */
    public String getAsString() {

        return this.parser.asString();

    }

    /**
     * @return A document containing the class specific elements
     */
    public ArrayList<Element> getSelectedElements() {
        return this.parser.getSelectedElements();
    }

    /**
     * getWordResult - Returns an WordResult object
     * The object contains details and if any, content of the word searched for
     * @return
     */
    public WordResult getWordResult() {
        return this.wR;
    }

    /////
    // Private Methods
    /////

    private void constructSearchStringByString(String searchWord, int flag) {

        if(flag == 0) {
            this.url = "http://dev.phpBin.ja.is/ajax_leit.php/" + "?q=" + searchWord;
        }
        else {
            this.url = "http://dev.phpBin.ja.is/ajax_leit.php/" + "?q=" + searchWord + "&ordmyndir=on";
        }

    }

    private void constructSearchStringById(int id) {

        this.url = "http://dev.phpBin.ja.is/ajax_leit.php/?id=" + id;

    }

    /**
     * constructWordResult - Passes arguments to an WordResult object
     * The object creates its content and is stored
     * @param elements A list of string representations of HTML elements wanted in the parsed document
     */
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
