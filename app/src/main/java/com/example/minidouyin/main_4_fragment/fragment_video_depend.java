package com.example.minidouyin.main_4_fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.minidouyin.PostActivity;
import com.example.minidouyin.PreviewActivity;
import com.example.minidouyin.R;
import com.example.minidouyin.utils.ResourceUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class fragment_video_depend extends Fragment {
    private Uri mSelectedVideo;
    private Uri mSelectedImage;
    private File videoFile;
    private String videoFilePath;
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
        iv_shoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PostActivity.class));
            }
        });
        return view_fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO && resultCode == RESULT_OK && null != data) {
            mSelectedVideo = data.getData();
            videoFile = new File(ResourceUtils.getRealPath(getActivity(), mSelectedVideo));
            videoFilePath = videoFile.getAbsolutePath();
            Intent i = new Intent(getActivity(), PreviewActivity.class);
            i.putExtra("path", videoFilePath);
            startActivity(i);
//            postVideo();
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

    public void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);
    }
}
