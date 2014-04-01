package jp.bootcamp.arai;

import com.actionbarsherlock.app.SherlockActivity;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsActivity extends SherlockActivity {
    
    private Activity activity;
    
    @SuppressWarnings("unused")
    private static final String TAG = NewsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        activity = this;
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        TextView newsitle = (TextView)findViewById(R.id.NewsTitle);
        TextView userName = (TextView)findViewById(R.id.userName);
        ImageView photo = (ImageView)findViewById(R.id.photo);
        TextView photoDetail = (TextView)findViewById(R.id.photoDetail);
        newsitle.setText(intent.getStringExtra("key.title"));
        userName.setText(intent.getStringExtra("key.p"));
        photoDetail.setText(intent.getStringExtra("key.photoDetail"));
        Picasso.with(this).load(intent.getStringExtra("key.photoUrl")).into(photo);
    }
    
    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home :
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        default :
            return super.onOptionsItemSelected(item);
        }
    }
}
