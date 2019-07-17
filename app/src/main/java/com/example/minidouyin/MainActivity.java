package com.example.minidouyin;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.minidouyin.main_4_fragment.fragment_following;
import com.example.minidouyin.main_4_fragment.fragment_index;
import com.example.minidouyin.main_4_fragment.fragment_me;
import com.example.minidouyin.main_4_fragment.fragment_message;
import com.example.minidouyin.main_4_fragment.fragment_video_depend;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.addToBackStack(null);
            switch (item.getItemId()) {
                case R.id.nav_index:
                    transaction.replace(R.id.main_frame, new fragment_index());
                    transaction.commit();
                    return true;
                case R.id.nav_following:
                    transaction.replace(R.id.main_frame, new fragment_following());
                    transaction.commit();
                    return true;
                case R.id.nav_message:
                    transaction.replace(R.id.main_frame, new fragment_message());
                    transaction.commit();
                    return true;
                case R.id.nav_me:
                    transaction.replace(R.id.main_frame, new fragment_me());
                    transaction.commit();
                    return true;
                case R.id.nav_post:
                    transaction.replace(R.id.main_frame, new fragment_video_depend());
                    transaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.main_frame, new fragment_index());
        transaction.commit();
    }

}
