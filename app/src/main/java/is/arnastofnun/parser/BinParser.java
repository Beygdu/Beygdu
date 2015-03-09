package is.arnastofnun.parser;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

/**
 * Created by arnarjons on 8.3.2015.
 */
public class BinParser {
    String url = "";
    String searchWord;

    private HTMLParser parser;

    private WordResult wR = new WordResult();

    public BinParser(String searchWord, int flag) {

        constructSearchStringByString(searchWord, flag);

        this.parser = new HTMLParser(this.url);
        this.wR.setSearchWord(searchWord);

        //constructWordResult();

    }

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

    public BinParser(int id, int step, String[] elements) {

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
            this.url = "http://dev.phpBin.ja.is/ajax_leit.php/" + "?q=" + searchWord;
        }
        else {
            this.url = "http://dev.phpBin.ja.is/ajax_leit.php/" + "?q=" + searchWord + "&ordmyndir=on";
        }

    }

    private void constructSearchStringById(int id) {

        this.url = "http://dev.phpBin.ja.is/ajax_leit.php/?id=" + id;

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
