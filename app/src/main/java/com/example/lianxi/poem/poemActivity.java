package com.example.lianxi.poem;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.lianxi.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

public class poemActivity extends AppCompatActivity {

    private RecyclerView poem_RecyclerView;
    private poem_Adapter adapter;
    private String path = "https://api.apiopen.top/";
    private SwipeRefreshLayout poem_swip;
    int page = 1;
    private SQLiteDatabase db;
    private MySql sql;
    private List<poemdata.ResultBean> result;


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            result = (List<poemdata.ResultBean>) msg.obj;
            adapter.refresh(result);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem);
        initView();
        initdata();
        initRetrofit(page);

        poem_swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        initRetrofit(page);
                        poem_swip.setRefreshing(false);

                    }
                },2000);

            }
        });


    }

    private void initRetrofit(final int page) {
        new Thread() {
            @Override
            public void run() {
                super.run();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(path)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Service_poem service = retrofit.create(Service_poem.class);
                MediaType parse = MediaType.parse("application/otcet-stream");

                Call<poemdata> call = service.getcall(page, 10);
                try {
                    Response<poemdata> execute = call.execute();
                    for (int i = 0; i < execute.body().getResult().size(); i++) {
                        poemdata.ResultBean resultBean = execute.body().getResult().get(i);
                        db.execSQL("insert into poem values (?,?,?)", new String[]{resultBean.getTitle(), resultBean.getContent(), resultBean.getAuthors()});
                    }
                    Message message = handler.obtainMessage();
                    message.obj = execute.body().getResult();
                    handler.sendMessage(message);
                } catch (IOException e) {
                    sqlselect();
                }
            }
        }.start();
    }

    private void initdata() {
        adapter = new poem_Adapter();
        poem_RecyclerView.setAdapter(adapter);
        sql = new MySql(this);
        db = sql.getReadableDatabase();
    }

    private void initView() {
        poem_RecyclerView = (RecyclerView) findViewById(R.id.poem_RecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        poem_RecyclerView.setLayoutManager(manager);
        poem_swip = (SwipeRefreshLayout) findViewById(R.id.poem_swip);
    }

    //接口请求失败或返回数据异常,从本地加载内容
    private void sqlselect() {
        Cursor cursor = db.rawQuery("select * from poem", null);
        System.out.println(cursor.getCount());
        result = new ArrayList<>();
        while (cursor.moveToNext()) {
            poemdata.ResultBean bean = new poemdata.ResultBean();
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String count = cursor.getString(cursor.getColumnIndex("count"));
            String authors = cursor.getString(cursor.getColumnIndex("authors"));
            bean.setTitle(title);
            bean.setContent(count);
            bean.setAuthors(authors);
            result.add(bean);
        }
        adapter.refresh(result);
    }
}
