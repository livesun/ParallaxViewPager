package io.livesun.parallaxviewpager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.livesun.parallax.ParallaxViewPager;

public class ParallaxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParallaxViewPager viewPager=findViewById(R.id.pager);
        viewPager.setLayout(getSupportFragmentManager(),
                new int[]{R.layout.fragment_page_first,
                        R.layout.fragment_page_second,
                        R.layout.fragment_page_third});

    }
}
