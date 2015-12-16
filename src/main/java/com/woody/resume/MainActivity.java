package com.woody.resume;

import android.content.res.AssetManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woody.bean.Function;
import com.woody.bean.Project;
import com.woody.bean.Technology;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import woody.utils.animation.AnimUtils;

public class MainActivity extends BaseActivity {
    private LinearLayout mExpriPreViewll;
    private LinearLayout mItemEdull;
    List<Project> mProjects = null;
    private LinearLayout mExpriDetailll;
    ArrayList<View> mExpreItems = new ArrayList<View>();

    @Override
    protected int getlayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        initProjectData();
    }

    @Override
    protected void initView() {

//       项目经验
        initExpriView();
        initExpriDetail(0);
//        教育经验
        initEduView();
    }

    private void initExpriDetail(final int index) {
        mExpriDetailll.removeAllViews();
        View inflate = getLayoutInflater().inflate(R.layout.layout_item_despri, null);
        TextView mDesTv = (TextView) inflate.findViewById(R.id.tv_des);
        TextView mEnviromentTv = (TextView) inflate.findViewById(R.id.tv_envir);
        Project project = mProjects.get(index);
//      init Environment
        mEnviromentTv.setText(project.getEnvironment());
//      init Description
        mDesTv.setText(project.getDescribe());

        inflate.findViewById(R.id.tv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnimUtils instance = AnimUtils.getInstance();
                instance.setPivotX(mExpriPreViewll, mExpriPreViewll.getWidth(), mExpriPreViewll.getHeight());
                instance.translateX(mExpriPreViewll, 2000, 0, -mExpriPreViewll.getWidth());
                instance.setPivotX(mExpriDetailll, 0, 0);
                instance.translateX(mExpriDetailll, 2000, 0, -mExpriPreViewll.getWidth());
                initExpriDetailMore(index);
//                AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
//                alphaAnimation.setDuration(1000);
//                TranslateAnimation translateAnimation = new TranslateAnimation(mExpriPreViewll.getLayoutParams().width, 0, 0, 0);
//                translateAnimation.setDuration(1000);
//                AnimationSet animationSet = new AnimationSet(true);
//                animationSet.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        mExpriPreViewll.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//                animationSet.addAnimation(alphaAnimation);
//                animationSet.addAnimation(translateAnimation);
//                mExpriPreViewll.startAnimation(animationSet);
            }
        });

        mExpriDetailll.addView(inflate);
    }

    //  显示详情
    private void initExpriDetailMore(int index) {
        mExpriDetailll.removeAllViews();
        View inflate = getLayoutInflater().inflate(R.layout.layout_item_expri_detail, null);
        TextView mProjectName = (TextView) inflate.findViewById(R.id.ll_project_name);

        LinearLayout mFunctionll = (LinearLayout) inflate.findViewById(R.id.ll_function);
        LinearLayout mTechll = (LinearLayout) inflate.findViewById(R.id.ll_tech);
        if (!mProjects.isEmpty() && mProjects.get(index) != null) {
            Project project = mProjects.get(index);
            mProjectName.setText(project.getName());
//          项目描述
            ArrayList<Function> functions = project.getFunction();
            for (int i = 0; i < functions.size(); i++) {
                View functionflate = getLayoutInflater().inflate(R.layout.layout_item_simple_text, null);
                TextView mfunctionTv = (TextView) functionflate.findViewById(R.id.textView2);
                mfunctionTv.setText(functions.get(i).getName());
                mFunctionll.addView(functionflate);
            }

//          技术要点
            ArrayList<Technology> technologies = project.getTechnology();
            for (int i = 0; i < technologies.size(); i++) {
                View technoflate = getLayoutInflater().inflate(R.layout.layout_item_simple_text, null);
                TextView technologyTv = (TextView) technoflate.findViewById(R.id.textView2);
                technologyTv.setText(technologies.get(i).getTechName());
                mTechll.addView(technoflate);
            }
        }
        mExpriDetailll.addView(inflate);
    }

    /**
     * 初始化Education View
     */
    private void initEduView() {
        String[] eduName = getResources().getStringArray(R.array.item_educated);
        String[] eduTime = getResources().getStringArray(R.array.item_educated_time);
        if (eduName.length != 0 && eduTime.length != 0 && eduName.length == eduTime.length) {
            for (int i = 0; i < eduName.length; i++) {
                View inflate = getLayoutInflater().inflate(R.layout.layout_item_edu, null);
                TextView expreName = (TextView) inflate.findViewById(R.id.tv_edu_name);
                TextView expreStartTimeTv = (TextView) inflate.findViewById(R.id.tv_edu_starttime);
                ImageView expreIv = (ImageView) inflate.findViewById(R.id.iv_expri);
//              init baseView
                RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                para.leftMargin = 48;
                expreName.setLayoutParams(para);
                expreName.setText(eduName[i]);
                expreStartTimeTv.setText(eduTime[i]);
                mItemEdull.addView(inflate);
                inflate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        initExpriDetail(i);
                    }
                });
            }
        }


    }

    private void initProjectData() {
        AssetManager asset = this.getAssets();
        InputStream input = null;
        try {
            input = asset.open("expri_detail.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            mProjects = handler.getDataList();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void findView() {
        mExpriPreViewll = (LinearLayout) findViewById(R.id.item_exper);
        mItemEdull = (LinearLayout) findViewById(R.id.item_edu);
        mExpriDetailll = (LinearLayout) findViewById(R.id.item_expri_info);
    }

    private void initExpriView() {
        final String[] experiaName = getResources().getStringArray(R.array.item_experia);
        final String[] experiaTime = getResources().getStringArray(R.array.item_experia_time);
        mExpreItems.clear();
        if (experiaName.length != 0 && experiaTime.length != 0 && experiaName.length == experiaTime.length) {
            for (int i = 0; i < experiaName.length; i++) {
                View inflate = getLayoutInflater().inflate(R.layout.layout_item_exper, null);
                mExpreItems.add(inflate);
                TextView expreName = (TextView) inflate.findViewById(R.id.tv_expre_name);
                TextView expreStartTime = (TextView) inflate.findViewById(R.id.tv_expre_starttime);
                final ImageView expreIv = (ImageView) inflate.findViewById(R.id.expri_iv);
                RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                para.leftMargin = 48;
                expreName.setLayoutParams(para);
                expreName.setText(experiaName[i]);
                expreStartTime.setText(experiaTime[i]);
                expreIv.setSelected(false);
                if (i == 0) {
                    expreIv.setSelected(true);
                }

//                final int finalI = i;
//                inflate.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        expreIv.setSelected(true);
//                        initExpriDetailMore(finalI);
//                    }
//                });
                mExpriPreViewll.addView(inflate);
            }
//          单选
            setRadioSelected();
        }
    }

    private void setRadioSelected() {
        for (int i = 0; i < mExpreItems.size(); i++) {
            final View _item = mExpreItems.get(i);
            final int finalI = i;
            _item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _item.findViewById(R.id.expri_iv).setSelected(true);
                    initExpriDetail(finalI);
                    for (View j : mExpreItems) {
                        if (j != _item) {
                            j.findViewById(R.id.expri_iv).setSelected(false);
                        }
                    }
                }
            });
        }
    }
}
