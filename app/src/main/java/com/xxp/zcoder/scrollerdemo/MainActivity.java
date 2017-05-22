package com.xxp.zcoder.scrollerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtDuration;
    private Button btnRun;
    private IconRunImageView ivIcon;
    private int mDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        btnRun.setOnClickListener(this);
        mDuration = Integer.parseInt(txtDuration.getText().toString());
    }

    private void initView() {
        txtDuration = (EditText) findViewById(R.id.txtDuration);
        ivIcon = (IconRunImageView) findViewById(R.id.ivIcon);
        btnRun = (Button) findViewById(R.id.btnRun);
    }

    @Override
    public void onClick(View v) {
        ivIcon.scroll(100, 100, 500, 500, mDuration);
    }
}
