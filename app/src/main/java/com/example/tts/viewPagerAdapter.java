package com.example.tts;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class viewPagerAdapter extends FragmentStateAdapter {

    public viewPagerAdapter(@NonNull BookingFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CurrentBookingsFragment();
            case 1:
                return new PastBookingsFragment();
            // Add more cases for additional fragments
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of fragments
    }
}