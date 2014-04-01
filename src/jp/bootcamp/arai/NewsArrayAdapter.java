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

public class NewsArrayAdapter extends ArrayAdapter<News> {

    private LayoutInflater mLayoutInflater;

    public NewsArrayAdapter(Context context, List<News> object) {

        super(context, 0, object);

        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.list_item_news, parent, false);
        } else {
            view = convertView;
        }

        News newsItem = getItem(position);
        TextView title = (TextView)view.findViewById(R.id.Title);
        CharSequence cs = Html.fromHtml(newsItem.getTitle());
        title.setText(cs);
        TextView publisher = (TextView)view.findViewById(R.id.Publisher);
        publisher.setText(newsItem.getPublisher());
        ImageView photo = (ImageView)view.findViewById(R.id.Picture);
        Picasso.with(getContext()).load(newsItem.getPhotoUrl()).resize(300, 200).into(photo);
        return view;

    }

}
