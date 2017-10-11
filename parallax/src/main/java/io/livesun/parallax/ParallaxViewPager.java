package io.livesun.parallax;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：视差的ViewPager
 * 创建人：livesun
 * 创建时间：2017/9/22.
 * 修改人：
 * 修改时间：
 * github：https://github.com/livesun
 */
public class ParallaxViewPager extends ViewPager {
    //布局数组
    private int[] mLayouts;
    private List<ParallaxFragment> mFragments=new ArrayList<>();
    public ParallaxViewPager(Context context) {
        this(context,null);
    }

    public ParallaxViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLayout(FragmentManager fm, int[] layouts) {
        mFragments.clear();
        mLayouts = layouts;
        for (int layoutId : layouts) {
            ParallaxFragment fragment=new ParallaxFragment();
            Bundle bundle=new Bundle();
            bundle.putInt(ParallaxFragment.LAYOUT_ID_KEY,layoutId);
            fragment.setArguments(bundle);
            mFragments.add(fragment);
        }
        setAdapter(new ParallaxPagerAdapter(fm));
        //监听
        addOnPageChangeListener(new SimpleOnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                // 左out 右 in
                ParallaxFragment outFragment = mFragments.get(position);
                List<View> parallaxViews = outFragment.getParallaxViews();
                for (View parallaxView : parallaxViews) {
                    ParallaxTag tag = (ParallaxTag) parallaxView.getTag(R.id.parallax_tag);
                    parallaxView.setTranslationX((-positionOffsetPixels)*tag.translationXOut);
                    parallaxView.setTranslationY((-positionOffsetPixels)*tag.translationYOut);
                }

                try {
                    ParallaxFragment inFragment = mFragments.get(position+1);
                    parallaxViews = inFragment.getParallaxViews();
                    for (View parallaxView : parallaxViews) {
                        ParallaxTag tag = (ParallaxTag) parallaxView.getTag(R.id.parallax_tag);
                        parallaxView.setTranslationX((getMeasuredWidth()-positionOffsetPixels)*tag.translationXIn);
                        parallaxView.setTranslationY((getMeasuredWidth()-positionOffsetPixels)*tag.translationYIn);

                    }
                }catch (Exception e){}
            }
        });
    }

    private class ParallaxPagerAdapter extends FragmentPagerAdapter {

        public ParallaxPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
