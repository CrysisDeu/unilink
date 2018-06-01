package com.teamxod.unilink;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class RoommateFragment extends Fragment {

    private View header;
    private int touchSlop = 5;
    private ListView listView;

    private Button preference;
    private ToggleButton toggle;

    private RelativeLayout layer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_roommate, container, false); // get the GUI

        listView = layout.findViewById(R.id.roomate_list);
        preference = layout.findViewById(R.id.roommate_preference);
        toggle = layout.findViewById(R.id.toggle_score);
        layer = layout.findViewById(R.id.layer);

        ArrayList<RoommateSimple> roommates = new ArrayList<>();


        String [] pros = {"clean","quite","sleep early"};
        String [] cons = {"drink often","smoke"};
        roommates.add(new RoommateSimple("Eason Chan", "http://cdn.kingston.ac.uk/includes/img/cms/site-images/resized/e57c559-kingston-university-bfe85b4-eason-chan.jpg",
                "Computer Science", 9, 4,pros, cons));


        RoommateListAdapter adapter = new RoommateListAdapter(this.getActivity(),roommates,listView);
        listView.setAdapter(adapter);

        preference.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),My_preference.class);
                startActivity(i);
            }
        });

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //show the user in roommate list
                if(isChecked){

                }
                //eliminate
                else{

                }
            }
        });

        header = new View(this.getActivity());
        header.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.header_roommate)));
        header.setBackgroundColor(Color.parseColor("#00000000"));
        listView.addHeaderView(header);
        touchSlop = (int) (ViewConfiguration.get(RoommateFragment.this.getActivity()).getScaledTouchSlop() * 0.9);
        return layout;
    }



    //set the back animator
    private AnimatorSet backAnimatorSet;

    private void animateBack() {
        //eliminate other animator
        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
            hideAnimatorSet.cancel();
        }
        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {

        } else {
            backAnimatorSet = new AnimatorSet();

            //move the element back to originial position
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(layer, "translationY", layer.getTranslationY(), 0f);

            //add animator object to arraylist
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(headerAnimator);


            backAnimatorSet.setDuration(400);
            backAnimatorSet.playTogether(animators);
            backAnimatorSet.start();
        }
    }


    //aniamtor to hide element
    private AnimatorSet hideAnimatorSet;

    private void animateHide() {
        //eliminate other animators
        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
            backAnimatorSet.cancel();
        }
        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {

        } else {
            hideAnimatorSet = new AnimatorSet();
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(layer, "translationY", layer.getTranslationY(), -2*layer.getHeight());

            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(headerAnimator);

            hideAnimatorSet.setDuration(400);
            hideAnimatorSet.playTogether(animators);
            hideAnimatorSet.start();
        }
    }



    //set up onTouchListener
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {


        float lastY = 0f;
        float currentY = 0f;
        //represent two scroll direction  >0 : scroll down; <0 : scroll up
        int lastDirection = 0;
        int currentDirection = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastY = event.getY();
                    currentY = event.getY();
                    currentDirection = 0;
                    lastDirection = 0;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (listView.getFirstVisiblePosition() > 0) {
                        //hide or show element only if listView.getFirstVisiblePosition()>0
                        float tmpCurrentY = event.getY();
                        //start only if movement didtance > toushSlop
                        if (Math.abs(tmpCurrentY - lastY) > touchSlop) {
                            currentY = tmpCurrentY;
                            currentDirection = (int) (currentY - lastY);
                            if (lastDirection != currentDirection) {
                                //if the direction of movement is different from lat time, then hide or show elements
                                if (currentDirection < 0) {
                                    animateHide();
                                } else {
                                    animateBack();
                                }
                            }
                            lastY = currentY;
                        }
                    }
                    break;

                case MotionEvent.ACTION_CANCEL:

            }
            return false;
        }
    };



}
