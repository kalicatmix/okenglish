package com.oe.okenglish.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.oe.okenglish.R;
import com.oe.okenglish.adaptor.ToolContentAdaptor;

public class ToolFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tool, null);
        TabLayout tabs = view.findViewById(R.id.tool_tabs);
        ViewPager content = view.findViewById(R.id.tool_content);
        tabs.setTabIconTint(ColorStateList.valueOf(Color.parseColor("#008577")));
        TabLayout.Tab reader = tabs.newTab().setText("阅读");
        TabLayout.Tab plan = tabs.newTab().setText("计划");
        reader.setIcon(R.drawable.ic_chrome_reader_mode_green_24dp);
        plan.setIcon(R.drawable.ic_assistant_photo_green_24dp);
        tabs.addTab(reader);
        tabs.addTab(plan);
        content.setAdapter(new ToolContentAdaptor(getFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        content.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        content.setOffscreenPageLimit(ToolContentAdaptor.PAGE_SIZE);
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(content));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
