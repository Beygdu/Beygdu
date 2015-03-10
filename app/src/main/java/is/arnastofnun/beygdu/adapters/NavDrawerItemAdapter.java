package is.arnastofnun.beygdu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import is.arnastofnun.beygdu.R;

/**
 * @author Daniel Pall Johannsson
 * @since 06.03.2015
 * @version 1.0
 *
 * NavDrawerItemAdapter maps the item to the list view in each layout.
 */
public class NavDrawerItemAdapter extends BaseAdapter{

    // An ArrayList that contains the information to put into each item
    private ArrayList<String> itemList;
    Context context;

    public NavDrawerItemAdapter(ArrayList<String> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        // Reuse the view if it is empty
        if(v == null){
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.nav_drawer_item,null);
        }

        // Get the Text view we want to populate with our data
        TextView textView = (TextView) v.findViewById(R.id.navDrawerItem);

        // Get the String at the designated position
        String itemTitle = itemList.get(position);

        // Set the Text view with the itemTitle we got
        textView.setText(itemTitle);

        return v;
    }
}
