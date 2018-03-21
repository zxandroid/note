package com.zx.cn.note.Util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * 適配android 7.0 拍照裁剪类
 */

public class CropImageUtil {
    private Activity activity;
    public static final int TAKE_PHOTO = 1;//拍照
    public static final int CHOOSE_PHOTO = 2;//选择相册
    public static final int PICTURE_CUT = 3;//剪切图片
    private Uri imageUri;//相机拍照图片保存地址
    private Uri outputUri;//裁剪万照片保存地址
    private String imagePath;//打开相册选择照片的路径


    private CropImageUtil() {

    }

    public CropImageUtil(Activity activity) {
        this.activity = activity;
    }

    /**
     * 從攝像頭拍攝照片
     */
    public void FromCamera() {
        // 创建File对象，用于存储拍照后的图片
        File outputImage = new File(activity.getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(outputImage);
        } else {
            //Android 7.0系统开始 使用本地真实的Uri路径不安全,使用FileProvider封装共享Uri
            //重要：在application节点下加入这段，在res文件下的xml文件下创建文件file_paths
//         <provider
//            android:name="android.support.v4.content.FileProvider"
//            android:authorities="com.conow.cn.onlineedu"
//            android:exported="false"
//            android:grantUriPermissions="true">
//            <meta-data
//            android:name="android.support.FILE_PROVIDER_PATHS"
//            android:resource="@xml/file_paths"></meta-data>
//        </provider>
            //参数二:fileprovider绝对路径 com.dyb.testcamerademo：项目包名
            imageUri = FileProvider.getUriForFile(activity, "com.conow.cn.onlineedu", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, TAKE_PHOTO);
    }

    /**
     * 从相册选取图片
     */
    public void FromAlbum() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            activity.startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
        }
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        // 创建File对象，用于存储裁剪后的图片，避免更改原图
        File file = new File(activity.getExternalCacheDir(), "crop_image.jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputUri = Uri.fromFile(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        //裁剪图片的宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("crop", "true");//可裁剪
        // 裁剪后输出图片的尺寸大小
        //intent.putExtra("outputX", 400);
        //intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);//支持缩放
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片格式
        intent.putExtra("noFaceDetection", true);//取消人脸识别
        activity.startActivityForResult(intent, PICTURE_CUT);
    }

    // 4.4及以上系统使用这个方法处理图片 相册图片返回的不再是真实的Uri,而是分装过的Uri
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(activity, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        cropPhoto(uri);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = activity.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        cropPhoto(uri);
    }

    /**
     * 在调用的acitivity的onActivityResult中调用
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void handleResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO://拍照
                    if (resultCode == RESULT_OK) {
                        cropPhoto(imageUri);//裁剪图片
                    }
                    break;
                case CHOOSE_PHOTO://打开相册
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                    break;
                case PICTURE_CUT://裁剪完成
                    onPictureResult.getPic(outputUri);
                    break;
            }
        }
    }

    /**
     * 防止activity保存引用导致内存泄漏，在ondestroy中调用
     */
    public void release() {
        activity = null;
    }


    /**
     * 完成回调。
     */
    onPictureResult onPictureResult;
    public void setGetPictureListener(onPictureResult onPictureResult) {
        this.onPictureResult = onPictureResult;
    }
    public interface onPictureResult {
        void getPic(Uri path);
    }

}
