package com.teamxod.unilink;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        questionList.add(new Question("Do U like pets?", "yes", "no"));
        questionList.add(new Question("Do U cook?", "yes", "no"));
        questionList.add(new Question("Will U bring others overnight?", "yes", "No, maybe if I get lucky"));
        questionList.add(new Question("Do U smoke?", "yes", "no"));

        questionList.add(new Question("Do U like pets?", "yes", "no"));
        questionList.add(new Question("Do U cook?", "yes", "no"));
        questionList.add(new Question("Will U bring others overnight?", "yes", "No, maybe if I get lucky"));
        questionList.add(new Question("Do U smoke?", "yes", "no")); questionList.add(new Question("Do U like pets?", "yes", "no"));
        questionList.add(new Question("Do U cook?", "yes", "no"));
        questionList.add(new Question("Will U bring others overnight?", "yes", "No, maybe if I get lucky"));
        questionList.add(new Question("Do U smoke?", "yes", "no"));
        // set adaptor to display
        preferenceAdaptor adaptor = new preferenceAdaptor(this, questionList);
        listView = (ListView) findViewById(R.id.my_preference_list);
        listView.setAdapter(adaptor);
    }
}
