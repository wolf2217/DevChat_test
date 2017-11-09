package com.commandcenter.devchat.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.commandcenter.devchat.Model.ChatboxMessage;
import com.commandcenter.devchat.R;

import java.util.List;

public class FirebaseMessageAdapter extends RecyclerView.Adapter<FirebaseMessageAdapter.MessageViewHolder>{

    List<ChatboxMessage> messageList;

    public FirebaseMessageAdapter(List<ChatboxMessage> messageList) {
        this.messageList = messageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatbox_single_message_row, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {

        ChatboxMessage message = messageList.get(position);
        holder.tv_message.setText(message.getUser() + ": " + message.getChatMessage());
        holder.iv_avatar.setImageResource(R.drawable.ic_person);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_avatar;
        public TextView tv_message;

        public MessageViewHolder(View itemView) {
            super(itemView);

            iv_avatar = itemView.findViewById(R.id.chatbox_single_message_avatar);
            tv_message = itemView.findViewById(R.id.chatbox_single_message_tv_message);
        }
    }
}
