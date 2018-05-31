package com.teamxod.unilink;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class RoommateFragment extends Fragment {

    private String mNames[] = {"welcome","android","TextView"};
    private ListView listView;

    private Button preference;
    private ToggleButton toggle;

    private TagViewGroup tagViewLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_roommate, container, false); // get the GUI

        listView = layout.findViewById(R.id.roomate_list);
        preference = layout.findViewById(R.id.roommate_preference);
        toggle = layout.findViewById(R.id.toggle_score);


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
        return layout;
    }




}
