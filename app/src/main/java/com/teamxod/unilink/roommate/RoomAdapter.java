package com.teamxod.unilink.roommate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamxod.unilink.R;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private final ArrayList<Room> rooms;

    public RoomAdapter(Context context, ArrayList<Room> rooms) {
        this.rooms = rooms;
        Context context1 = context;
    }

    @NonNull
    @Override
    public RoomAdapter.RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.house_room_list_item, parent, false);

        return new RoomAdapter.RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.RoomViewHolder holder, int position) {

        Room room = rooms.get(position);

        holder.typeTextView.setText(room.getRoomType());

        String price = "$ " + room.getPrice() + "/MO";
        holder.priceTextView.setText(price);

    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    class RoomViewHolder extends RecyclerView.ViewHolder {
        final TextView typeTextView;
        final TextView priceTextView;

        RoomViewHolder(View itemView) {
            super(itemView);
            typeTextView = itemView.findViewById(R.id.room_type);
            priceTextView = itemView.findViewById(R.id.room_price);
        }
    }
}
