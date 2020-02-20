package com.amanaggarwal1.flashchat;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.os.Build.VERSION_CODES.O;
import static com.amanaggarwal1.flashchat.RegisterActivity.DISPLAY_NAME_KEY;


public class MainChatActivity extends AppCompatActivity {


    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference mDatabaseReference;
    private ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        setupDisplayName();

        // Link the Views in the layout to the Java code
        mInputText = findViewById(R.id.messageInput);
        mSendButton = findViewById(R.id.sendButton);
        mChatListView = findViewById(R.id.chat_list_view);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                sendMessage();
                return true;
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }


    private void setupDisplayName(){
        SharedPreferences preferences = getSharedPreferences(RegisterActivity.CHAT_PREFS, 0);
        mDisplayName = preferences.getString(RegisterActivity.DISPLAY_NAME_KEY, null);
        if(mDisplayName.equals("")) mDisplayName = "Black Hat";
    }


    private void sendMessage() {
        Log.d(getString(R.string.logcat), "Sending Message");
        String message = mInputText.getText().toString();
        if(!message.trim().isEmpty()){
            InstantMessage newMessage = new InstantMessage(mDisplayName, message);
            mDatabaseReference.child("messages").push().setValue(newMessage);
            mInputText.setText(null);
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        chatListAdapter = new ChatListAdapter(this, mDatabaseReference, mDisplayName);
        mChatListView.setAdapter(chatListAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

        //Remove the Firebase event listener on the adapter.
        chatListAdapter.freeUp();
    }

}
