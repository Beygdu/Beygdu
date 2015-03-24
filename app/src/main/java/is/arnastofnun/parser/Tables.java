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
     * Tables - A representation of a Table
     * Contains a title, column names, row names and content
     *
     * @param title Title of the table
     * @param columnNames Column names of the table
     * @param rowNames Row names of the table
     * @param content Content of the table
     */
    public Tables(String title, String[] columnNames, String[] rowNames, ArrayList<String> content) {
  
        this.title = title;
        this.columnNames = columnNames;
        this.rowNames = rowNames;
        this.content = content;
  
    }

    /**
     * @return Title of the table
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @return Column names of the table
     */
    public String[] getColumnNames() {
        return this.columnNames;
    }

    /**
     * @return Row names of the table
     */
    public String[] getRowNames() {
        return this.rowNames;
    }

    /**
     * @return Content of the table
     */
    public ArrayList<String> getContent() {
        return this.content;
    }

}
