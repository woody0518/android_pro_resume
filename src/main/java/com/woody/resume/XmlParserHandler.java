package com.woody.resume;

import com.woody.bean.Function;
import com.woody.bean.Project;
import com.woody.bean.Technology;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XmlParserHandler extends DefaultHandler {
    Project mProject;
    private ArrayList<Project> mProvinceList = new ArrayList<Project>();
    private Function mFunction;
    private Technology mTechnology;

    public XmlParserHandler() {
    }

    public List<Project> getDataList() {
        return mProvinceList;
    }

    String mFunctionItem;
    String mTecItem;

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        switch (localName) {
            case "root":
                break;
            case "project":
                mProject = new Project();
                mProject.setFunction(new ArrayList<Function>());
                mProject.setTechnology(new ArrayList<Technology>());
                mProject.setName(attributes.getValue(0));
                break;
            case "environment":
                mProject.setEnvironment(attributes.getValue(0));
                break;

            case "describe":
                mProject.setDescribe(attributes.getValue(0));
                break;

            case "function":
                mFunction = new Function();
                mFunction.setName(attributes.getValue(0));
                break;

            case "technology":
                mTechnology = new Technology();
                mTechnology.setTechName(attributes.getValue(0));
                break;
        }
    }


    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        switch (qName) {
            case "project":
                mProvinceList.add(mProject);
                break;

            case "function":
                mProject.getFunction().add(mFunction);
                break;

            case "technology":
                mProject.getTechnology().add(mTechnology);
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
    }

}
