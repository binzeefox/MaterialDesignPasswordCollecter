package com.binzeefox.materialdesignpasswordcollecter.animation;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.*;

/**
 * Created by tong.xiwen on 2017/4/12.
 */
public class MyAnimation {

    /**
     * 旋转动画
     *
     * @param view  控件
     * @param radio 旋转角度
     * @param time  持续时间
     * @return
     */
    public static RotateAnimation rotateAct(View view, float radio, int time) {
        int pivotX1 = view.getWidth() / 2;
        int pivotY1 = view.getHeight() / 2;
        RotateAnimation ra = new RotateAnimation(0, radio, pivotX1, pivotY1);
        ra.setDuration(time);
//        ra.setInterpolator(new CycleInterpolator(1.5f));
        view.startAnimation(ra);
        return ra;
    }

    /**
     * 透明动画（设定时间）
     *
     * @param view
     * @param fromAlpha
     * @param toAlpha
     * @param time
     * @return
     */
    public static AlphaAnimation AlphaAct(View view, float fromAlpha, float toAlpha, int time) {

        AlphaAnimation aa = new AlphaAnimation(fromAlpha, toAlpha);
        aa.setDuration(time);
        view.startAnimation(aa);
        return aa;
    }

    /**
     * 透明动画（预设250毫秒）
     *
     * @param view
     * @param fromAlpha
     * @param toAlpha
     * @return
     */
    public static AlphaAnimation AlphaAct(View view, float fromAlpha, float toAlpha) {

        AlphaAnimation aa = new AlphaAnimation(fromAlpha, toAlpha);
        aa.setDuration( 500);
        view.startAnimation(aa);
        return aa;
    }

    /**
     * 震动动画（预设 500毫秒）
     *
     * @param view
     * @return
     */
    public static TranslateAnimation shakeAct(View view) {

        TranslateAnimation ta = new TranslateAnimation(0, 10, 0, 0);
        ta.setInterpolator(new CycleInterpolator(4f));
        ta.setDuration( 500);
        view.startAnimation(ta);
        return ta;
    }


    /**
     * 弹出动画
     * @param view
     * @param isVisible
     * @return
     */
    public static ScaleAnimation popUpAct(View view, boolean isVisible) {

        ScaleAnimation sa;
        /**
         * 设置缩放中心的值
         */
        int pivotX0 = view.getWidth() / 2;
        int pivotY0 = view.getHeight() / 2;


        //初始化缩放对象
        if (isVisible) {
            int pivotX = 0;
            int pivotY = view.getHeight();
            sa = new ScaleAnimation(1f, 0, 1f, 0, pivotX,
                    pivotY);
        } else {
            int pivotX = view.getWidth();
            int pivotY = view.getHeight();
            sa = new ScaleAnimation(0, 1f, 0, 1f, pivotX,
                    pivotY);
        }
        //动画的速率
        sa.setDuration( 500);
        view.startAnimation(sa);
        return sa;
    }

    /**
     * 拉伸动画
     * @param view
     * @param isShowing
     * @return
     */
    public static boolean scaleAct(View view, boolean isShowing) {

        if (isShowing){
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0);
            animator.setDuration(500);
            animator.start();
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleY", 0, 1f);
            animator.setDuration(500);
            animator.start();
        }

        return true;
    }

    /**
     * 伸缩动画
     * @param view
     * @param isShowing
     * @param time
     * @return
     */
    public static boolean popUpAndDown(final View view, boolean isShowing ,int time) {

        Animation transformAnim;


        if (isShowing) {
            final int initialHeight = view.getMeasuredHeight();
            transformAnim = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    view.getLayoutParams().height = (int) ((initialHeight) * interpolatedTime);
                    view.requestLayout();

                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };
            transformAnim.setDuration(time);
            view.startAnimation(transformAnim);
        }else {
            final int initialHeight = view.getMeasuredHeight();
            transformAnim = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    view.getLayoutParams().height = initialHeight - (int) ((initialHeight) * interpolatedTime);
                    view.requestLayout();
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };
            transformAnim.setDuration(time);
            view.startAnimation(transformAnim);
        }

        return true;
    }

    /**
     * 渐入渐出（返回ObjectAnimator）
     * @param view
     * @param isShowing
     * @param time
     * @return
     */
    public static ObjectAnimator changeAlpha(View view, boolean isShowing, int time){

        float a;
        float b;
        if (isShowing){
            a = 0f;
            b = 1f;
        }else {
            a = 1f;
            b = 0f;
        }
        ObjectAnimator  animator  =  ObjectAnimator.ofFloat(view,  "alpha",  a,  b);
        animator.setDuration(time);
//        animator.start();
        return animator;
    }
}
