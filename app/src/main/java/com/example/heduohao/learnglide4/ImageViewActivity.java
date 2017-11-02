package com.example.heduohao.learnglide4;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.jingewenku.abrahamcaijin.commonutil.DensityUtils;

/**
 * 作者：liutengfei on 2017/10/19.
 * 邮箱：371116959@qq.com
 * 描述：
 */

public class ImageViewActivity extends AppCompatActivity{
    private ImageView bigImageIv;
    private String thumUrl = "http://static.fdc.com.cn/avatar/sns/1486172566083.png@233w_160h_20q";
    private String bigUrl = "http://static.fdc.com.cn/avatar/sns/1486172566083.png";
    private RequestOptions requestOptions;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);
        progressBar=findViewById(R.id.progressBar);
        requestOptions = new RequestOptions()
                .error(R.drawable.deafault)
                .placeholder(R.drawable.deafault)
        ;
        bigImageIv=findViewById(R.id.iv_big);

        WindowManager wm = (WindowManager)
                getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int imgWith=DensityUtils.dp2px(this,80);
        final float beishu=(float) width/ imgWith;

        Glide.with(this)
                .load(bigUrl)
                .thumbnail(Glide.with(this)
                        .load(thumUrl).apply(requestOptions).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.VISIBLE);
                                return true;
                            }
                        }))
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (dataSource.equals(DataSource.REMOTE)){
                            bigImageIv.setImageDrawable(resource);
                            new Handler().postDelayed(new Runnable(){
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    ObjectAnimator anim = ObjectAnimator//
                                            .ofFloat(bigImageIv, "zhy",1.0F,  beishu)//
                                            .setDuration(2000);//
                                    anim.start();
                                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                                    {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator animation)
                                        {
                                            float cVal = (Float) animation.getAnimatedValue();
                                            bigImageIv.setScaleX(cVal);
                                            bigImageIv.setScaleY(cVal);
                                        }
                                    });
                                }
                            }, 2000);

                        }else {
                            progressBar.setVisibility(View.GONE);
                            bigImageIv.setLayoutParams(
                                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                            bigImageIv.setImageDrawable(resource);
                        }
                        return true;
                    }
                })
                .into(bigImageIv);
    }
}
