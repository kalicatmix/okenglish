package com.oe.okenglish.adaptor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.oe.okenglish.fragment.ToolPlanFragment;
import com.oe.okenglish.fragment.ToolReaderFragment;

public class ToolContentAdaptor extends FragmentPagerAdapter {
    public static final int PAGE_SIZE=2;
    private Fragment[] fragments = new Fragment[]{
            new ToolReaderFragment(),
            new ToolPlanFragment(),
    };

    public ToolContentAdaptor(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}