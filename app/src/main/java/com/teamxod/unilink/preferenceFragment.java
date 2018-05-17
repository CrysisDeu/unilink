package com.teamxod.unilink;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class preferenceFragment extends Fragment{
    ListView listView;
    ArrayList<Question> questionList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my_preference, container, false);
        // create the list of questions
        questionList = new ArrayList<Question>();
        // add questions to the arrayList
        questionList.add(new Question("Nihao", false));

        // set adaptor to display
        preferenceAdaptor adaptor = new preferenceAdaptor(this.getActivity(), questionList);
        listView  = (ListView)layout.findViewById(R.id.my_preference_list);
        listView.setAdapter(adaptor);

        return listView;
    }

}
