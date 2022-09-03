package com.example.getapro.Helpers;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.getapro.MyObjects.Spetz;
import com.example.getapro.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erankatsav on 19/03/2018.
 */

public class SpetzAdapter extends RecyclerView.Adapter<SpetzAdapter.SpetzViewHolder> {

    private List<Spetz> spetzs;
    private ISpetzListener listener;

    public interface ISpetzListener {
        void onInfoClicked(int position, View view);
        void onSpetzClicked(int position, View view);
        void onSpetzLongClicked(int position,View view);
    }

    public void setListener(ISpetzListener listener) {
        this.listener = listener;
    }

    public SpetzAdapter(ArrayList<Spetz> spetzs) {
        this.spetzs = spetzs;
    }


    public class SpetzViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        ImageView picIv;
        ImageButton infoIB;

        public SpetzViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.spetz_name);
            picIv = itemView.findViewById(R.id.album_image);
            infoIB = itemView.findViewById(R.id.spetzInfo);
            infoIB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                        listener.onInfoClicked(getAdapterPosition(),view);
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(listener!=null)
                        listener.onSpetzClicked(getAdapterPosition(),view);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(listener!=null)
                        listener.onSpetzLongClicked(getAdapterPosition(),view);
                    return false;
                }
            });
        }
    }

    @Override
    public SpetzViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spetz_cell,parent,false);
        SpetzViewHolder spetzViewHolder = new SpetzViewHolder(view);
        return spetzViewHolder;
    }

    @Override
    public void onBindViewHolder(SpetzViewHolder holder, int position) {
        Spetz Spetz = spetzs.get(position);
        holder.nameTv.setText(Spetz.getUserName());
        if(Spetz.getPhotoPath()!= null)
            holder.picIv.setImageBitmap(BitmapFactory.decodeFile(Spetz.getPhotoPath()));
        else
            holder.picIv.setImageResource(Spetz.getResID());
    }

    @Override
    public int getItemCount() {
        return spetzs.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}




