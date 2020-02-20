package com.amanaggarwal1.flashchat;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import static android.view.Gravity.END;
import static android.view.Gravity.getAbsoluteGravity;

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mSnapshotList;

    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity activity, DatabaseReference reference, String name){
        mActivity = activity;
        mDatabaseReference = reference.child("messages");
        mDatabaseReference.addChildEventListener(mListener);
        mDisplayName = name;
        mSnapshotList = new ArrayList<>();
    }

    static class ViewHolder{
        TextView author;
        TextView chat;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return mSnapshotList.size();
    }

    @Override
    public InstantMessage getItem(int position) {
        DataSnapshot snapshot = mSnapshotList.get(position);
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = mActivity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.chat_msg_row, parent, false);

            final ViewHolder holder = new ViewHolder();
            holder.author = convertView.findViewById(R.id.author);
            holder.chat = convertView.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams) holder.author.getLayoutParams();

            convertView.setTag(holder);
        }

        final InstantMessage message = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        boolean isMe = message.getAuthor().equals(mDisplayName);
        setChatRowAppearance(isMe, holder);

        holder.author.setText(message.getAuthor());
        holder.chat.setText(message.getMessage());

        return convertView;
    }

    private void setChatRowAppearance(boolean isMe, ViewHolder holder) {
        if(isMe){
            holder.params.gravity = Gravity.END;
            holder.author.setTextColor(Color.GREEN);
            holder.chat.setBackgroundResource(R.drawable.bubble2);
        }else{
            holder.params.gravity = Gravity.START;
            holder.author.setTextColor(Color.BLUE);
            holder.chat.setBackgroundResource(R.drawable.bubble1);
        }

        holder.author.setLayoutParams(holder.params);
        holder.chat.setLayoutParams(holder.params);
    }



    public void freeUp(){ mDatabaseReference.removeEventListener(mListener);}
}
