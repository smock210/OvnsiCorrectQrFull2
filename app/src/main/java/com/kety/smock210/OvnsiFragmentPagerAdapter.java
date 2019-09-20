package com.kety.smock210.ovnsicorrectqrfull;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

public class OvnsiFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Tab1", "Tab2"};
    private Context context;
    private String mQrText;

    public OvnsiFragmentPagerAdapter(FragmentManager fm, Context context, String qrText) {
        super(fm);
        this.context = context;
        this.mQrText = qrText;
    }

    @Override public int getCount() {
        return PAGE_COUNT;
    }

    @Override public Fragment getItem(int position) {


        return PageFragment.newInstance(position + 1, this.mQrText + position);
    }

    @Override public CharSequence getPageTitle(int position) {
        // генерируем заголовок в зависимости от позиции
        return tabTitles[position];
    }
}