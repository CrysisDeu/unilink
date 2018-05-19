package com.teamxod.unilink;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

class Question{
    // q is the acual question and checked is the indicator to show if this question is checked or not
    String q;
    boolean checked;

    public Question(String q, boolean checked) {
        this.q = q;
        this.checked = checked;
    }

    public String getQuestion(){
        return this.q;
    }

    public boolean getChecked(){
        return this.checked;
    }

    public void setQuestion(String q){
        this.q = q;
    }

    public void setChecked(boolean checked){
        this.checked = checked;
    }


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
                    R.layout.preference_list_item_check, parent, false);
        }
        Question preference = (Question)getItem(position);
        TextView q = (TextView)listItem.findViewById((R.id.question));
        q.setText(preference.getQuestion());
        CheckBox checkBox = (CheckBox)listItem.findViewById(R.id.checkBox);

        return listItem;
    }
}
