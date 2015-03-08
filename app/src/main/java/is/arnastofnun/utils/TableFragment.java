package is.arnastofnun.utils;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
     */
    private Context context;
    private TableLayout tableLayout;
    private Block block;
    private TextView title;

    //Fonts
    private Typeface LatoBold;
    private Typeface LatoSemiBold;
    private Typeface LatoLight;



    //Fragments have to have one empty constructor
    public TableFragment() {

    }

    /**
     * @param context er contextið sem taflan mun birtast í.
     * @param tableLayout - er layoutið sem taflan er sett í.
     * @param block - inniheldur raðar og column headerana og contentið á töflunni
     * @param title - er titilinn á töflunni
     */
    public TableFragment(Context context, TableLayout tableLayout, Block block, TextView title) {
        this.context = context;
        this.tableLayout = tableLayout;
        this.block = block;
        this.title = title;

        // Fonts
        LatoBold = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Bold.ttf");
        LatoSemiBold = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Semibold.ttf");
        LatoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf");
    }

    /**
     * @return the title of the table
     */
    public CharSequence getTitle() {
        return title.getText();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.table,
                container, false);
        createBlock();
        return rootView;
        //Set typeface for fonts

    }

    /**
     * constructs a TextView containing the title of the subBlock
     * and a TextView containing the title of the tables and then calls
     * the function createTable which constucts the tables in the subBlock
     */
    private void createBlock() {
        tableLayout.addView(title);
        //Iterate through sub-blocks and set title
        for (SubBlock sBlock: block.getBlocks()){
            if(!sBlock.getTitle().equals("")) {
                TextView subBlockTitle = new TextView(context);
                subBlockTitle.setText(sBlock.getTitle());
                subBlockTitle.setTextSize(22);
                subBlockTitle.setMinHeight(70);
                subBlockTitle.setTypeface(LatoLight);
                subBlockTitle.setBackgroundResource(R.drawable.bottom_border);
                subBlockTitle.setTextColor(getResources().getColor(R.color.font_default));
                subBlockTitle.setPadding(0,50,0,50);
                tableLayout.addView(subBlockTitle);
            }
            //Create the tables and set title
            for (Tables tables : sBlock.getTables()) {
                //if(!tables.getTitle().equals("")) {
                    TextView tableTitle = new TextView(context);
                    tableTitle.setText(tables.getTitle());
                    tableTitle.setTextSize(20);
                    tableTitle.setMinHeight(80);
                    tableTitle.setTypeface(LatoLight);
                    tableTitle.setPadding(10, 20, 0, 20);
                    tableLayout.addView(tableTitle);
                    createTable(tables);
                //}
            }
        }
    }

    /**
     * @param table the table which is to be built
     * Makes a tableTow for each row which contains TextView for each column.
     */
    private void createTable(Tables table) {
        final int rowNum = table.getRowNames().length;
        final int colNum = table.getColumnNames().length;

        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        tableRowParams.weight = colNum;
        tableRowParams.setMargins(1, 1, 1, 1);

        int contentIndex = 0;
        for (int row = 0; row < rowNum; row++) {
            TableRow tr = new TableRow(context);
            tr.setLayoutParams(tableRowParams);
            tr.setMinimumHeight(80);
            if (row % 2 == 0) {
                tr.setBackgroundResource(R.drawable.top_border_blue);
            }
            else if (row == (rowNum - 1)){
                if(row % 2 == 0) {
                    tr.setBackgroundResource(R.drawable.bottom_top_border_blue);
                }
                else {
                    tr.setBackgroundResource(R.drawable.bottom_top_border_white);
                }
            }
            else if(row % 1 == 0) {
                tr.setBackgroundResource(R.drawable.top_border_white);
            }
            if(rowNum < 2) {
                tr.setBackgroundResource(R.drawable.bottom_top_border_blue);
            }

            for (int col = 0; col < colNum; col++) {
                TextView cell = new TextView(context);
                cell.setTextAppearance(context, R.style.BodyText);
                cell.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
                cell.setMinHeight(80);
                cell.setGravity(Gravity.CENTER);
                cell.setTypeface(LatoLight);
                if (row % 2 == 0) {
                    cell.setTextColor(getResources().getColor(R.color.white));
                }
                else {
                    cell.setTextColor(getResources().getColor(R.color.font_default));
                }


                if (row == 0) {
                    if (table.getContent().size() == 1) {
                        cell.setText(table.getContent().get(row));
                    } else {
                        cell.setText(table.getColumnNames()[col]);
                    }
                } else {
                    if (col == 0) {
                        cell.setText(table.getRowNames()[row]);
                    } else {
                        cell.setText(table.getContent().get(contentIndex++));
                    }
                }


                tr.addView(cell);
            }
            tableLayout.addView(tr);
        }
    }
}