package com.modirniya.rideshareaid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.modirniya.rideshareaid.modules.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ForumActivity extends Activity {
    private EditText etMsg;
    private RecyclerView rvForum;
    private String sCityCode;

    private LinearLayoutManager mLinearLayoutManager;

    private FirebaseFunctions mFunctions;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder>
            mFirebaseAdapter;

    private enum functions {
        newEntry
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMessage, tvSender, tvTime;

        public MessageViewHolder(View v) {
            super(v);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvSender = itemView.findViewById(R.id.tvSender);
            tvTime = itemView.findViewById(R.id.tvTime);
        }

    }

    @Override
    protected void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mFirebaseAdapter.startListening();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        sCityCode = loadSetting("city_code", "unknown_location");
        initViews();
        initFirebase();
    }

    private void initFirebase() {
        // Initiate Functions
        mFunctions = FirebaseFunctions.getInstance();
        // New child entries
        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SnapshotParser<Message> parser = new SnapshotParser<Message>() {
            @NonNull
            @Override
            public Message parseSnapshot(DataSnapshot dataSnapshot) {
                Message message = dataSnapshot.getValue(Message.class);
                assert message != null;
                message.setUid(dataSnapshot.getKey());
                return message;
            }
        };
        DatabaseReference messagesRef = mFirebaseDatabaseReference.child("Forums").child(sCityCode);
        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(messagesRef, parser)
                        .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message message) {
                if (message.getText() != null) {
                    holder.tvMessage.setText(message.getText());
                    if (message.getName() != null)
                        holder.tvSender.setText(message.getName());
                    else
                        holder.tvSender.setVisibility(View.GONE);
                    holder.tvTime.setText(message.getStringTime());
                }

            }

            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new MessageViewHolder(inflater.inflate(R.layout.item_message, parent, false));
            }
        };
        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();

                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    rvForum.scrollToPosition(positionStart);
                }
            }
        });
        rvForum.setAdapter(mFirebaseAdapter);
    }

    private void initViews() {
        etMsg = findViewById(R.id.etMsg);

        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());

        rvForum = findViewById(R.id.rvForum);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        rvForum.setLayoutManager(mLinearLayoutManager);

        TextInputLayout textInputLayout = findViewById(R.id.materialTextInput);
        textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMessage();
            }
        });
    }

    private void postMessage() {
        String sEntry = etMsg.getText().toString();
        if (sEntry.length() > 400) {
            etMsg.setError("Input is too long(more than 400 characters)");
        } else if (sEntry.length() < 10) {
            etMsg.setError("Input is too short(less than 10 characters)");
        } else {
            etMsg.setText("");
            Map<String, Object> data = new HashMap<>();
            data.put("city", sCityCode);
            data.put("entry", sEntry);
            callFunctions(functions.newEntry.toString(), data);
        }
    }

    private void callFunctions(String funcName, Map<String, Object> data) {
        mFunctions
                .getHttpsCallable(funcName)
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        return (String) Objects.requireNonNull(task.getResult()).getData();
                    }
                });
    }

    public String loadSetting(String keyword, String def) {
        String result;
        if (def.equals("")) {
            result = "N/A";
        } else {
            result = def;
        }
        SharedPreferences settings = getSharedPreferences("Rideshare-Aid", 0);
        result = settings.getString(keyword, result);
        return result;
    }


}
