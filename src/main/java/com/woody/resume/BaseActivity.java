package com.woody.resume;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2015/11/28.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getlayoutRes());
        findView();
        initData();
        initView();

    }

    protected abstract int getlayoutRes();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void findView();

}
