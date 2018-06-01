package com.teamxod.unilink;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class MyPostFragment extends Fragment {

    private RecyclerView houseListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my_post, container, false);

        setupButton(layout);

        loadData(layout);

        return layout;
    }

    private void setupButton(View layout) {
        ImageView mBackButton = (ImageView)layout.findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Fragment fragment = getFragmentManager().findFragmentByTag("my_post");
                getFragmentManager()
                        .beginTransaction()
                        .remove(fragment)
                        .commit();
            }
        });

        ToggleButton mEditButton = (ToggleButton) layout.findViewById(R.id.delete_btn);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void loadData(View layout) {

    }
}
