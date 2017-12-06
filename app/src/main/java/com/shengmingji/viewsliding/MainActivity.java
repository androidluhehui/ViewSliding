package com.shengmingji.viewsliding;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout rootView;
    private SlidingView slidingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = findViewById(R.id.root);
        slidingView = (SlidingView) findViewById(R.id.sliding_view);

    }


    public void scrollTo(View view) {
        rootView.scrollTo(0, -200);

    }

    public void scrollBy(View view) {
        rootView.scrollBy(0, 200);
    }

    public void scroller(View view) {
        slidingView.smoothScrollTo(200, 300);
    }

    public void translation(View view) {
        ObjectAnimator.ofFloat(slidingView,"translationX",0,400)
                .setDuration(2000).start();
        ObjectAnimator.ofFloat(slidingView,"translationY",0,400)
                .setDuration(2000).start();
        //slidingView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.translate));
    }

    public void layoutParams(View view){
        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) slidingView.getLayoutParams();
        layoutParams.leftMargin += 150;
        layoutParams.topMargin += 150;
        slidingView.setLayoutParams(layoutParams);
    }
}
