package com.teamxod.unilink;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class RoommateFragment extends Fragment {

    private String mNames[] = {"welcome","android","TextView"};
    private ListView listView;

    private TagViewGroup tagViewLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_roommate, container, false); // get the GUI

        ArrayList<RoommateSimp> roommates = new ArrayList<>();


        String [] pros = {"clean","quite","sleep early"};
        String [] cons = {"drink often","smoke"};
        roommates.add(new RoommateSimp("Eason Chan", "http://cdn.kingston.ac.uk/includes/img/cms/site-images/resized/e57c559-kingston-university-bfe85b4-eason-chan.jpg",
                "Computer Science", 9, 4,pros, cons));

        listView = layout.findViewById(R.id.roomate_list);

        RoommateListAdapter adapter = new RoommateListAdapter(this.getActivity(),roommates,listView);
        listView.setAdapter(adapter);
        /*tagViewLayout = (TagViewGroup)layout.findViewById(R.id.flowlayout);
        Log.d("check null", "onCreateView: "+tagViewLayout);

        initChildViews();*/
        return layout;
    }




}
