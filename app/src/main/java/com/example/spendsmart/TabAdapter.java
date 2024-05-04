package com.example.spendsmart;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentStateAdapter {

    private List<Fragment> fragments = new ArrayList<>();

    public TabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return the fragment based on the position
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        // Return the total number of fragments
        return fragments.size();
    }
}
