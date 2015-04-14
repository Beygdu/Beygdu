package is.arnastofnun.utils;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import is.arnastofnun.DB.DBController;
import is.arnastofnun.beygdu.BeygingarActivity;
import is.arnastofnun.beygdu.CompareActivity;
import is.arnastofnun.beygdu.R;
import is.arnastofnun.parser.Block;
import is.arnastofnun.parser.SubBlock;
import is.arnastofnun.parser.Tables;

/**
 * @author Jón Friðrik Jónatansson
 * @since 20.10.14import is.arnastofnun
 * @version 0.9
 *
 * Generic Table which is constructed from the information in WordResult
 * The amount of rows and columns depends in the size of the ArrayLists containing the col and row headers in the WordResult object
 */
public class TableFragment extends Fragment {

    /**
     * context - is context the table is added to
     * content - is an arraylist of the content which is added into the table, in the right order
     * block - contains the row and col headers and the content of the of the table.
     * title - the title of the table
     * fonts - app fonts
     */
    private Context context;
    private TableLayout tableLayout;
    private Block block;
    private Tables table;
    private TextView title;
    private String dBWordTitle;
    private String dBBlockTitle;

    private DBController controller;
    private boolean compareTableFragment = false;
    private ArrayList<Tables> comparedTables;

    private long initTime = -1;

    //Fonts
    private Typeface LatoBold;
    private Typeface LatoSemiBold;
    private Typeface LatoLight;


    private int subBlockTitleText = 22;
    private int tableTitleText = 18;
    private int cellText = 16;


    //Fragments have to have one empty constructor
    public TableFragment() {

    }

    /**
     * @param context     er contextið sem taflan mun birtast í.
     * @param tableLayout - er layoutið sem taflan er sett í.
     * @param block       - inniheldur raðar og column headerana og contentið á töflunni
     * @param title       - er titilinn á töflunni
     */
    public TableFragment(Context context, TableLayout tableLayout, Block block, TextView title, String wordTitle, String blockTitle) {
        this.context = context;
        this.tableLayout = tableLayout;
        this.block = block;
        this.title = title;

        dBWordTitle = wordTitle;
        dBBlockTitle = blockTitle;

        // Fonts
        LatoBold = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Bold.ttf");
        LatoSemiBold = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Semibold.ttf");
        LatoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf");
    }

    /**
     * @param context er contextið sem taflan mun birtast í.
     * @param tableLayout - er layoutið sem taflan er sett í.
     * @param table - er taflan
     */
    public TableFragment(Context context, TableLayout tableLayout, Tables table, String wordTitle) {
        this.context = context;
        this.tableLayout = tableLayout;
        this.table = table;
        compareTableFragment = true;

        // Fonts
        LatoBold = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Bold.ttf");
        LatoSemiBold = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Semibold.ttf");
        LatoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf");
    }

