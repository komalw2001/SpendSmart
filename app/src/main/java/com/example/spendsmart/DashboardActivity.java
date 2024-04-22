package com.example.spendsmart;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpager);

        viewPager.setAdapter(new TabAdapter(this));

        TabLayoutMediator mediator = new TabLayoutMediator(
                tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {

            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int i) {
                switch (i){
                    case 0:
                    {
                        tab.setText("Overview");
                        tab.setIcon(R.drawable.baseline_bar_chart_24);
                        break;
                    }
                    case 1:
                    {
                        tab.setText("Budgets");
                        tab.setIcon(R.drawable.baseline_attach_money_24);
                        break;
                    }
                    case 2:
                    {
                        tab.setText("Goals");
                        tab.setIcon(R.drawable.baseline_checklist_rtl_24);
                        break;
                    }
                    case 3:
                    {
                        tab.setText("Reminders");
                        tab.setIcon(R.drawable.baseline_announcement_24);
//                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
//                        badgeDrawable.setNumber(51);
//                        badgeDrawable.setBackgroundColor(getResources().getColor(R.color.red, getTheme()));
//                        badgeDrawable.setMaxCharacterCount(3);
//                        badgeDrawable.setVisible(true);
                        break;
                    }
                    default:
                    {
                        tab.setText("Reports");
                        tab.setIcon(R.drawable.baseline_assignment_24);
                        break;
                    }
                }
            }
        });

        mediator.attach();

//        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                BadgeDrawable bd = Objects.requireNonNull(tabLayout.getTabAt(position)).getOrCreateBadge();
//                bd.setVisible(false);
//                bd.setNumber(0);
//            }
//        });
    }

}