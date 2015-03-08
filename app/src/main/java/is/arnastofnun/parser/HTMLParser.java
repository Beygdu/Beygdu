package is.arnastofnun.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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

    String searchString;
    String[] elements;

    Document doc;
    Element body;

    String asString;

    ArrayList<Element> selectedElements;

    public HTMLParser(String url) {

        this.searchString = url;

        createDocument();

    }

    public HTMLParser(String url, String[] elements) {

        this.searchString = url;

        this.elements = elements;

        createDocument();

    }

    /////
    // Public Methods
    /////

    public Document getDocument() {
        return this.doc;
    }

    public String asString() {

        if(this.asString == null) {

            this.asString = this.doc.toString();
        }

        return this.asString;

    }

    public boolean containsClass(String nodeName) {

        Elements elements = this.body.getAllElements();
        for( Element element : elements ) {
            if( element.hasClass(nodeName) ) {
                return true;
            }
        }

        return false;
    }

    public String getSpecificTextElement(String tag) {
        return this.doc.getElementsByTag(tag).text();
    }

    public ArrayList<Element> getSelectedElements() {

        if( this.elements == null ) {
            return null;
        }

        if( this.selectedElements == null ) {
            this.selectedElements = new ArrayList<Element>();

            for( Element element : this.body.getAllElements() ) {
                if( containsElement(element.tagName()) ) {
                    this.selectedElements.add(element);
                }
            }
        }

        return selectedElements;

    }

    /////
    // Private Methods
    /////

    private boolean containsElement(String tag) {

        for( String string : this.elements ) {

            if( tag.equals(string) ) {
                return true;
            }

        }

        return false;

    }

    private void createDocument() {

        try {
            Document newDoc = Jsoup.connect(this.searchString).get();
            this.doc = newDoc;
            this.body = this.doc.body();
        }
        catch( IOException e ) {
            this.doc = null;
            this.body = null;
        }

    }
}
  

