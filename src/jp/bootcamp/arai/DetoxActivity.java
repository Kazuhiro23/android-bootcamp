package jp.bootcamp.arai;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class DetoxActivity extends SherlockActivity {

    
    @SuppressWarnings("unused")
    private static final String TAG = NewsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detox);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        TextView newstitle = (TextView)findViewById(R.id.postDetail); 
        MovementMethod movementmethod = LinkMovementMethod.getInstance();
        newstitle.setMovementMethod(movementmethod);
        String str = intent.getStringExtra("key.title");        
        String result = str.replaceAll("/hashtag", "http://de-tox.jp/hashtag");
        CharSequence cs = Html.fromHtml(result);
        newstitle.setText(cs);
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
