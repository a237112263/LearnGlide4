package com.example.heduohao.learnglide4;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.net.URI;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private String thumUrl = "http://static.fdc.com.cn/avatar/sns/1486172566083.png@233w_160h_20q";
    private String bigUrl = "http://static.fdc.com.cn/avatar/sns/1486172566083.png";
    private RequestOptions requestOptions;
    private Button clearDiskCacheBtn;
    private Button clearMemoryCacheBtn;
    private Button catchSizeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestOptions = new RequestOptions()
                .error(R.drawable.deafault)
                .placeholder(R.drawable.deafault)
        ;
        imageView =  findViewById(R.id.iv_thum);
        clearDiskCacheBtn =  findViewById(R.id.clear_disk_cache_btn);
        clearMemoryCacheBtn =  findViewById(R.id.clear_memory_cache_btn);
        catchSizeBtn=  findViewById(R.id.catch_size_btn);

        Glide.with(this)
                .load(thumUrl)
                .apply(requestOptions)
                .into(imageView);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(MainActivity.this, ImageViewActivity.class);
                startActivity(intent3, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, imageView, "share").toBundle());
            }
        });


        clearMemoryCacheBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlideCatchUtil.getInstance().clearCacheMemory()) {
                    showToast("清除Glide内存缓存成功");
                } else {
                    showToast("清除Glide内存缓存失败");
                }
            }
        });

        clearDiskCacheBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlideCatchUtil.getInstance().clearCacheDiskSelf()) {
                    showToast("清除Glide磁盘缓存成功，Glide自带方法");
                } else {
                    showToast("清除Glide磁盘缓存失败，Glide自带方法");
                }
            }
        });

        catchSizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Glide磁盘缓存大小:" + GlideCatchUtil.getInstance().getCacheSize());
            }
        });
    }

    class ShareTask extends AsyncTask<String, Void, File> {
        private final Context context;

        public ShareTask(Context context) {
            this.context = context;
        }

        @Override
        protected File doInBackground(String... params) {
            String url = params[0]; // should be easy to extend to share multiple images at once
            try {
                return Glide
                        .with(context)
                        .downloadOnly()
                        .load(url)
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get() // needs to be called on background thread
                        ;
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                Glide.with(context)
                        .load(bigUrl)
                        .apply(requestOptions)
                        .into(imageView);
                return;
            }
            URI uri = result.toURI();
            Glide.with(context)
                    .load(bigUrl)
                    .thumbnail(Glide.with(context)
                            .load(thumUrl))
                    .into(imageView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }
}
