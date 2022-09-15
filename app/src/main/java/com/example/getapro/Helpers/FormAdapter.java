package com.example.getapro.Helpers;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.getapro.MyObjects.Form;
import com.example.getapro.MyObjects.Spetz;
import com.example.getapro.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;



public class FormAdapter extends RecyclerView.Adapter<FormAdapter.FormViewHolder> {

    private List<Form> forms;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//    StorageReference gsReference;

    public FormAdapter(List<Form> forms) {
        this.forms = forms;
    }

    public interface FormListener {
        void onFormClicked(int position, View view);
        void onFormLongClicked(int position, View view);
    }

    private FormListener listener;

    public void setListener(FormListener listener) {
        this.listener = listener;
    }

    public class FormViewHolder extends RecyclerView.ViewHolder {

        TextView descTV;
        ImageView picIv;

        public FormViewHolder(View itemView) {
            super(itemView);
            descTV = itemView.findViewById(R.id.problem_description);
            picIv = itemView.findViewById(R.id.problem_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onFormClicked(getAdapterPosition(),view);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onFormLongClicked(getAdapterPosition(),view);
                    return true;
                }
            });
        }
    }

    @NonNull
    @Override
    public FormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spetz_cell,parent,false);
        FormViewHolder formViewHolder = new FormViewHolder(view);
        return formViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FormViewHolder holder, int position) {

        Form form = forms.get(position);
        holder.descTV.setText(form.getDescription());

        if(form.getIssueImage()!= null) {
            String s = form.getIssueImage();
                storageReference.child(form.getIssueImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(holder.picIv.getContext()).load(uri).into(holder.picIv);
                }
            });
        }else
            holder.picIv.setImageResource(form.getIssueImageResID());
    }

    @Override
    public int getItemCount() {
        return forms.size();
    }
}


