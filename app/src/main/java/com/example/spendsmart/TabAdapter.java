package com.example.spendsmart;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabAdapter extends FragmentStateAdapter {
    public TabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
            {
                return new OverviewFragment();
            }
            case 1:
            {
                return new BudgetsFragment();
            }
            case 2:
            {
                return new GoalsFragment();
            }
            case 3:
            {
                return new RemindersFragment();
            }
            default:
            {
                return new ReportsFragment();
            }
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
