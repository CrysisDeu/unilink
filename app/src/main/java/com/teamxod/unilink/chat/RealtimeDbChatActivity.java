package com.teamxod.unilink.chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.util.ui.ImeHelper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.teamxod.unilink.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Class demonstrating how to setup a {@link RecyclerView} with an adapter while taking sign-in
 * states into consideration. Also demonstrates adding data to a ref and then reading it back using
 * the {@link FirebaseRecyclerAdapter} to build a simple chat app.
 * <p>
 * For a general intro to the RecyclerView, see <a href="https://developer.android.com/training/material/lists-cards.html">Creating
 * Lists</a>.
 */
public class RealtimeDbChatActivity extends AppCompatActivity
        implements FirebaseAuth.AuthStateListener {
    private static final String TAG = "RealtimeDatabase";

    static Query sChatQuery;
    static final DatabaseReference messages= FirebaseDatabase.getInstance().getReference().child("Messages");
    static final DatabaseReference chats = FirebaseDatabase.getInstance().getReference().child("Chat");

    private String other_id;
    private String other_name;
    private String uid;
    private String name;
    private TextView mTitleName;
    private ImageView mBackButton;
    private Uri other_photo;
    private Uri photo;

    @BindView(R.id.messagesList)
    RecyclerView mRecyclerView;

    @BindView(R.id.sendButton)
    Button mSendButton;

    @BindView(R.id.messageEdit)
    EditText mMessageEdit;

    @BindView(R.id.emptyTextView)
    TextView mEmptyListMessage;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();

        // get user uid and name
        other_id = intent.getStringExtra("user_id");
        other_name = intent.getStringExtra("user_name");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        DatabaseReference user_reference = FirebaseDatabase.getInstance().getReference().child("Users");
        user_reference.child(other_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                other_photo = Uri.parse(dataSnapshot.child("picture").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        user_reference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                photo = Uri.parse(dataSnapshot.child("picture").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sChatQuery =  FirebaseDatabase.getInstance().getReference().child("Messages").child(uid).child(other_id).limitToLast(100);
        mTitleName = findViewById(R.id.title_user_name);
        mTitleName.setText(other_name);

        // back button
        mBackButton = findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // send button
        ImeHelper.setImeOnDoneListener(mMessageEdit, new ImeHelper.DonePressedListener() {
            @Override
            public void onDonePressed() {
                onSendClick();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isSignedIn()) { attachRecyclerViewAdapter(); }
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }


    private boolean isSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    private void attachRecyclerViewAdapter() {
        final RecyclerView.Adapter adapter = newAdapter();

        // Scroll to bottom on new messages
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });

        mRecyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.sendButton)

    // send button
    public void onSendClick() {
        if (mMessageEdit.getText().toString().matches("")) {
            Toast.makeText(this, "Please send a non-empty message!", Toast.LENGTH_SHORT).show();
            return;
        }

        // get timestamp
        long tsLong = System.currentTimeMillis()/1000;

        // add to firebase
        onAddMessage(tsLong);
        mMessageEdit.setText("");
    }

    RecyclerView.Adapter newAdapter() {
        FirebaseRecyclerOptions<Chat> options =
                new FirebaseRecyclerOptions.Builder<Chat>()
                        .setQuery(sChatQuery, Chat.class)
                        .setLifecycleOwner(this)
                        .build();

        return new FirebaseRecyclerAdapter<Chat, ChatHolder>(options) {
            @NonNull
            @Override
            public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ChatHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull Chat model) {
                holder.bind(model);
                holder.setImage(model, RealtimeDbChatActivity.this, other_photo, photo);
            }

            @Override
            public void onDataChanged() {
                // If there are no chat messages, show a view that invites the user to add a message.
                mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };
    }

    void onAddMessage(long time) {
        DatabaseReference self;
        DatabaseReference other;

        // create key
        self = messages.child(uid).child(other_id).push();
        other = messages.child(other_id).child(uid).child(self.getKey());
        Chat chat = new Chat(name, mMessageEdit.getText().toString(), uid, time);

        // save to database
        self.setValue(chat);
        other.setValue(chat);

        // update timestamp
        chats.child(uid).child(other_id).child("mTimestamp").setValue(time);
        chats.child(other_id).child(uid).child("mTimestamp").setValue(time);

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

    }
}
