package com.example.minidouyin;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minidouyin.api.IMiniDouyinService;
import com.example.minidouyin.player.VideoPlayerIJK;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class VerticalViewPager extends AppCompatActivity {
    private String theVideoURI, lastVideoURI, nextVideoURI;
    private VideoPlayerIJK ijkPlayer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theVideoURI = getIntent().getStringExtra("theVideoURI");
        lastVideoURI = getIntent().getStringExtra("lastVideoURI");
        nextVideoURI = getIntent().getStringExtra("nextVideoURI");
        setContentView(R.layout.myactivity_video);
        initRecyclerView();
    }

    private Retrofit retrofit;
    private IMiniDouyinService miniDouyinService;
    private RecyclerView mRv;
    private List<VideoPlayerIJK> ijkPlayers = new ArrayList<>();


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private Context context;
        private List<VideoPlayerIJK> ijkPlayers;
        private int [] ImageMetaData={0,1};
        public MyAdapter(Context context, List<VideoPlayerIJK> ijkMediaPlayers){
            this.context = context;
            this.ijkPlayers =ijkMediaPlayers;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = View.inflate(context, R.layout.item_view_pager, null);
            MyViewHolder viewHolder = new MyViewHolder(itemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
            final VideoPlayerIJK ijk = ijkPlayers.get(i);
            viewHolder.bind(VerticalViewPager.this, ijk);
        }
        @Override
        public int getItemCount() {
            return ijkPlayers.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
            }
            public void bind(final AppCompatActivity activity, final VideoPlayerIJK ijk) {
                Resources resources = VerticalViewPager.this.getResources();
            }
        }
    }
    private void initRecyclerView() {
        mRv = findViewById(R.id.player_recycler);
        mRv.setLayoutManager(new LinearLayoutManager(VerticalViewPager.this, RecyclerView.VERTICAL, false));
        mRv.setAdapter(new MyAdapter(VerticalViewPager.this, ijkPlayers));
        ijkPlayer = findViewById(R.id.ijk);
        ijkPlayer.setVideoPath(theVideoURI);
        ijkPlayer.start();
    }

}
