package com.example.getapro.Helpers;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.getapro.MyObjects.Spetz;
import com.example.getapro.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class SpetzAdapter extends RecyclerView.Adapter<SpetzAdapter.SpetzViewHolder> {

    private List<Spetz> spetzs;
    private ISpetzListener listener;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

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
            nameTv = itemView.findViewById(R.id.problem_description);
            picIv = itemView.findViewById(R.id.problem_image);
            infoIB = itemView.findViewById(R.id.formInfo);
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
        StorageReference pathReference;
        Spetz spetz = spetzs.get(position);
        String path = "UsersProfilePhotos/" + spetz.getEmail() + ".jpg";
        pathReference = storageReference.child(path);
        pathReference.getDownloadUrl();

        holder.nameTv.setText(spetz.getUserName());
        if (spetz.getEmail() != null) {

            storageReference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with((holder.picIv.getContext())).load(uri).into(holder.picIv);
                }
            });
        }
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




