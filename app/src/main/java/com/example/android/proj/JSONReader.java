package com.example.android.proj;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to read quiz data
 */
public class JSONReader {
    class QA{
        public String mQuestion;
        public String[] mOptions = new String[4];
        public String mHint;
        public String mAnswer;
        public int mOpt;
    };

    private HashMap<String,QA[]> mMap = new HashMap<String,QA[]>();
    private String[] mSubjects;
    private String mName;
    private String mEmail;

    boolean loadString(String string){

        try {
            JSONObject object = new JSONObject(string);
            mName = object.getString("Teacher");
            mEmail = object.getString("Email");
            JSONArray array = object.getJSONArray("Subjects");
            int numSubject = array.length();
            mSubjects = new String[numSubject];
            for(int i=0;i<numSubject;++i){
                mSubjects[i] = array.getString(i);
                JSONArray jsonArray = object.getJSONArray(mSubjects[i]);
                QA[] qaArray = new QA[jsonArray.length()];
                for(int j=0;j<jsonArray.length();++j){
                    JSONObject obj = jsonArray.getJSONObject(j);
                    qaArray[j]=new QA();
                    qaArray[j].mAnswer = obj.getString("Answer");
                    qaArray[j].mQuestion = jsonArray.getJSONObject(j).getString("Question");
                    qaArray[j].mHint = jsonArray.getJSONObject(j).getString("Hint");
                    qaArray[j].mOpt = jsonArray.getJSONObject(j).getInt("Opt");
                    qaArray[j].mOptions[0] = jsonArray.getJSONObject(j).getString("Opt0");
                    qaArray[j].mOptions[1] = jsonArray.getJSONObject(j).getString("Opt1");
                    qaArray[j].mOptions[2] = jsonArray.getJSONObject(j).getString("Opt2");
                    qaArray[j].mOptions[3] = jsonArray.getJSONObject(j).getString("Opt3");

                }
                mMap.put(mSubjects[i],qaArray);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    boolean loadFile(String aFileStr){
        try {
            InputStream ifs = new FileInputStream(aFileStr);
            int numBytes = ifs.available();
            byte bytes[] = new byte[numBytes];
            ifs.read(bytes);
            String string = new String(bytes,"UTF-8");

            return loadString(string);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    String[] getSubjects(){
        return mSubjects;
    }

    String getName(){
        return mName;
    }

    String getEmail(){
        return mEmail;
    }

    String getQuestion(String subject,int qNum){
        QA[] qa = mMap.get(subject);
        if(qa!=null && qNum < qa.length)
            return qa[qNum].mQuestion;
        return null;
    }

    int getQuestionCnt(String subject){
        QA[] qas =  mMap.get(subject);
        if(qas!=null){
            return qas.length;
        }
        return 0;
    }

    String getAnswer(String subject,int qNum){
        QA[] qa = mMap.get(subject);
        if(qa!=null && qNum < qa.length)
            return qa[qNum].mAnswer;
        return null;
    }

    int getAnswerOpt(String subject,int qNum){
        QA[] qa = mMap.get(subject);
        if(qa!=null && qNum < qa.length)
            return qa[qNum].mOpt;
        return -1;
    }

    String getHint(String subject,int qNum){
        QA[] qa = mMap.get(subject);
        if(qa!=null && qNum < qa.length)
            return qa[qNum].mHint;
        return null;
    }

    String getOption(String subject, int qNum,int opt){
        QA[] qa = mMap.get(subject);
        if(qa!=null && qNum < qa.length && opt<4)
            return qa[qNum].mOptions[opt];
        return null;
    }

    Bundle createBundle(){
        Bundle ret = new Bundle();
        ret.putString("Teacher",mName);
        ret.putString("Email",mEmail);
        ret.putStringArray("Subjects",mSubjects);
        for(int i=0;i<mSubjects.length;++i){
            Bundle subject = new Bundle();
            QA[] qas = mMap.get(mSubjects[i]);
            subject.putInt("qaNum",qas.length);
            for(int j=0;j<qas.length;++j){
                Bundle qa = new Bundle();
                qa.putString("Question",qas[j].mQuestion);
                qa.putString("Opt0",qas[j].mOptions[0]);
                qa.putString("Opt1",qas[j].mOptions[1]);
                qa.putString("Opt2",qas[j].mOptions[2]);
                qa.putString("Opt3",qas[j].mOptions[3]);
                qa.putInt("Opt",qas[j].mOpt);
                qa.putString("Answer",qas[j].mAnswer);
                qa.putString("Hint",qas[j].mHint);

                subject.putBundle("qa"+j,qa);
            }
            ret.putBundle(mSubjects[i],subject);

        }
        return ret;
    }

    public void loadBundle(Bundle bundle){

        mName = bundle.getString("Teacher");
        mEmail = bundle.getString("Email");
        mSubjects = bundle.getStringArray("Subjects");
        for(int i=0;i<mSubjects.length;++i){
            Bundle subject = bundle.getBundle(mSubjects[i]);
            QA[] qas = new QA[subject.getInt("qaNum")];
            for(int j=0;j<qas.length;++j){
                Bundle qa = subject.getBundle("qa"+j);
                qas[j] = new QA();
                qas[j].mQuestion = qa.getString("Question");
                qas[j].mOptions[0] = qa.getString("Opt0");
                qas[j].mOptions[1] = qa.getString("Opt1");
                qas[j].mOptions[2] = qa.getString("Opt2");
                qas[j].mOptions[3] = qa.getString("Opt3");
                qas[j].mOpt = qa.getInt("Opt");
                qas[j].mAnswer = qa.getString("Answer");
                qas[j].mHint = qa.getString("Hint");
            }
            mMap.put(mSubjects[i],qas);

        }
    }

}
