package com.zx.cn.note.toolPage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.zx.cn.note.R;
import com.zx.cn.note.Util.CompressImgUtil;
import com.zx.cn.note.Util.CropImageUtil;

import java.io.File;

/**
 * Created by zhang xiong on 2018/3/21.
 */

public class captureDemo extends Activity{
    private CropImageUtil cropImage;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState) ;
        setContentView(R.layout.activity_capture_demo);
        final ImageView image = findViewById(R.id.iv_head);
        cropImage = new CropImageUtil(this);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImage.FromCamera();
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImage.FromAlbum();
            }
        });

        //裁剪回调，在onActivityResult中调用handlerrsult后回调
        cropImage.setGetPictureListener(new CropImageUtil.onPictureResult() {

            @Override
            public void getPic(Uri path) {
                //压缩
                File scal = CompressImgUtil.scal(path);
                Bitmap bitmap = BitmapFactory.decodeFile(scal.getPath());
                //可以使用scal.getPath()后上传图片
                image.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cropImage.handleResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        cropImage.release();
        super.onDestroy();
    }

}
