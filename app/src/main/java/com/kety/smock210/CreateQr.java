package com.kety.smock210.ovnsicorrectqrfull;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class CreateQr extends AppCompatActivity {
    public static  String parqr = "";
    static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        parqr = extras.getString("qr");
        final ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(
                new OvnsiFragmentPagerAdapter(getSupportFragmentManager(), CreateQr.this, parqr));

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected, position = " + position);
                if (position == 0){
                    TextView vie2 = viewPager.findViewById(R.id.fullCodeText);
                    TextView vie = viewPager.findViewById(R.id.EditfullCodeText);
                    vie2.setText(vie.getText());
                }
                if (position == 1){
                    TextView vie = viewPager.findViewById(R.id.fullCodeText);
                    TextView vie2 = viewPager.findViewById(R.id.EditfullCodeText);
                    vie2.setText(vie.getText());

                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Передаём ViewPager в TabLayout
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        findViewById(R.id.sliding_tabs);

        //TextView vie=findViewById(R.id.fullCodeText);
        //vie.setText("New Text");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
