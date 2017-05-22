package com.xxp.zcoder.scrollerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtDuration;
    private Button btnRun;
    private Mylayout myLayout;
    private int mDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        btnRun.setOnClickListener(this);
    }

    private void initView() {
        txtDuration = (EditText) findViewById(R.id.txtDuration);
        myLayout = (Mylayout) findViewById(R.id.myLayout);
        btnRun = (Button) findViewById(R.id.btnRun);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myLayout:
                mDuration = Integer.parseInt(txtDuration.getText().toString());
                myLayout.scroll(0, 0, -500, -500, mDuration);
                break;
        }
    }
}
