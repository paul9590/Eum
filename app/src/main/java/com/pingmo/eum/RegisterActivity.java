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
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;


public class RegisterActivity extends AppCompatActivity {
    Dialog addrDialog;
    private EditText editAddrSI;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnSignIn = (Button) findViewById(R.id.btnSignIn);
        EditText editNameSI = (EditText) findViewById(R.id.editNameSI);
        editAddrSI = (EditText) findViewById(R.id.editAddrSI);

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

                RegisterRequest rR = new RegisterRequest("192.168.0.81", 8856, sb.toString());
            }
        });
    }

    private void addrDial(){
        addrDialog.show();
        init_web();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void init_web() {
        WebView webAddr = (WebView) addrDialog.findViewById(R.id.webAddr);

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