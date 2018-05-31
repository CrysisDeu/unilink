package com.teamxod.unilink;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder>{
    private ArrayList<Room> rooms;

    Context context;

    RoomAdapter(Context context, ArrayList<Room> rooms){
        this.rooms = rooms;
        this.context = context;
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
        TextView typeTextView;
        TextView priceTextView;

        RoomViewHolder (View itemView) {
            super(itemView);
            typeTextView = (TextView) itemView.findViewById(R.id.room_type);
            priceTextView = (TextView) itemView.findViewById(R.id.room_price);
        }
    }
}
