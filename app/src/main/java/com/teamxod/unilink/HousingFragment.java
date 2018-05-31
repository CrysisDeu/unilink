package com.teamxod.unilink;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HousingFragment extends Fragment {

    ListView listView;
    LinearLayout searchBar;
    Spinner spinner;
    FloatingActionButton addPost;
    View header;
    int touchSlop = 5;
    final ArrayList<HousePost> posts = new ArrayList<>();

    private DatabaseReference HouseDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_housing, container, false); // get the GUI

        //initialize
        listView = (ListView) layout.findViewById(R.id.list_view);
        searchBar = layout.findViewById(R.id.searchView);
        addPost = layout.findViewById(R.id.add_post_btn);

        //firebase
        DatabaseReference HouseDatabase = FirebaseDatabase.getInstance().getReference("House_post");
        HouseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //create an array of post
               // ArrayList<HousePost> posts = new ArrayList<HousePost>();

                posts.clear();
                for(DataSnapshot house : dataSnapshot.getChildren()){
                    String key = house.getKey();
                    String location = house.child("location").getValue(String.class);
                    String type = house.child("houseType").getValue(String.class);
                    String title = house.child("title").getValue(String.class);
                    String term = house.child("leasingLength").getValue(String.class);
                    int price = (int)house.child("rooms").child("0").child("price").getValue(Integer.class);
                    String imageId = house.child("pictures").child("0").getValue(String.class);
                    boolean favorite = true;
                    HousePost post = new HousePost(key,type,title,price,term,location,imageId,favorite);
                    posts.add(post);
                }

                if(!posts.isEmpty()){
                    HousePostAdapter adapter = new HousePostAdapter(getActivity(), posts,listView);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        addPost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), AddPost.class);
                startActivity(myIntent);
            }
        });

       // Log.d("array size", "outside: "+posts.size());

        // listView.setOnScrollListener(adapter);

        spinner = (Spinner)layout.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Sort, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(spinnerAdapter);
        //set listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                switch(spinner.getSelectedItemPosition()) {
                    //select sort --> do nothing
                    case 0:
                        break;
                    //select price low to high
                    case 1:
                        sortPrice(true);
                        Toast.makeText(getContext(),"Click"+parentView.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                        break;
                    //select price high to low
                    case 2:
                        sortPrice(false);
                        Toast.makeText(getContext(),"Click"+parentView.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                        break;
                    //select term short to long
                    case 3:
                        Toast.makeText(getContext(),"Click"+parentView.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                        break;
                    //select term long to short
                    case 4:
                        Toast.makeText(getContext(),"Click"+parentView.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        header = new View(this.getActivity());
        header.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.abc_action_bar_default_height_material)));
        header.setBackgroundColor(Color.parseColor("#00000000"));
        listView.addHeaderView(header);
        touchSlop = (int) (ViewConfiguration.get(HousingFragment.this.getActivity()).getScaledTouchSlop() * 0.9);

        //set touch and scroll event for listView
        listView.setOnTouchListener(onTouchListener);
        //listView.setOnScrollListener(onScrollListener);

        //set onClickListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent myIntent = new Intent(view.getContext(), SingleHousePostActivity.class);

                myIntent.putExtra("uid",posts.get(position-1).getRoom_key());
                startActivity(myIntent);

            }
        });

        /*filterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), SingleHousePostActivity.class);
                startActivity(myIntent);
            }
        });*/
        return layout;
    }

    //sort method
    private void sortPrice(boolean order){

        Collections.sort(posts, new Comparator<HousePost>() {
            @Override
            public int compare(HousePost p1, HousePost p2) {
                return p1.getRoom_price() - p2.getRoom_price();
            }
        });
        if(!order) {
            Collections.reverse(posts);
        }

        HousePostAdapter adapter = new HousePostAdapter(getActivity(), posts,listView);
        listView.setAdapter(adapter);
    }
    /*
    private void sortTerm(boolean order){
        Collections.sort(posts, new Comparator<HousePost>() {
            @Override
            public int compare(HousePost p1, HousePost p2) {
                return p1.getRoom_price() - p2.getRoom_price();
            }
        });
        if(!order) {
            Collections.reverse(posts);
        }

        HousePostAdapter adapter = new HousePostAdapter(getActivity(), posts,listView);
        listView.setAdapter(adapter);
    }*/


    //set the back animator
    AnimatorSet backAnimatorSet;

    private void animateBack() {
        //eliminate other animator
        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
            hideAnimatorSet.cancel();
        }
        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {

        } else {
            backAnimatorSet = new AnimatorSet();

            //move the element back to originial position
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(searchBar, "translationY", searchBar.getTranslationY(), 0f);
            ObjectAnimator addPostAnimator = ObjectAnimator.ofFloat(addPost, "translationY", addPost.getTranslationY(), 0f);

            //add animator object to arraylist
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(headerAnimator);
            animators.add(addPostAnimator);


            backAnimatorSet.setDuration(400);
            backAnimatorSet.playTogether(animators);
            backAnimatorSet.start();
        }
    }


    //aniamtor to hide element
    AnimatorSet hideAnimatorSet;

    private void animateHide() {
        //eliminate other animators
        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
            backAnimatorSet.cancel();
        }
        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {

        } else {
            hideAnimatorSet = new AnimatorSet();
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(searchBar, "translationY", searchBar.getTranslationY(), -2* searchBar.getHeight());
            ObjectAnimator addPostAnimator = ObjectAnimator.ofFloat(addPost, "translationY", addPost.getTranslationY(), 2*addPost.getHeight());

            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(headerAnimator);
            animators.add(addPostAnimator);

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


  /*  //set onScrollListener
    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        //meet the situation when user's finger leaves screen,
        //but the screen is still scrolling
        int lastPosition = 0;
        int state = SCROLL_STATE_IDLE;
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //record current list statement
            state = scrollState;
        }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem == 0) {
                animateBack();
            }
            if (firstVisibleItem > 0) {
                if (firstVisibleItem > lastPosition && state == SCROLL_STATE_FLING) {
                    //if the position of last time is smaller than current, hide
                    animateHide();
                }
            }
            lastPosition = firstVisibleItem;
        }
    };*/

}