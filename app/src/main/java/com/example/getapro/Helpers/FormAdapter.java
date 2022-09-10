package com.example.getapro.Helpers;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getapro.MyObjects.Form;
import com.example.getapro.MyObjects.Spetz;
import com.example.getapro.R;

import java.util.ArrayList;
import java.util.List;



public class FormAdapter extends RecyclerView.Adapter<FormAdapter.FormViewHolder> {

    private List<Form> forms;

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

        if(form.getPhotoPath()!= null)
            holder.picIv.setImageBitmap(BitmapFactory.decodeFile(form.getPhotoPath()));
        else
            holder.picIv.setImageResource(form.getResID());
    }

    @Override
    public int getItemCount() {
        return forms.size();
    }
}


