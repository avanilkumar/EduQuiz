package com.example.android.proj;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Animation.AnimationListener {

    /****************************************
     * file content stored in this variable
     ***************************************/
    //private String mQuizStr;

    /***************************************************
     * Subject String and question screen related params
     * JSON object params
     ***************************************************/
    private String mSubjectStr;
    //private int mSubjectLen;
    private int mCurQuestion;
    //public String mCurHint;
    //private String mCurAnswer;
    //private int mCurOpt;
    //private String mTeacher;
    //private String mFeedback;
    private JSONReader mData = new JSONReader();

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    /**************************
     * loads default Quiz file
     *************************/
    public void defaultQuizLoad() {
        try {
            InputStream inputStream = getAssets().open(getString(R.string.default_quiz));
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            //mQuizStr = new String(buffer, "UTF-8");
            mCurQuestion = 0;
            mSubjectStr = "";
            mData.loadString(new String(buffer, "UTF-8"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateNavigationMenu() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        SubMenu sub = navigationView.getMenu().addSubMenu(getString(R.string.Subjects));


        TextView t = (TextView) navigationView.getHeaderView(0).findViewById(R.id.teacher);
        if (t != null) {
            t.setText("");
        }
        //mTeacher = mFeedback = "";

        t = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);
        if (t != null) {
            t.setText("");
        }

        // JSONObject object = new JSONObject(mQuizStr);
        //String str = object.getString(getString(R.string.Teacher));
        //if (str != null) {
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.teacher)).setText(mData.getName());
        //     mTeacher = str;
        //}
        //str = object.getString(getString(R.string.Email));
        //if (str != null) {
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.email)).setText(mData.getEmail());
        //mFeedback = str;
        //}


        //JSONArray array = object.getJSONArray(getString(R.string.Subjects));
        //int cnt = array.length();
        int cnt = mData.getSubjects().length;
        for (int i = 0; i < cnt; ++i) {
            //str = array.getString(i);
            //if (str != null) {
                MenuItem item = sub.add(R.id.SubjectGroup,i+1,i,mData.getSubjects()[i]);

               // if (i == 0 && (mSubjectStr==null || mSubjectStr.isEmpty())) {
              //      mSubjectStr = str;

                //}
            //}
        }


        sub.setGroupCheckable(R.id.SubjectGroup,true,true);

        sub = navigationView.getMenu().addSubMenu("Communication");
        sub.add("Feedback").setIcon(R.drawable.ic_menu_send);


    }

    public void setQuestionScreen() {
        // JSONObject object = new JSONObject(mQuizStr);
        //JSONArray array = object.getJSONArray(mSubjectStr);
        ((TextView)findViewById(R.id.questionScreen).findViewById(R.id.answer)).setText("");
        //mSubjectLen = array.length();
        findViewById(R.id.bgimage).setAlpha(0.0f);
        if(mCurQuestion < mData.getQuestionCnt(mSubjectStr)){
            String str;
            //JSONObject curObject;
            //curObject =array.getJSONObject(mCurQuestion);


            //if(curObject!=null){
                //mCurAnswer = curObject.getString(getString(R.string.Answer));
                //mCurHint = curObject.getString(getString(R.string.Hint));
                //mCurOpt = curObject.getInt(getString(R.string.Opt));
                //str = curObject.getString(getString(R.string.Question));
                str = mData.getQuestion(mSubjectStr,mCurQuestion);
                ((TextView)findViewById(R.id.questionScreen).findViewById(R.id.question)).setText(str);

                ((RadioGroup)findViewById(R.id.questionScreen).findViewById(R.id.options)).clearCheck();

                //str = curObject.getString(getString(R.string.Opt0));
                str = mData.getOption(mSubjectStr,mCurQuestion,0);
                ((RadioButton)findViewById(R.id.questionScreen).findViewById(R.id.op1)).setText(str);

                //str = curObject.getString(getString(R.string.Opt1));
                str = mData.getOption(mSubjectStr,mCurQuestion,1);
                ((RadioButton)findViewById(R.id.questionScreen).findViewById(R.id.op2)).setText(str);

                //str = curObject.getString(getString(R.string.Opt2));
                str = mData.getOption(mSubjectStr,mCurQuestion,2);
                ((RadioButton)findViewById(R.id.questionScreen).findViewById(R.id.op3)).setText(str);

              //  str = curObject.getString(getString(R.string.Opt3));
                str = mData.getOption(mSubjectStr,mCurQuestion,3);
                ((RadioButton)findViewById(R.id.questionScreen).findViewById(R.id.op4)).setText(str);


               findViewById(R.id.bgimage).setAlpha(0.0f);
            //}
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String str = ((MainActivity)view.getContext()).mCurHint;
                int qNum = ((MainActivity)view.getContext()).mCurQuestion;
                String sub = ((MainActivity)view.getContext()).mSubjectStr;
                String str = ((MainActivity)view.getContext()).mData.getHint(sub,qNum);
                if(str==null)str="";
                Snackbar.make(view, str, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setBackgroundResource(R.drawable.bgimage);

        if(savedInstanceState==null){
            mCurQuestion=0;
            defaultQuizLoad();
            mSubjectStr = mData.getSubjects()[0];
            ((Button)findViewById(R.id.questionScreen).findViewById(R.id.okNxt)).setText(getText(R.string.ok));
        }else{
            mCurQuestion = savedInstanceState.getInt("questionNum");//outState.putInt("questionNum",mCurQuestion);
            //mCurOpt = savedInstanceState.getInt(getString(R.string.Opt));//outState.putInt(getString(R.string.Opt),mCurOpt);
            //mQuizStr = savedInstanceState.getString("file");//outState.putString("file",mQuizStr);
            mSubjectStr = savedInstanceState.getString("subject");//outState.putString("subject",mSubjectStr);
            String str = savedInstanceState.getString("okNxtButton");
            ((Button)findViewById(R.id.okNxt)).setText(str);
            if(str.equals(getString(R.string.Next))){
                ((TextView)findViewById(R.id.questionScreen).findViewById(R.id.answer)).setText(mData.getAnswer(mSubjectStr,mCurQuestion));
            }
            mData = new JSONReader();
            mData.loadBundle(savedInstanceState.getBundle("dat"));
        }

        updateNavigationMenu();
        setQuestionScreen();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private static final int PICKFILE_RESULT_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICKFILE_RESULT_CODE){
            if (resultCode == RESULT_OK) {
                String FilePath = data.getData().getPath();
                if(FilePath!=null && !FilePath.isEmpty()){
                    //FileInputStream fis = new FileInputStream(FilePath);
                    //byte[] buffer = new byte[fis.available()];
                    //fis.read(buffer);
                    //mQuizStr = new String(buffer, "UTF-8");
                    mData = new JSONReader();
                    mData.loadFile(FilePath);
                    mCurQuestion = 0;
                    mSubjectStr = mData.getSubjects()[0];
                    ((Button)findViewById(R.id.questionScreen).findViewById(R.id.okNxt)).setText("OK");
                    updateNavigationMenu();
                    setQuestionScreen();
                }

                //FilePath is your file as a string
            }else{
                Toast.makeText(this,getString(R.string.openFail),Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.reset) {
            ((Button)findViewById(R.id.questionScreen).findViewById(R.id.okNxt)).setText("OK");
            mCurQuestion = 0;
            setQuestionScreen();
            return true;
        }else if(id == R.id.open){
            Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
            fileintent.setType("gagt/sdf");
            try {
                startActivityForResult(fileintent, PICKFILE_RESULT_CODE);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this,getString(R.string.openFail),Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(item.getTitle().toString().equals("Feedback")){

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{mData.getEmail()});
            i.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            i.putExtra(Intent.EXTRA_TEXT   , "Dear "+mData.getName());
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }else {

            mSubjectStr = item.getTitle().toString();
            mCurQuestion = 0;
            ((Button) findViewById(R.id.questionScreen).findViewById(R.id.okNxt)).setText("OK");
            setQuestionScreen();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.android.proj/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.android.proj/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt(getString(R.string.Opt),mCurOpt);
        //outState.putString("file",mQuizStr);
        outState.putString("subject",mSubjectStr);
        String str = new String(((Button)findViewById(R.id.okNxt)).getText().toString());
        outState.putString("okNxtButton",str);
        outState.putInt("questionNum",mCurQuestion);
        Bundle dat = mData.createBundle();
        outState.putBundle("dat",dat);

    }

    public void onOkNxt(View view) {
        Button b = (Button)view;
            String str = mData.getAnswer(mSubjectStr,mCurQuestion);
            ((TextView)findViewById(R.id.questionScreen).findViewById(R.id.answer)).setText(str);

            if(b.getText().toString().equals(getText(R.string.ok))){

                int[] opts = new int[]{R.id.op1,R.id.op2,R.id.op3,R.id.op4};
                int id = ((RadioGroup)findViewById(R.id.questionScreen).findViewById(R.id.options)).getCheckedRadioButtonId();
                ImageView bgImge = (ImageView) findViewById(R.id.bgimage);
                AlphaAnimation animation = new AlphaAnimation(0.0f,1.0f);
                animation.setAnimationListener(this);
                animation.setDuration(2500);
                animation.setInterpolator(new LinearInterpolator());

                b.setText(getString(R.string.Next));
                int opt = mData.getAnswerOpt(mSubjectStr,mCurQuestion);
                if(opts[opt]==id){//correct
                    bgImge.setImageResource(R.drawable.correct);
                    bgImge.setAlpha(1.0f);
                    bgImge.setAnimation(animation);
                    if(mCurQuestion+1 < mData.getQuestionCnt(mSubjectStr)){
                        mCurQuestion++;
                        setQuestionScreen();

                    }else{//last question
                        b.setText(getString(R.string.RESET));
                        Toast.makeText(this,getString(R.string.done), Toast.LENGTH_LONG).show();
                    }
                    //Toast.makeText(this,getString(R.string.correct),Toast.LENGTH_LONG).show();

                }else{ //wrong
                    bgImge.setImageResource(R.drawable.incorrect);
                    bgImge.setAlpha(1.0f);
                    bgImge.setAnimation(animation);
                    //RadioButton radio = (RadioButton) findViewById(R.id.questionScreen).findViewById(opts[opt]);
                    //radio.setChecked(true);


                    //Toast.makeText(this,getString(R.string.wrong),Toast.LENGTH_LONG).show();

                }
            }else if(b.getText().toString().equals(getString(R.string.Next))){//next

                if(mCurQuestion+1 < mData.getQuestionCnt(mSubjectStr)){
                    mCurQuestion++;
                    setQuestionScreen();
                    ((Button)findViewById(R.id.questionScreen).findViewById(R.id.okNxt)).setText(getText(R.string.ok));
                }else{//last question
                    b.setText(getString(R.string.RESET));
                    Toast.makeText(this,getString(R.string.done), Toast.LENGTH_LONG).show();
                }
           }else{//reset
                mCurQuestion = 0;
                setQuestionScreen();
                ((Button)findViewById(R.id.questionScreen).findViewById(R.id.okNxt)).setText(getText(R.string.ok));
            }


    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        findViewById(R.id.bgimage).setAlpha(0.0f);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
