package com.example.minidouyin.child_fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.minidouyin.R;
import com.example.minidouyin.model.Video;

import java.util.List;

public class fragment_index_video extends Fragment{
    private List<Video> videos;
    private Video targetVideo;
    private VideoView vv;
    private View view_fragment;
    private GestureDetector mGestureDetector;
    public fragment_index_video(List<Video> videos, Video video){
        this.videos = videos;
        this.targetVideo = video;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_fragment = inflater.inflate(R.layout.fragment_index_video, container, false);
        vv = view_fragment.findViewById(R.id.video_container);
        mGestureDetector = new GestureDetector(getActivity(), new MyOnGestureListener());
        vv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });

        vv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vv.isPlaying()){
                    vv.pause();
                }else{
                    vv.start();
                }
            }
        });
        vv.setVideoURI(Uri.parse(targetVideo.getVideoUrl()));
        vv.start();
        return view_fragment;
    }
    public class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            if (vv.isPlaying()){
                vv.pause();
            }else{
                vv.start();
            }
            return true;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {

            Log.d("正在监听singletapup事件", "");
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d("你双击了","");
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

            Log.d("正在监听scroll事件", "");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

            Log.d("正在监听长按事件", "");
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            int thisIndex = 0;
            int nextIndex = 0;
            if (videos.contains(targetVideo)) {
                thisIndex = videos.indexOf(targetVideo);
                if (motionEvent.getY() - motionEvent1.getY() > 120) {//从下往上滑动
                    nextIndex = thisIndex == (videos.size() - 1) ? thisIndex : thisIndex + 1;
                } else if (motionEvent.getY() - motionEvent1.getY() < -120) {//从上往下滑动
                    nextIndex = thisIndex == 0?thisIndex:thisIndex-1;
                }
            }
            Log.d("正在监听滑动事件", "");
//            vv.pause();
//            vv.setVideoURI(Uri.parse(videos.get(nextIndex).getVideoUrl()));
//            vv.start();
            return true;
        }
    }

}
