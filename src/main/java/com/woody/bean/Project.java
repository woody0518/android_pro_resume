package com.woody.bean;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private String mName;
    private String mEnvironment;
    private String mDescribe;
    private ArrayList<Function> mFunction;
    private ArrayList<Technology> mTechnology;

    public Project() {
        super();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getEnvironment() {
        return mEnvironment;
    }

    public void setEnvironment(String environment) {
        mEnvironment = environment;
    }

    public String getDescribe() {
        return mDescribe;
    }

    public void setDescribe(String describe) {
        mDescribe = describe;
    }

    public ArrayList<Function> getFunction() {
        return mFunction;
    }

    public void setFunction(ArrayList<Function> function) {
        mFunction = function;
    }

    public ArrayList<Technology> getTechnology() {
        return mTechnology;
    }

    public void setTechnology(ArrayList<Technology> technology) {
        mTechnology = technology;
    }
}
