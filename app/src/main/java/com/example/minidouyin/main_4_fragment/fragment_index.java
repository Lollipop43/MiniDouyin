package com.example.minidouyin.main_4_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.minidouyin.IJKPlayerActivity;
import com.example.minidouyin.R;
import com.example.minidouyin.api.IMiniDouyinService;
import com.example.minidouyin.model.Feed;
import com.example.minidouyin.model.Video;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class fragment_index extends Fragment {
    private View view_fragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_fragment = inflater.inflate(R.layout.fragment_index, container, false);
        initRecyclerView();
        fetchFeed(view_fragment);
        return view_fragment;
    }
    private Retrofit retrofit;
    private IMiniDouyinService miniDouyinService;
    private RecyclerView mRv;
    private List<Video> mVideos = new ArrayList<>();

    private IMiniDouyinService getMiniDouyinService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(IMiniDouyinService.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        if (miniDouyinService == null) {
            miniDouyinService = retrofit.create(IMiniDouyinService.class);
        }
        return miniDouyinService;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private Context context;
        private List<Video> videos;
        private int [] ImageMetaData={0,1};
        public MyAdapter(Context context, List<Video> videos){
            this.context = context;
            this.videos =videos;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = View.inflate(context, R.layout.video_item_view, null);
            MyViewHolder viewHolder = new MyViewHolder(itemView);
            view_fragment.setTag(viewHolder);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
            final Video video = videos.get(i);
            viewHolder.bind((AppCompatActivity) getActivity(), video);
        }
        @Override
        public int getItemCount() {
            return videos.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView img;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
            }
            public void bind(final AppCompatActivity activity, final Video video) {
                Resources resources = getContext().getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();
                int width = (dm.widthPixels-16)/2;
                int height = width*video.getImageHeight()/video.getImageWidth();
                img.setMinimumWidth(width);
                img.setMaxWidth(width);
                img.setMinimumHeight(height);
                img.setMaxHeight(height);
                RequestOptions options =
                        new RequestOptions()
                                .override(width, height)
                                .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide
                        .with(img.getContext())
                        .load(video.getImageUrl())
                        .apply(options)
                        .transition(withCrossFade())
                        .into(img);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), IJKPlayerActivity.class);
                        i.putExtra("videoURL", video.getVideoUrl());
                        startActivity(i);
//                        VideoActivity.launch(getActivity(), video.getVideoUrl());
//                        VideoView videoView = getActivity().findViewById(R.id.video_container);
//                        videoView.setVideoURI(Uri.parse(video.getVideoUrl()));
//                        videoView.start();
                    }
                });
            }
        }
    }
    public class MyItemDecoration extends RecyclerView.ItemDecoration{
//        int delta = UICommonUtils.dpToPixel(getActivity(), 5);
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
//            if (parent.getChildAdapterPosition(view) % 4 == 0){
//                outRect.left = 3;
//                outRect.right = 5;
//            }else if (parent.getChildAdapterPosition(view) % 4 == 1){
//                outRect.left = 5;
//                outRect.right = 3;
//            }
//            outRect.bottom = 6;
        }
    }
    private void initRecyclerView() {
        mRv = view_fragment.findViewById(R.id.rv);
        mRv.addItemDecoration(new MyItemDecoration());
        final StaggeredGridLayoutManager grid = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRv.setLayoutManager(grid);
        grid.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRv.setAdapter(new MyAdapter(getActivity(), mVideos));


        ((DefaultItemAnimator) mRv.getItemAnimator()).setSupportsChangeAnimations(false);
        ((SimpleItemAnimator) mRv.getItemAnimator()).setSupportsChangeAnimations(false);
        mRv.getItemAnimator().setChangeDuration(0);

        mRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                grid.invalidateSpanAssignments();
            }
        });
    }
    public void fetchFeed(View view) {

        Call<Feed> call = getMiniDouyinService().downloadVideo();
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                Feed feed = response.body();
                List<Video> videos = feed.getFeeds();

                Log.d("获得的视频的个数", videos.size()+"");
                String data = "";
//                List<String> ids = new ArrayList<>();
                for (int i = 0; i < videos.size(); i++){
                    Video v = new Video();
                    v.setVideoUrl(videos.get(i).getVideoUrl());
                    v.setStudentId(videos.get(i).getStudentId());
                    v.setUserName(videos.get(i).getUserName());
                    v.setImageUrl(videos.get(i).getImageUrl());
                    v.setImageWidth(videos.get(i).getImageWidth());
                    v.setImageHeight(videos.get(i).getImageHeight());
                    mVideos.add(v);
                }
                initRecyclerView();
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable throwable) {
                Log.d("HTTP请求", "请求失败： " + throwable.toString());
            }
        });
    }
}
