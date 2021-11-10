package com.pingmo.eum;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;


public class RegisterActivity extends AppCompatActivity {
    Dialog addrDialog, jobDialog;
    private EditText editAddrSI;
    private Handler handler;
    private final String [] JOB_LIST = {"개발", "새발"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnSignIn = (Button) findViewById(R.id.btnSignIn);
        EditText editNameSI = (EditText) findViewById(R.id.editNameSI);
        editAddrSI = (EditText) findViewById(R.id.editAddrSI);
        EditText editJobSI = (EditText) findViewById(R.id.editJobSI);

        Intent RegisterIntent = getIntent();
        String UID = RegisterIntent.getStringExtra("UID");
        handler = new Handler();

        editAddrSI.setFocusable(false);
        editAddrSI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addrDialog = new Dialog(RegisterActivity.this);
                addrDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                addrDialog.setContentView(R.layout.dial_addr);
                addrDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                addrDial();
            }
        });

        editJobSI.setFocusable(false);
        editJobSI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobDialog = new Dialog(RegisterActivity.this);
                jobDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                jobDialog.setContentView(R.layout.dial_job);
                jobDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                jobDial();
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                sb.append("'0'&'" + UID + "'&'");
                sb.append(editNameSI.getText().toString() + "'&'");
                String addrs[] = editAddrSI.getText().toString().split(" ");
                for(String addr : addrs) {
                    sb.append(addr + "'&'");
                }
                sb.append("개발'");

                RegisterRequest rR = new RegisterRequest(getString(R.string.SERVERIP), 8856, sb.toString());
            }
        });


    }

    private void addrDial(){
        addrDialog.show();
        init_web();
    }

    private void jobDial(){
        jobDialog.show();

        ListView listJob = (ListView) jobDialog.findViewById(R.id.listJob);
        ArrayAdapter<String> jobAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_txt, JOB_LIST);
        listJob.setAdapter(jobAdapter);

        listJob.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jobDialog.dismiss();
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void init_web() {
        WebView webAddr = (WebView) addrDialog.findViewById(R.id.webAddr);

        webAddr.setBackgroundColor(0);
        webAddr.getSettings().setJavaScriptEnabled(true);
        webAddr.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webAddr.addJavascriptInterface(new AndroidBridge(), "Eum");
        webAddr.setWebChromeClient(new WebChromeClient());
        webAddr.loadUrl("http://pingmo.co.kr/daum_address.php");

    }


    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(String addr1, String addr2, String addr3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    editAddrSI.setText(addr1 + " " + addr2 + " " + addr3);
                    addrDialog.dismiss();
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}