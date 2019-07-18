package com.example.minidouyin.main_4_fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.minidouyin.R;
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

import static android.app.Activity.RESULT_OK;

public class fragment_video_depend extends Fragment {
    private Uri mSelectedVideo;
    private Uri mSelectedImage;
    private static final int PICK_VIDEO = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view_fragment = inflater.inflate(R.layout.fragment_video_depend, container, false);
        ImageView iv_shoot = view_fragment.findViewById(R.id.shoot);
        final ImageView iv_local = view_fragment.findViewById(R.id.local);
        int width = 220, height = 220;
        Glide.with(view_fragment)
                .load(R.drawable.shoot)
                .apply(new RequestOptions().override(width, height))
                .into(iv_shoot);
        Glide.with(view_fragment)
                .load(R.drawable.local)
                .apply(new RequestOptions().override(width, height))
                .into(iv_local);
        iv_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);
            }
        });
        return view_fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO && resultCode == RESULT_OK && null != data) {
            mSelectedVideo = data.getData();
            postVideo();
        }

    }
    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        File f = new File(ResourceUtils.getRealPath(getActivity(), uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }

    public  static Bitmap getVideoThumb(String path) {

        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return  media.getFrameAtTime();

    }
    private void postVideo() {
//        MultipartBody.Part coverImagePart = getMultipartFromUri("cover_image", mSelectedImage);
        /* cover from first frame of video */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        File tmpFile = new File(ResourceUtils.getRealPath(getActivity(), mSelectedVideo));
        String path = tmpFile.getAbsolutePath();
        getVideoThumb(path).compress(Bitmap.CompressFormat.JPEG, 100, baos);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), baos.toByteArray());
        MultipartBody.Part coverImagePart = MultipartBody.Part.createFormData("cover_image", tmpFile.getName()+".jpg", requestFile);

        MultipartBody.Part videoPart = getMultipartFromUri("video", mSelectedVideo);
        // TODO 9: post video & update buttons
        Log.d("你的图片是", coverImagePart+"");
        Log.d("你的视频是", videoPart+"");
        /*
            困惑：Call<...>必须是非基础类型，尽管用不到返回数据？比如int就不行
         */
        Call<Feed> mCall = internet.getMiniDouyinService().uploadVideo("1231431", "lollipop", coverImagePart, videoPart);

        mCall.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.d("上传失败", t.toString());
                Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);
    }
}