    /**
     * @return the title of the word
     */
    public String getTitle() {
        return title.getText().toString();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.table, container, false);
        controller = new DBController(context);
        comparedTables = controller.fetchAllComparableWords();
        if(compareTableFragment) {
            createCompareTable(table);
        } else {
            createBlock();
        }
        return rootView;
        //Set typeface for fonts

    }

    /**
     * @author Snær Seljan, Jón Friðrik
     * constructs a TextView containing the title of the subBlock
     * and a TextView containing the title of the tables and then calls
     * the function createTable which constucts the tables in the subBlock
     */
    private void createBlock() {
        tableLayout.addView(title);
        //Iterate through sub-blocks and set title
        for (SubBlock sBlock : block.getBlocks()) {

            //Special case for nafnháttur
            if (sBlock.getTitle().equals("Nafnháttur")) {
                // Text title
                TextView nafnhatturTitle = new TextView(context);
                nafnhatturTitle.setText(sBlock.getTitle());
                nafnhatturTitle.setTextSize(subBlockTitleText);
                nafnhatturTitle.setMinHeight(70);
                nafnhatturTitle.setTypeface(LatoLight);
                nafnhatturTitle.setPadding(0, 20, 20, 20);
                nafnhatturTitle.setTextColor(getResources().getColor(R.color.white));
                tableLayout.addView(nafnhatturTitle);

                TextView tableTitle = new TextView(context);
                tableTitle.setText(sBlock.getTitle());
                createTableSpecial(sBlock.getTables().get(0));
                continue;
            }

            //Special case for lýsingarháttur nútíðar
            if (block.getTitle().toLowerCase().equals("lýsingarháttur nútíðar")) {
                TextView tableTitle = new TextView(context);
                tableTitle.setText(sBlock.getTitle());
                tableLayout.addView(tableTitle);
                createTableSpecial(sBlock.getTables().get(0));
                continue;
            }

            // The rest of the tables
            if (!sBlock.getTitle().equals("")) {
                TextView subBlockTitle = new TextView(context);
                subBlockTitle.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                subBlockTitle.setText(sBlock.getTitle());
                subBlockTitle.setTextSize(subBlockTitleText);
                subBlockTitle.setMinHeight(70);
                subBlockTitle.setTypeface(LatoLight);
                subBlockTitle.setTextColor(getResources().getColor(R.color.white));
                subBlockTitle.setPadding(0, 80, 20, 20);
                tableLayout.addView(subBlockTitle);
            }
            //Create the tables and set title
            for (final Tables tables : sBlock.getTables()) {
                final TextView tableTitle = new TextView(context);
                tableTitle.setText(tables.getTitle());
                tableTitle.setTextSize(tableTitleText);
                tableTitle.setHeight(50);
                tableTitle.setTextSize(20);
                tableTitle.setTypeface(LatoLight);
                tableTitle.setTextColor(getResources().getColor(R.color.white));
                tableTitle.setBackgroundResource(R.drawable.top_border_orange);
                tableTitle.setPadding(16, 5, 0, 10);
                tableTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_copy, 0);
                boolean dbContains = false;
                for(Tables dBTable : comparedTables) {
                    if (dBTable.getWordTitle().equals(dBWordTitle) && dBTable.getBlockTitle().equals(dBBlockTitle) && dBTable.getHeader().equals(tables.getTitle())){
                        dbContains = true;
                        tableTitle.setBackgroundResource(R.drawable.top_border_yellow);
                    }
                }
                tableTitle.setOnClickListener(getCompareClickListener(dBWordTitle, dBBlockTitle, tables.getTitle(), tableTitle, tables, dbContains));
                tableLayout.addView(tableTitle);
                createTable(tables);
            }
        }
    }

    /**
     * @param table the table which is to be built
     *              Makes a tableTow for each row which contains TextView for each column.
     * @author Snær Seljan, Jón Friðrik
     * @version 2.0
     * @since 20.03.15
     */
    private void createTable(Tables table) {
        final int rowNum = table.getRowNames().length;
        final int colNum = table.getColumnNames().length;
        final int rowLast = rowNum - 1;

        int contentIndex = 0;
        int counter = 0;
        for (int row = 0; row < rowNum; row++) {
            TableRow tr = new TableRow(context);
            //tr.setPadding(20, 10, 0, 10);

            // For small tables like, nafnbót and sagnbót
            if (rowNum < 2) {
                tr.setBackgroundResource(R.drawable.bottom_top_border_blue);
            }

            // Even numbers and not last row
            else if (row % 2 == 0 && (row != rowLast)) {
                tr.setBackgroundResource(R.drawable.top_border_blue);
            }


            // Odd numbers and not last row
            else if ((row % 1 == 0) && (row != rowLast)) {
                tr.setBackgroundResource(R.drawable.top_border_white);
            }

            // Last row
            else if (row == rowLast) {
                if (row % 2 == 0) {
                    tr.setBackgroundResource(R.drawable.bottom_top_border_blue);
                } else {
                    tr.setBackgroundResource(R.drawable.bottom_top_border_white);
                }
            }

            for (int col = 0; col < colNum; col++) {
                final TextView cell = new TextView(context);
                cell.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                cell.setGravity(Gravity.LEFT);
                cell.setTypeface(LatoLight);
                cell.setTextSize(cellText);
                cell.setPadding(5,10,0,10);

                if (!(row == 0 || col == 0)) {
                    cell.setClickable(true);
                    cell.setOnLongClickListener(new XLongClickListener(context, cell));
                }

                if (row % 2 == 0) {
                    cell.setTextColor(getResources().getColor(R.color.font_default));
                } else {
                    cell.setTextColor(getResources().getColor(R.color.font_default));
                }


                if (row == 0) {
                    if( col == 0) {
                        cell.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.6f));
                    }
                    if (table.getContent().size() == 1) {
                        cell.setText(table.getContent().get(row));
                    } else {
                        cell.setText(table.getColumnNames()[col]);
                    }
                } else {
                    if (col == 0) {
                        cell.setText(table.getRowNames()[row]);
                        cell.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.6f));
                    } else {
                        String cellString = table.getContent().get(contentIndex++);

                        if (cellString.contains("/")) {
                            String firstLine = cellString.split("/")[0];
                            String secondLine = cellString.split("/")[1];
                            cellString = firstLine + "/" + System.getProperty("line.separator") + secondLine;
                        }

                        int whiteSpaceCount = cellString.length() - cellString.replaceAll(" ", "").length();
                        String lastWord = "";
                        String allButLast = "";

                        if(whiteSpaceCount > 0) {
                            String[] allWords = cellString.split(" ");
                            lastWord = allWords[allWords.length - 1];
                            for(int i = 0;i < allWords.length-1; i++) {
                                allButLast += allWords[i] + " ";
                            }
                        }

                        if(whiteSpaceCount > 0) {
                            cell.setText(Html.fromHtml(allButLast +  "<b>" + lastWord + "</b>"));
                        }
                        else {
                            cell.setText(cellString);
                        }

                    }
                }
                tr.addView(cell);
            }
            tableLayout.addView(tr);
        }
    }

    /**
     * @param table the table which is to be built
     *              Makes a textview for a single table cell like nafnhattur, lysingarhattur þt.
     * @author Snær Seljan
     * @version 2.0
     * @since 20.03.15
     */
    private void createTableSpecial(Tables table) {
        TableRow tr = new TableRow(context);
        final TextView cell = new TextView(context);
        cell.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        cell.setHeight(50);
        cell.setPadding(20, 5, 0, 0);
        cell.setTextSize(20);
        cell.setBackgroundResource(R.drawable.top_border_orange);
        cell.setTypeface(LatoLight);
        cell.setTextColor(getResources().getColor(R.color.white));
        cell.setClickable(true);
        cell.setOnLongClickListener(new XLongClickListener(context, cell));
        cell.setText(table.getContent().get(0));
        cell.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_copy, 0);
        boolean dbContains = false;
        for(Tables dBTable : comparedTables) {
            if (dBTable.getWordTitle().equals(dBWordTitle) && dBTable.getBlockTitle().equals(dBBlockTitle) && dBTable.getHeader().equals(table.getTitle())){
                dbContains = true;
                cell.setBackgroundResource(R.drawable.top_border_yellow);
            }
        }
        cell.setOnClickListener(getCompareClickListener(dBWordTitle, dBBlockTitle, table.getTitle(), cell, table, dbContains));
        dbContains = false;

        tr.addView(cell);
        tableLayout.addView(tr);
    }

    private void createCompareTable(Tables table) {
        TextView tableTitle = new TextView(context);
        tableTitle.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        String label = table.getWordTitle();
        if (!table.getBlockTitle().equals("")) {
           label = label + " - " + table.getBlockTitle();
        }
        label = label + " - " + table.getHeader();
        tableTitle.setText(label);
        tableTitle.setTextSize(subBlockTitleText);
        tableTitle.setMinHeight(70);
        tableTitle.setTypeface(LatoLight);
        tableTitle.setTextColor(getResources().getColor(R.color.white));
        tableTitle.setPadding(0, 80, 20, 20);
        tableLayout.addView(tableTitle);

        final TextView tableName = new TextView(context);
        tableName.setText(table.getTitle());
        tableName.setTextSize(tableTitleText);
        tableName.setTextSize(20);
        tableName.setTypeface(LatoLight);
        tableName.setTextColor(getResources().getColor(R.color.white));
        tableName.setBackgroundResource(R.drawable.top_border_orange);
        tableName.setPadding(16, 5, 0, 10);
        tableName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_copy, 0);
        boolean dbContains = false;
        for(Tables dBTable : comparedTables) {
            if (dBTable.getWordTitle().equals(table.getWordTitle()) && dBTable.getBlockTitle().equals(table.getBlockTitle()) && dBTable.getHeader().equals(table.getTitle())){
                dbContains = true;
                tableName.setBackgroundResource(R.drawable.top_border_yellow);
            }
        }
        tableName.setOnClickListener(getCompareClickListener(table.getWordTitle(), table.getBlockTitle(), table.getTitle(), tableName, table, dbContains));
        tableLayout.addView(tableName);
        createTable(table);
    }

    private View.OnClickListener getCompareClickListener(final String wordTitle, final String blockTitle, final String tableTitle, final TextView cell, final Tables tables, final boolean chosen) {
        View.OnClickListener clickListener = new View.OnClickListener() {
            private boolean copyState = chosen;

            public void onClick(View view) {
                if (copyState) {
                    // reset background to default;
                    cell.setBackgroundResource(R.drawable.top_border_orange);
                    controller.removeCompareTable(wordTitle,blockTitle, tableTitle);
                    if(context.getClass().getSimpleName().equals(CompareActivity.class.getSimpleName())) {
                        Intent intent = new Intent(context, CompareActivity.class);
                        startActivity(intent);
                    }
                } else {
                    cell.setBackgroundResource(R.drawable.top_border_yellow);
                    controller.insertCompareTable(wordTitle, blockTitle, tableTitle, tables);
                }
                copyState = !copyState;
            }
        };
        return clickListener;
    }
}