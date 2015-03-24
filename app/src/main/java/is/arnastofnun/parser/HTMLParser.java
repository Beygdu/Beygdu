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
 * @since 24.01.2015
 * @version 1.0
 *
 */
public class HTMLParser {

    String searchString;
    String[] elements;

    Document doc;
    Element body;

    String asString;

    ArrayList<Element> selectedElements;

    /**
     * HTMLParser - Creates a Jsoup HTML document from the requested url
     * @param url A string representation of an valid URL
     */
    public HTMLParser(String url) {

        this.searchString = url;

        createDocument();

    }

    /**
     * HTMLParser - Creates a jsoup document from the requested url
     * @param url A string representation of an valid URL
     * @param elements An array of HTML elements that will populate the Jsoup document
     */
    public HTMLParser(String url, String[] elements) {

        this.searchString = url;

        this.elements = elements;

        createDocument();

    }

    /**
     * @return An Jsoup document containing the requested url
     */
    public Document getDocument() {
        return this.doc;
    }

    /**
     * @return A string representation of the Jsoup document
     */
    public String asString() {

        if(this.asString == null) {

            this.asString = this.doc.toString();
        }

        return this.asString;

    }

    /**
     * @param nodeName A string representation of a HTML tag
     * @return True if the parsed document contains the tag
     */
    public boolean containsClass(String nodeName) {

        Elements elements = this.body.getAllElements();
        for( Element element : elements ) {
            if( element.hasClass(nodeName) ) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param tag A string representation of a HTML tag
     * @return Elements containing the requested tag
     */
    public String getSpecificTextElement(String tag) {
        return this.doc.getElementsByTag(tag).text();
    }

    /**
     * @return A Jsoup document containing the specific elements given to the parser
     */
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

    /**
     * createDocument - creates a Jsoup document based on the parsers given url
     */
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
  

