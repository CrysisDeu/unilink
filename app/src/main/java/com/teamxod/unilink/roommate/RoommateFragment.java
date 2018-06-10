package com.teamxod.unilink.roommate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamxod.unilink.R;
import com.teamxod.unilink.user.ChangePreferenceActivity;
import com.teamxod.unilink.user.Preference;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class RoommateFragment extends Fragment {

    private int touchSlop = 5;
    private ListView listView;
    private Switch visible_btn;

    private LinearLayout layer;
    private ArrayList<String> roommateUID;
    private String myUid;

    private DatabaseReference visibleReference;
    private DatabaseReference preferenceReference;

    private boolean hasPreference;


    private PriorityQueue<Pair> queue;
    private int user_count;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_roommate, container, false); // get the GUI

        listView = layout.findViewById(R.id.roommate_list);
        visible_btn = layout.findViewById(R.id.visible);
        layer = layout.findViewById(R.id.layer);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        visibleReference = database.child("Visible");
        myUid = auth.getCurrentUser().getUid();
        preferenceReference = database.child("Preference");


        roommateUID = new ArrayList<>();
        queue = new PriorityQueue<>(1000, new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                return (int) ((o2.getValue() - o1.getValue()) * 1000);
            }
        });

        setButton(layout);

        checkPreference();

        return layout;
    }

    private void loadData() {
        visibleReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                roommateUID.clear();
                ArrayList<Double> scores = new ArrayList<>(0);

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userID = userSnapshot.getKey();
                    if (userSnapshot.getValue(Boolean.class) && !userID.equals(myUid)) {
                        roommateUID.add(userID);
//                        scores.add(score);
//                        Log.d("SCORE", Double.toString(score));
                    }
                    if (getActivity() == null) {
                        return;
                    }
                }

//                Log.d("SIZE", Integer.toString(roommateUID.size()));
                user_count = 0;

                int i = 0;
                for (String otherUID : roommateUID) {
                    getScore(myUid, otherUID, i, roommateUID.size());
                    i++;
                }

                boolean isVisible;
                if (dataSnapshot.hasChild(myUid))
                    isVisible = dataSnapshot.child(myUid).getValue(Boolean.class);
                else
                    isVisible = false;
                setupVisible(isVisible);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }


    private void getScore(final String myUid, final String otherUid, final int index, final int total) {

        preferenceReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Preference myPreference = dataSnapshot.child(myUid).getValue(Preference.class);
                Preference posterPreference = dataSnapshot.child(otherUid).getValue(Preference.class);

                Recommendation recommendation = new Recommendation(myPreference, posterPreference);
                queue.add(new Pair(index, recommendation.getScore()));
//                Log.d("WHY", "onDataChange: " + queue.size());
                user_count++;

                if (index + 1 == total) {

                    final ArrayList<String> sortUID = new ArrayList<>();

                    while (!queue.isEmpty()) {
                        Pair pair = queue.poll();
//                        Log.d("SORT", Double.toString(pair.getValue()));
                        sortUID.add(roommateUID.get(pair.getKey()));
                        RoommateListAdapter adapter = new RoommateListAdapter(getActivity(), sortUID);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                Intent myIntent = new Intent(view.getContext(), RoommatePostActivity.class);
                                myIntent.putExtra("uid", sortUID.get(position));
                                startActivity(myIntent);
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //default
            }
        });
    }

    private void setupVisible(boolean isVisible) {
        visible_btn.setChecked(isVisible);

        visible_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasPreference) {
                    visible_btn.setChecked(false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Without Preference information, we are unable to provide you with the best possible roommate" +
                            " and you will be invisible to other users");
                    builder.setPositiveButton("Set Preference", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            Intent i = new Intent(getActivity(), ChangePreferenceActivity.class);
                            startActivity(i);
                        }
                    });
                    builder.setNegativeButton("Still Look Around", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (visible_btn.isChecked()) {
                    visibleReference.child(myUid).setValue(true);
                } else {
                    visibleReference.child(myUid).setValue(false);
                }
            }
        });
    }

    private void checkPreference() {
        preferenceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(myUid)) {
                    hasPreference = true;
                    loadData();

                } else {
                    hasPreference = false;
                    if (getActivity() == null) return;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Without preference information, we are unable to provide you with the best possible roommate" +
                            " and you will be invisible to other users.");
                    builder.setPositiveButton("Set Preference", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            Intent i = new Intent(getActivity(), ChangePreferenceActivity.class);
                            startActivity(i);
                        }
                    });
                    builder.setNegativeButton("Still Look Around", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    loadNaN();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadNaN() {
        visibleReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                roommateUID.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userID = userSnapshot.getKey();
                    if (userSnapshot.getValue(Boolean.class) && !userID.equals(myUid))
                        roommateUID.add(userID);
                    if (getActivity() == null) {
                        return;
                    }
                    //sort array

                    RoommateListAdapter adapter = new RoommateListAdapter(getActivity(), roommateUID);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Intent myIntent = new Intent(view.getContext(), RoommatePostActivity.class);

                                myIntent.putExtra("uid", roommateUID.get(position));
                            
                            startActivity(myIntent);
                        }
                    });
                }

                boolean isVisible;
                if (dataSnapshot.hasChild(myUid))
                    isVisible = dataSnapshot.child(myUid).getValue(Boolean.class);
                else
                    isVisible = false;
                setupVisible(isVisible);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void setButton(View layout) {

        Button preference_btn = layout.findViewById(R.id.roommate_preference);
        preference_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChangePreferenceActivity.class);
                startActivity(i);
            }
        });
    }



}
