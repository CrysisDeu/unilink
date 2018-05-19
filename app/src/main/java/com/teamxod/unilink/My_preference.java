package com.teamxod.unilink;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.ArrayList;

public class My_preference extends AppCompatActivity {
    ListView listView;
    ArrayList<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_preference);
        // create the list of questions
        questionList = new ArrayList<Question>();
        // add questions to the arrayList
        questionList.add(new Question("Nihao", "diyi", "dier"));

        // set adaptor to display
        preferenceAdaptor adaptor = new preferenceAdaptor(this, questionList);
        listView = (ListView) findViewById(R.id.my_preference_list);
        listView.setAdapter(adaptor);

        /**change state while selecting
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView
            }
        });*/

    }
}
