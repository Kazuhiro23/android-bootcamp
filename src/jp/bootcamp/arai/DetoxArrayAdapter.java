package jp.bootcamp.arai;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetoxArrayAdapter extends ArrayAdapter<News> {

    private LayoutInflater mLayoutInflater;

    public DetoxArrayAdapter(Context context, List<News> object) {

        super(context, 0, object);

        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.list_item_detox, parent, false);
        } else {
            view = convertView;
        }

        News newsItem = getItem(position);
        TextView title = (TextView)view.findViewById(R.id.Title);
        TextView userName = (TextView)view.findViewById(R.id.User);
        CharSequence cs = Html.fromHtml(newsItem.getTitle());
        title.setText(cs);
        userName.setText(newsItem.getPublisher());
        return view;

    }

}
