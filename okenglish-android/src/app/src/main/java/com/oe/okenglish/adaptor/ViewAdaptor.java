package com.oe.okenglish.adaptor;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.oe.okenglish.fragment.HomeFragment;
import com.oe.okenglish.fragment.SettingFragment;
import com.oe.okenglish.fragment.ToolFragment;

public class ViewAdaptor extends FragmentStateAdapter {
    private Fragment[] fragments=new Fragment[]{new HomeFragment(),new ToolFragment(),new SettingFragment()};
    public static int HOME_ITEM=0;
    public static int TOOL_ITEM=1;
    public static int SETTING_ITEM=2;
    public ViewAdaptor(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }
}
