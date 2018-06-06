package com.teamxod.unilink.roommate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.progresviews.ProgressWheel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamxod.unilink.R;
import com.teamxod.unilink.user.Preference;
import com.teamxod.unilink.user.Profile;
import com.teamxod.unilink.user.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

class RoommateListAdapter extends BaseAdapter {

    private final static int MARGIN = 15;
    private final ArrayList<String> mList;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private DatabaseReference database;

    RoommateListAdapter(Context context, List<String> objects) {
        mList = (ArrayList<String>) objects;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        final ViewHolder viewHolder = new ViewHolder();
        view = mInflater.inflate(R.layout.roommate_list_item, null);
        viewHolder.vhPic = view.findViewById(R.id.headShot);
        viewHolder.vhName = view.findViewById(R.id.rName);
        viewHolder.vhGender = view.findViewById(R.id.rMajor);
        viewHolder.vhProgress = view.findViewById(R.id.rProgress);
        viewHolder.vhScore = view.findViewById(R.id.rScore);
        viewHolder.vhYear = view.findViewById(R.id.rYear);
        viewHolder.tag = view.findViewById(R.id.tag);
        view.setTag(viewHolder);


        final String roommateUID = (String) getItem(position);

        // click avatar to show profile
        viewHolder.vhPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, Profile.class);
                i.putExtra("uid", roommateUID);
                mContext.startActivity(i);
            }
        });


        database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userReference = database.child("Users").child(roommateUID);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) return;
                loadData(viewHolder, user, roommateUID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void loadData(final ViewHolder viewHolder, User roommate, final String roommateUID) {
        //glide image
        Glide.with(mContext)
                .load(roommate.getPicture())
                .apply(RequestOptions.circleCropTransform())
                .into(viewHolder.vhPic);

        int enterYear = Integer.parseInt(roommate.getYearGraduate()) - 4;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR) - enterYear;
        String temp;
        if (currentYear > 5)
            temp = "Alumni";
        else if (currentYear <= 0)
            temp = "Incoming Student";
        else
            temp = "Class of " + roommate.getYearGraduate();
        viewHolder.vhYear.setText(temp);

        viewHolder.vhName.setText(roommate.getName());
        viewHolder.vhGender.setText(roommate.getGender());

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String myUid = auth.getCurrentUser().getUid();

        DatabaseReference preferenceReference = database.child("Preference");
        preferenceReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                double score;
                ArrayList<String> tagList;

                Preference myPreference = dataSnapshot.child(myUid).getValue(Preference.class);
                Preference posterPreference = dataSnapshot.child(roommateUID).getValue(Preference.class);

                if (myPreference != null && posterPreference != null) {
                    Recommendation recommendation = new Recommendation(myPreference, posterPreference);
                    score = recommendation.getScore();
                    tagList = recommendation.getTagList();
                    DecimalFormat df = new DecimalFormat("0.00");
                    String temp = df.format(score);
                    viewHolder.vhScore.setText(temp);
                } else {
                    score = 0;
                    tagList = new ArrayList<>();
                    viewHolder.vhScore.setText("?");
                }

                viewHolder.vhProgress.setPercentage((int) (score * 36));

                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = MARGIN;
                lp.rightMargin = MARGIN;
                lp.topMargin = MARGIN;
                lp.bottomMargin = MARGIN;
                for (int i = 0; i < tagList.size(); i++) {
                    TextView tagView = new TextView(mContext);
                    tagView.setText(tagList.get(i));
                    tagView.setTextAppearance(R.style.tag_text);

                    tagView.setBackgroundResource(R.drawable.roommate_tag_layout);
                    viewHolder.tag.addView(tagView, lp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //default
            }
        });
    }

    class ViewHolder {

        ImageView vhPic;
        TextView vhName;
        TextView vhGender;
        TextView vhYear;
        ProgressWheel vhProgress;
        TextView vhScore;
        TagViewGroup tag;
    }
}
