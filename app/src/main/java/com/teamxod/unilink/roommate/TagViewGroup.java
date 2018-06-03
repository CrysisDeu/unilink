package com.teamxod.unilink.roommate;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
/**
 *

 */
public class TagViewGroup extends ViewGroup{

    //存储所有子View
    private List<List<View>> mAllChildViews = new ArrayList<>();

    //每一行的高度
    private List<Integer> mLineHeight = new ArrayList<>();

    public TagViewGroup(Context context) {
        this(context, null);
    }
    public TagViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public TagViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //父控件传进来的宽度和高度以及对应的测量模式
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //if current ViewGroup -> wrap_content
        int width = 0;
        int height = 0;

        //record the width and height of each line
        int lineWidth = 0;
        int lineHeight = 0;

        //get the number of child view
        int childCount = getChildCount();
        for(int i = 0;i < childCount; i ++){
            View child = getChildAt(i);

            //measure width and height of child view
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            //get LayoutParams
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            //child View width occupied
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            //child View height occupied
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;


            //change line
            if(lineWidth + childWidth > sizeWidth){

                //get largest width
                width = Math.max(width, lineWidth);
                //reset lineWidth
                lineWidth = childWidth;
                //
                height += lineHeight;
                lineHeight = childHeight;


            }else{//not change line
                //add line width
                lineWidth += childWidth;
                //get max line height
                lineHeight = Math.max(lineHeight, childHeight);
            }
            //handle the situation of last child view
            if(i == childCount -1){
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        //wrap_content
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width,
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mAllChildViews.clear();
        mLineHeight.clear();
        //get current width of ViewGroup
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        //get current line view
        List<View> lineViews = new ArrayList<View>();

        int childCount = getChildCount();

        for(int i = 0;i < childCount; i ++){
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            //change line
            if(childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width){
                //record LineHeight
                mLineHeight.add(lineHeight);
                //record current line Views
                mAllChildViews.add(lineViews);

                //reset width and height
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;

                //reset view list
                lineViews = new ArrayList();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }

        //handle the last line
        mLineHeight.add(lineHeight);
        mAllChildViews.add(lineViews);

        //set the postion of cjild View
        int left = 0;
        int top = 0;

        //get line count
        int lineCount = mAllChildViews.size();
        for(int i = 0; i < lineCount; i ++){

            //current line views and width and height
            lineViews = mAllChildViews.get(i);
            lineHeight = mLineHeight.get(i);
            for(int j = 0; j < lineViews.size(); j ++){
                View child = lineViews.get(j);

                //decide show or not
                if(child.getVisibility() == View.GONE){
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int cLeft = left + lp.leftMargin;
                int cTop = top + lp.topMargin;
                int cRight = cLeft + child.getMeasuredWidth();
                int cBottom = cTop + child.getMeasuredHeight();

                //set the layout of child view
                child.layout(cLeft, cTop, cRight, cBottom);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            left = 0;
            top += lineHeight;
        }

    }
    /**
     *
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {


        return new MarginLayoutParams(getContext(), attrs);
    }
}
