package com.example.calog.common;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.calog.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class GraphPagerFragment extends Fragment {

    public GraphPagerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_graph_pager, container, false);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                TabLayout tabLayout = view.findViewById(R.id.tabLayout);
                ViewPager viewPager = view.findViewById(R.id.graphPager);

                PagerAdapter pagerAdapter = new PagerAdapter(getFragmentManager());
                viewPager.setAdapter(pagerAdapter);

                tabLayout.setupWithViewPager(viewPager);

                ProgressBar circleProgress = view.findViewById(R.id.progress_circular);
                circleProgress.setVisibility(View.GONE);
            }
        }, 3000);

        return view;
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragments = new ArrayList<>();
        String[] tabTitle = {"Week", "Month", "Year"};

        public PagerAdapter(FragmentManager fm) {
            super(fm);

            fragments.add(new GraphFragment("week"));
            fragments.add(new GraphFragment("month"));
            fragments.add(new GraphFragment("year"));

        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle[position];
        }
    }
}
