package jp.bootcamp.arai;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

public class EntertainmentFragment extends SherlockFragment {

    private PullToRefreshListView mListView;
    private DetoxArrayAdapter newsArrayAdapter;

    public static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        new AsyncJob().execute();


        View view = inflater.inflate(R.layout.fragment_entaertanment, container, false);
        mListView = (PullToRefreshListView)view.findViewById(R.id.NewsList);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        // ContextMenuを設定
        getActivity().getMenuInflater().inflate(R.menu.context_menu_main, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.TwtterListItem && item.getItemId() != R.id.FacebookListItem) {
            return false;
        }
        ContextMenuInfo menuInfo = item.getMenuInfo();
        AdapterContextMenuInfo adapterInfo = (AdapterContextMenuInfo) menuInfo;

        int position = adapterInfo.position;

        // ListViewから長押しされたリストアイテムを取得します
        News news = (News) mListView.getItemAtPosition(position);
        // ListViewからセットされているAdapterを取得します
        NewsArrayAdapter adapter = (NewsArrayAdapter) mListView.getAdapter();

        if (item.getItemId() == R.id.TwtterListItem) {
            adapter.remove(news);
        } else if (item.getItemId() == R.id.FacebookListItem) {
            adapter.add(news);
        }
        adapter.notifyDataSetChanged();
        return true;
    }

    class AsyncJob extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            HttpURLConnection connection = null; 

            try {

                URL url = new URL("http://de-tox.jp/api/feed/latest");
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                StringBuilder builder = new StringBuilder();
                while(true) {
                    byte[] line = new byte[1024];
                    int size = inputStream.read(line);
                    if (size <= 0) {
                        break;
                    }
                    builder.append(new String(line, "UTF-8"));
                }
                result = builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String data) {
            JSONObject jsonObj;
            JSONArray result;
            ArrayList<News> list = new ArrayList<News>();
            try {
                jsonObj = new JSONObject(data);
                result = jsonObj.getJSONArray("latestFeed");
                for(int i = 0; i < result.length(); i++) {
                    JSONObject pecoly = result.getJSONObject(i);
                    News news = new News();
                    news.setTitle(pecoly.getString("content"));
                    news.setPublisher(pecoly.getString("postUserName"));
                    list.add(news);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            newsArrayAdapter = new DetoxArrayAdapter(getActivity(), list);
            if (mListView == null) {
                mListView = (PullToRefreshListView)getView().findViewById(R.id.NewsList);
            }
            mListView.setAdapter(newsArrayAdapter);

            mListView.setOnRefreshListener(new OnRefreshListener() {

                @Override
                public void onRefresh() { 
                    
                    new AsyncJob().execute();
                }
            });

            registerForContextMenu(mListView);
            mListView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), DetoxActivity.class);
                    News item = (News)mListView.getItemAtPosition(position);
                    intent.putExtra("key.title", item.getTitle());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        }

    }

}
