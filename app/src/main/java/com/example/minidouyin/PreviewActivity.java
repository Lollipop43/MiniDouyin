package com.example.minidouyin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minidouyin.api.internet;
import com.example.minidouyin.model.Feed;
import com.example.minidouyin.utils.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviewActivity extends AppCompatActivity {

    VideoView videoView ;
    Button btnSelectImg;
    String videoFilePath;
    Button btnPostIt;
    Uri mSelectedImage = null;
    private static final int PICK_IMAGE = 1;
    private static final int IMG_FROM_1_FRAME = 1;
    private static final int IMG_FROM_USER = 2;
    private int imgFromWhere = IMG_FROM_1_FRAME;//default
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_preview);
        btnPostIt = findViewById(R.id.postIt);
        videoView = findViewById(R.id.vv);
        btnSelectImg = findViewById(R.id.selectImg);

        videoFilePath = getIntent().getStringExtra("path");
        videoView.setVideoPath(videoFilePath);
        videoView.start();

        btnPostIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postVideo(imgFromWhere);
            }
        });
        btnSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgFromWhere = IMG_FROM_USER;
                chooseImage();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.setVideoPath(getIntent().getStringExtra("path"));
                videoView.start();
            }
        });

    }
    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == PICK_IMAGE) {
                mSelectedImage = data.getData();
            }
        }
    }
    private void postVideo(int imgSrc) {
//        MultipartBody.Part coverImagePart = getMultipartFromUri("cover_image", mSelectedImage);
        /* cover from first frame of video */
        MultipartBody.Part coverImagePart = null;
        if (imgSrc == IMG_FROM_1_FRAME){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            getVideoThumb(videoFilePath).compress(Bitmap.CompressFormat.JPEG, 100, baos);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), baos.toByteArray());
            coverImagePart = MultipartBody.Part.createFormData("cover_image", new File(videoFilePath.trim()).getName()+".jpg", requestFile);
        }else if (imgSrc == IMG_FROM_USER){
            if (mSelectedImage != null)
                coverImagePart = getMultipartFromUri("cover_image", mSelectedImage);
        }
        MultipartBody.Part videoPart = getMultipartFromPath("video", videoFilePath);
        Log.d("你的图片是", coverImagePart+"");
        Log.d("你的视频是", videoPart+"");
        Call<Feed> mCall = internet.getMiniDouyinService().uploadVideo("1231431", "lollipop", coverImagePart, videoPart);

        mCall.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                Toast.makeText(PreviewActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.d("上传失败", t.toString());
                Toast.makeText(PreviewActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        File f = new File(ResourceUtils.getRealPath(PreviewActivity.this, uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }
    private MultipartBody.Part getMultipartFromPath(String name, String path){
        File f = new File(path.trim());
        RequestBody requestFile  = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }

    public  static Bitmap getVideoThumb(String path) {

        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return  media.getFrameAtTime();

    }
}
