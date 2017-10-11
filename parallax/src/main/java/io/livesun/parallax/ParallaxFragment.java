package io.livesun.parallax;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：视差框架的fragment
 * 创建人：livesun
 * 创建时间：2017/9/22.
 * 修改人：
 * 修改时间：
 * github：https://github.com/livesun
 */
public class ParallaxFragment extends Fragment implements LayoutInflater.Factory2 {

    public static final String LAYOUT_ID_KEY="layout_id_key";
    private ParallaxCompatViewInflater mCompatViewInflater;
    private static final boolean IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21;
    private int [] mAttrSets=new int[]{
            R.attr.translationXIn,
            R.attr.translationXOut,
            R.attr.translationYIn,
            R.attr.translationYOut,

            };
    private List<View> mParallaxViews=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layouId=getArguments().getInt(LAYOUT_ID_KEY);
        //解析View的属性
        inflater = inflater.cloneInContext(getContext());
        LayoutInflaterCompat.setFactory2(inflater,this);
        return inflater.inflate(layouId,container,false);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attributeSet) {
        //创建View

        View view=createView(parent,name,context,attributeSet);

        if(view!=null){
            //解析view的属性
          analysisAttrs(view,context,attributeSet);
        }

        return view;
    }

    /**
     * 解析View的属性
     * @param view
     * @param context
     * @param attributeSet
     */
    private void analysisAttrs(View view, Context context, AttributeSet attributeSet) {
        TypedArray array = context.obtainStyledAttributes(attributeSet, mAttrSets);
        if(array!=null&&array.getIndexCount()!=0){
            int indexCount = array.getIndexCount();
            ParallaxTag tag = new ParallaxTag();
            for(int i=0;i<indexCount;i++){
                int attr = array.getIndex(i);
                switch (attr){
                    case 0:
                        tag.translationXIn = array.getFloat(attr, 0f);
                        break;
                    case 1:
                        tag.translationXOut=array.getFloat(attr, 0f);
                        break;
                    case 2:
                        tag.translationYIn=array.getFloat(attr, 0f);
                        break;
                    case 3:
                        tag.translationYOut=array.getFloat(attr, 0f);
                        break;

                }
            }
            view.setTag(R.id.parallax_tag,tag);
            mParallaxViews.add(view);
        }

        array.recycle();
    }

    public List<View> getParallaxViews(){
        return mParallaxViews;
    }

    @SuppressLint("RestrictedApi")
    private View createView(View parent, String name, Context context, AttributeSet attributeSet) {
        if (mCompatViewInflater == null) {
            mCompatViewInflater = new ParallaxCompatViewInflater();
        }

        boolean inheritContext = false;
        if (IS_PRE_LOLLIPOP) {
            inheritContext = (attributeSet instanceof XmlPullParser)
                    // If we have a XmlPullParser, we can detect where we are in the layout
                    ? ((XmlPullParser) attributeSet).getDepth() > 1
                    // Otherwise we have to use the old heuristic
                    : shouldInheritContext((ViewParent) parent);
        }

        return mCompatViewInflater.createView(parent, name, context, attributeSet, inheritContext,
                IS_PRE_LOLLIPOP, /* Only read android:theme pre-L (L+ handles this anyway) */
                true, /* Read read app:theme as a fallback at all times for legacy reasons */
                VectorEnabledTintResources.shouldBeUsed() /* Only tint wrap the context if enabled */
        );
    }


    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final View windowDecor = getActivity().getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == windowDecor || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return onCreateView(null, name, context, attrs);
    }
}
