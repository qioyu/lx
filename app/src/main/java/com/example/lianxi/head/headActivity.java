package com.example.lianxi.head;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.lianxi.R;
import com.example.lianxi.poem.Service_poem;
import com.example.lianxi.poem.poemdata;

import okhttp3.MediaType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class headActivity extends AppCompatActivity implements View.OnClickListener {

    private Button headButton;
    private ImageView headImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head);
        initView();


    }



    private void initView() {
        headButton = (Button) findViewById(R.id.headButton);
        headImage = (ImageView) findViewById(R.id.headImage);

        headButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headButton:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        ContentResolver resolver = getContentResolver();
        Cursor query = resolver.query(uri, null, null, null, null);
        query.moveToFirst();
        String path = query.getString(query.getColumnIndex("_DATA"));
        System.out.println(path);
        headImage.setImageURI(uri);
    }
}
