package methode.methodeelectronics_newdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    private MyDatabaseHandler db = new MyDatabaseHandler(getContext());
    public CustomAdapter(Context context, ArrayList<String> anyfile) {
        super(context, R.layout.custom_row,anyfile);
    }

    @Override
    public View getView(int position1, View convertView, ViewGroup parent) {
        LayoutInflater myinflator = LayoutInflater.from(getContext());
        View customView = myinflator.inflate(R.layout.custom_row, parent, false);
        String singlefile = getItem(position1);
        //String singlefile2 = getItem(position1);
        TextView itemname = (TextView)customView.findViewById(R.id.mp3songname);
        ImageView itemimage = (ImageView)customView.findViewById(R.id.imageView);
        TextView itemduration = (TextView)customView.findViewById(R.id.mp3songduration);
        itemname.setText(singlefile);
        itemduration.setText(String.valueOf(position1+1));
        itemimage.setImageResource(R.drawable.mp3sample);
        return customView;
    }

}