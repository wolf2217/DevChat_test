package com.commandcenter.devchat.Adapter;

import android.content.Context;
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
    Context context;

    public FirebaseMessageAdapter(Context context, List<ChatboxMessage> messageList) {
        this.context = context;
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
        holder.tv_message.setText(message.getChatMessage());
        holder.tv_username.setText(message.getUser());
        holder.tv_rank.setText("Admin");
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_message, tv_username, tv_rank;

        public MessageViewHolder(View itemView) {
            super(itemView);

            tv_message  = itemView.findViewById(R.id.chatbox_single_message_tv_message);
            tv_username = itemView.findViewById(R.id.chatbox_single_row_tv_username);
            tv_rank     = itemView.findViewById(R.id.chatbox_single_row_tv_rank);
        }
    }
}
