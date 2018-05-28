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
import android.widget.Button;
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

public class HousingFragment extends Fragment {

    ListView listView;
    LinearLayout searchBar;
    Spinner spinner;
    FloatingActionButton addPost;
    View header;
    int touchSlop = 5;

    private DatabaseReference HouseDatabase;
    private String HouseUid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_housing, container, false); // get the GUI

        //initialize
        listView = (ListView) layout.findViewById(R.id.list_view);
        searchBar = layout.findViewById(R.id.searchView);
        addPost = layout.findViewById(R.id.add_post_btn);

        final ArrayList<HousePost> posts = new ArrayList<HousePost>();



        //firebase
        HouseDatabase = FirebaseDatabase.getInstance().getReference("House");
        HouseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //create an array of post
               // ArrayList<HousePost> posts = new ArrayList<HousePost>();

                for(DataSnapshot house : dataSnapshot.getChildren()){
                    String HouseUid = house.toString();
                    String location = house.child("room_location").getValue(String.class);
                    String type = house.child("room_type").getValue(String.class);
                    String title = house.child("room_title").getValue(String.class);
                    String price = house.child("room_price").getValue(String.class);
                    String imageId = house.child("imageResourceId").getValue(String.class);
                    boolean favorite = (Boolean)house.child("isFavorite").getValue();
                    HousePost post = new HousePost(type,title,price,location,imageId,favorite);
                    //HousePost post = house.getValue(HousePost.class);
                    posts.add(post);

                }
                if(!posts.isEmpty()){
                    final HousePostAdapter adapter = new HousePostAdapter(getActivity(), posts,listView);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        //Log.d("array size", "outside: "+posts.size());


        //posts.add(new HousePost());
       /* posts.add(new HousePost("Master Bedroom", "Regents La Jolla","$1190/MO","La Jolla","https://www.sloaepi.org/wp-content/uploads/2016/11/Latest-House-Designs-Inspirations.jpg",true));
        posts.add(new HousePost("Shared Master Bedroom", "Villas of Renaissance","$690/MO","5360 Toscana way","http://www.bestinsurancecompaniesinfo.com/wp-content/uploads/2015/03/house-1.jpg",false));
        posts.add(new HousePost("single room", "beautiful room","$90090/MO","Gary's house","https://vignette.wikia.nocookie.net/animal-jam-clans-1/images/e/ea/Chic-Rich-Houses-with-Pool.jpg",true));
        posts.add(new HousePost("single room", "beautiful room","$1190/MO","San Diego","https://media.gettyimages.com/photos/exterior-view-of-custom-home-picture-id159087139",false));
        posts.add(new HousePost("single room", "beautiful room","$1190/MO","San Diego","http://www.bestinsurancecompaniesinfo.com/wp-content/uploads/2015/03/house-1.jpg",false));
        posts.add(new HousePost("single room", "beautiful room","$1190/MO","San Diego","https://www.sloaepi.org/wp-content/uploads/2016/11/Latest-House-Designs-Inspirations.jpg",false));
*/



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
                        Toast.makeText(getContext(),"Click"+parentView.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                        break;
                    //select price high to low
                    case 2:
                        break;
                    //select term short to long
                    case 3:
                        break;
                    //select term long to short
                    case 4:
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
                //myIntent.putExtra("uid", );
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