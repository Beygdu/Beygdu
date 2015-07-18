package is.arnastofnun.parser;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import is.arnastofnun.utils.Bstring;

/**
 * BParser.class
 * Fetches a HTML document from a selected url as a Jsoup 1.8.2 document
 *
 * @author Arnarjons
 */
public class BParser {

    private Bstring url;
    private String[] elements;
    private boolean assertKey;

    private Document doc;
    private ArrayList<Element> selectedElements;

    /**
     * BParser
     * @param url a string representation of an url?
     */
    public BParser(String url) {
        this.url = new Bstring(url);

        createDocument();
    }

    /**
     * BParser
     * @param url a string representation of an url?
     * @param elements String array of specific HTML5 element tags?
     */
    public BParser(String url, String[] elements) {

        this.url = new Bstring(url);
        this.elements = elements;

        createDocument();
    }

    /**
     * getDocument()
     * @return Jsoup document
     */
    public Document getDocument() {
        return doc;
    }

    /**
     * getElements
     * @return Array of the "heaviest" HTML elements?
     */
    public String[] getElements() {
        return elements;
    }

    /**
     * asSting
     * @return a String representation of a Jsoup document
     */
    public String asString() {
        return doc.toString();
    }

    /**
     * getSelectedElements
     * @return ArrayList of Jsoup elements according to the constructor arguments
     */
    public ArrayList<Element> getSelectedElements() {
        if(elements == null) return null;

        if(selectedElements == null) {

            selectedElements = new ArrayList<Element>();
            Element body = doc.body();
            for( Element element : body.getAllElements() ) {
                selectedElements.add(element);
            }

        }

        return selectedElements;
    }

    /**
     * containsNode(String nodeName)
     * @param nodeName A string representation of a HTML element tag
     * @return true if the document contains the tag nodeName, false otherwise
     */
    public boolean containsNode(String nodeName) {
        Elements body = doc.body().getAllElements();
        for( Element element : body ) {
            if(element.hasClass(nodeName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Assert()
     * @return True if the Jsoup document was created, false otherwise
     */
    public boolean Assert() {
        return assertKey;
    }

    private void createDocument() {
        try {
            doc = Jsoup.connect(this.url.get()).get();
            assertKey = true;
        }
        catch (Exception e) {
            Log.w("BParser createDocument", e.toString());
            assertKey = false;
        }
    }

}
