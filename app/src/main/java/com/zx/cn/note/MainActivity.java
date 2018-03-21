package com.zx.cn.note;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zx.cn.note.Util.CropImageUtil;

public class MainActivity extends AppCompatActivity {

    private CropImageUtil cropImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
