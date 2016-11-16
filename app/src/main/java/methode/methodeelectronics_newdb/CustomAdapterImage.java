package methode.methodeelectronics_newdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterImage extends ArrayAdapter<String> {
    private MyDatabaseHandler db = new MyDatabaseHandler(getContext());
    public CustomAdapterImage(Context context, ArrayList<String> anyfile) {
        super(context, R.layout.custom_row,anyfile);
    }

    @Override
    public View getView(int position1, View convertView, ViewGroup parent) {
        hold holdobject = new hold();
        LayoutInflater myinflator = LayoutInflater.from(getContext());
        View customView = myinflator.inflate(R.layout.custom_row, parent, false);
        String singlefile = getItem(position1);
        //String singlefile2 = getItem(position1);
        TextView itemname = (TextView)customView.findViewById(R.id.mp3songname);
        ImageView itemimage = (ImageView)customView.findViewById(R.id.imageView);
         TextView itemduration = (TextView)customView.findViewById(R.id.mp3songduration);
        itemname.setText(singlefile);
        itemduration.setText(String.valueOf(position1+1));
        itemimage.setImageResource(R.drawable.galleryicon);
//        String path = "storage/extSdCard/photos/";
//        path+=singlefile;
//        Uri uri=Uri.parse(path);
//        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(uri));
//        itemimage.setImageBitmap(bitmap);
//        holdobject.duration = itemduration;
//        holdobject.name = itemname;
//        holdobject.img = itemimage;
//        customView.setTag(holdobject);
//        holdobject.name.setText(singlefile);
//        holdobject.img.setImageBitmap(bitmap);

        return customView;
    }
    public static class hold{
        public ImageView img;
        public TextView name;
        public TextView duration;
    }
}