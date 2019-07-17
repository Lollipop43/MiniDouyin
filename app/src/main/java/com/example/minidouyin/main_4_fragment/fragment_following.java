package com.example.minidouyin.main_4_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.minidouyin.R;
import com.example.minidouyin.child_fragment.child_fragment_following;
import com.example.minidouyin.child_fragment.child_fragment_friends;
import com.google.android.material.tabs.TabLayout;

public class fragment_following extends Fragment {
    final private int PAGE_COUNT = 2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragment_view = inflater.inflate(R.layout.fragment_following, container, false);
        ViewPager pager = fragment_view.findViewById(R.id.view_pager);
        TabLayout tabLayout = fragment_view.findViewById(R.id.tab_layout);

        pager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if (i == 0)
                    return new child_fragment_following();
                else
                    return new child_fragment_friends();
            }

            @Override
            public int getCount() {
                return PAGE_COUNT;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0)
                    return "关注";
                else
                    return "好友";
            }
        });
        tabLayout.setupWithViewPager(pager);
        return fragment_view;
    }
}

