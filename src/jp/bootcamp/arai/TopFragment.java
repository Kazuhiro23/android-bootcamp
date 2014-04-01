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
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public class TopFragment extends SherlockFragment {

    private ListView mListView;
    private NewsArrayAdapter newsArrayAdapter;

    public static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        AsyncJob asyncJob = new AsyncJob();
        asyncJob.execute("http://pecolly.jp/photos/api/detail_list?photolistKey=popular_json&type=popular&option=detail&index=0&offsetId=8761&limit=10&offset=1&order=desc");


        View view = inflater.inflate(R.layout.fragment_top, container, false);
        mListView = (ListView)view.findViewById(R.id.NewsList);
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
                URL url = new URL(params[0]);
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
            Log.v("tag", data);
            JSONObject jsonObj;
            JSONArray result;
            ArrayList<News> list = new ArrayList<News>();
            try {
                jsonObj = new JSONObject(data);
                result = jsonObj.getJSONArray("resultObject");
                if (result.length() < 2) {
                    AsyncJob asyncJob = new AsyncJob();
                    asyncJob.execute("http://de-tox.jp/api/feed/latest");
                    result = jsonObj.getJSONArray("latestFeed");
                    for(int i = 0; i < result.length(); i++) {
                        JSONObject pecoly = result.getJSONObject(i);
                        News news = new News();
                        news.setTitle(pecoly.getString("content"));
                        news.setPublisher(pecoly.getString("postUserName"));
                        list.add(news);
                    }
                } else {
                    for(int i = 0; i < result.length(); i++) {
                        JSONObject pecoly = result.getJSONObject(i);
                        News news = new News();
                        news.setTitle(pecoly.getString("photoTitle"));
                        news.setPhotoUrl(pecoly.getString("photoUrl"));
                        news.setPublisher(pecoly.getString("userName"));
                        news.setPhotoDetail(pecoly.getString("photoDetail"));
                        
                        list.add(news);
                    }
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            newsArrayAdapter = new NewsArrayAdapter(getActivity(), list);
            if (mListView == null) {
                mListView = (ListView)getView().findViewById(R.id.NewsList);
            }           
            mListView.setAdapter(newsArrayAdapter);
            registerForContextMenu(mListView);
            mListView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), NewsActivity.class);
                    News item = (News)mListView.getItemAtPosition(position);
                    intent.putExtra("key.title", item.getTitle());
                    intent.putExtra("key.photoDetail", item.getPhotoDetail());
                    intent.putExtra("key.publisher", item.getPublisher());
                    intent.putExtra("key.photoUrl", item.getPhotoUrl());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        }

    }

}
