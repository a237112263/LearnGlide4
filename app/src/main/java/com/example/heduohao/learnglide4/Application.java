package com.example.heduohao.learnglide4;

/**
 * 作者：liutengfei on 2017/10/19.
 * 邮箱：371116959@qq.com
 * 描述：
 */

public class Application extends android.app.Application{
    public static Application instance;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}
