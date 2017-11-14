package com.commandcenter.devchat.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
        holder.tv_rank.setText(message.getRank());
        holder.getImageView().setImageResource(R.drawable.ic_person);
        if (message.getRank().equalsIgnoreCase("Admin")) {
            holder.card.setCardBackgroundColor(Color.parseColor("#DBA120"));
            holder.tv_rank.setTextColor(Color.parseColor("#4F3A0C"));
            holder.tv_username.setTextColor(Color.WHITE);
            holder.tv_message.setTextColor(Color.parseColor("#B55106"));
            holder.getImageView().setImageResource(R.drawable.ic_face_black_24dp);

        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_message, tv_username, tv_rank;
        public ImageView iv_avatar;
        public CardView card;

        public MessageViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.chatbox_message_cardview);
            tv_message  = itemView.findViewById(R.id.chatbox_single_message_tv_message);
            tv_username = itemView.findViewById(R.id.chatbox_single_row_tv_username);
            tv_rank     = itemView.findViewById(R.id.chatbox_single_row_tv_rank);
            iv_avatar   = itemView.findViewById(R.id.chatbox_single_row_iv_avatar);
        }

        public ImageView getImageView() {
            return iv_avatar;
        }
    }
}
