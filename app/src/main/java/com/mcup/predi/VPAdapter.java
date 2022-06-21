package com.mcup.predi;

import android.os.Bundle;
import android.view.View;

import com.mcup.predi.ui.news.NewsFragment;
import com.mcup.predi.ui.news.NewsObjectFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStateManagerControl;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class VPAdapter extends FragmentStatePagerAdapter {

    public VPAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new Fragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt(NewsObjectFragment.ARG_OBJECT, i + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return "OBJECT" + (position + 1);
    }
}
