package com.teamxod.unilink;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class My_preference extends AppCompatActivity {
    ListView listView;
    ArrayList<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_preference);
        // create the list of questions
        questionList = new ArrayList<Question>();
        // add questions to the arrayList
        questionList.add(new Question("Nihao", false));
        questionList.add(new Question("Nihao", false));
        questionList.add(new Question("Nihao", false));
        questionList.add(new Question("Nihao", false));
        questionList.add(new Question("Nihao", false));
        questionList.add(new Question("Nihao", false));
        questionList.add(new Question("Nihao", false));
        questionList.add(new Question("Nihao", false));
        questionList.add(new Question("Nihao", false));
        questionList.add(new Question("Nihao", false));
        questionList.add(new Question("Nihao", false));
        questionList.add(new Question("Nihao", false));
        questionList.add(new Question("Nihao", false));
        questionList.add(new Question("Nihao", false));
        questionList.add(new Question("Nihao", false));
        questionList.add(new Question("Nihao", false));
        questionList.add(new Question("Nihao", false));


        // set adaptor to display
        preferenceAdaptor adaptor = new preferenceAdaptor(this, questionList);
        listView = (ListView) findViewById(R.id.my_preference_list);
        listView.setAdapter(adaptor);

    }
}
