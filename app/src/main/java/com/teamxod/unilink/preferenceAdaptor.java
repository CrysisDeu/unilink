package com.teamxod.unilink;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.List;

class Question{
    // q is the question and choiceOne/Two are choices
    // booleans are indicators for boxes checked or not. Stored to implement check only one functionality
    String q;
    String choiceOne;
    String choiceTwo;
    boolean firstChecked;
    boolean secondChecked;

    public Question(String q, String choiceOne, String choiceTwo) {
        this.q = q;
        this.firstChecked = false;
        this.secondChecked = false;
        this.choiceOne = choiceOne;
        this.choiceTwo = choiceTwo;
    }
    // getter and setter for fields
    public String getQuestion(){
        return this.q;
    }

    public String getChoiceOne() { return this.choiceOne;}

    public String getChoiceTwo() { return this.choiceTwo;}

    public boolean getfirstChecked(){
        return this.firstChecked;
    }

    public boolean getSecondChecked() {return this.secondChecked;}

    public void setQuestion(String q){
        this.q = q;
    }

    public void setChoiceOne(String s) { this.choiceOne = s;}

    public void setChoiceTwo(String s) { this.choiceTwo = s;}

    public void setfirstChecked(boolean checked){
        this.firstChecked = checked;
    }

    public void setSecondChecked(boolean checked) {this.secondChecked = checked;}


}
public class preferenceAdaptor extends ArrayAdapter<Question>{
        private List<Question> questionList;
        private Context context;

        public preferenceAdaptor(Context context, List<Question> questionList) {
            super(context, 0, questionList);
        }

    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        // inflate the view if null
        View listItem = view;
        if(listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(
                    R.layout.preference_list_item_check_two, parent, false);
        }
        // set Question's text fields
        final Question preference = (Question)getItem(position);

        TextView q = (TextView)listItem.findViewById((R.id.question));
        q.setText(preference.getQuestion());

        CheckedTextView checkOne = (CheckedTextView)listItem.findViewById(R.id.checked1);
        checkOne.setText(preference.getChoiceOne());

        CheckedTextView checkTwo = (CheckedTextView)listItem.findViewById((R.id.checked2));
        checkTwo.setText(preference.getChoiceTwo());

        checkOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!preference.getfirstChecked()){
                    preference.setfirstChecked(true);
                    preference.setSecondChecked(false);
                }else{
                    preference.setfirstChecked(false);
                }
            }
        });

        checkTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!preference.getSecondChecked()) {
                    preference.setfirstChecked(false);
                    preference.setSecondChecked(true);
                }else{
                    preference.setSecondChecked(false);
                }
            }
        });

        return listItem;
    }
}
