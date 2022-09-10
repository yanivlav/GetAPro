package com.example.getapro.Helpers;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.getapro.MyObjects.Form;
import com.example.getapro.R;

import java.util.ArrayList;
import java.util.List;

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.FormViewHolder> {

    private List<Form> forms;
    private IFormListener listener;

    public interface IFormListener {
        void onInfoClicked(int position, View view);
        void onFormClicked(int position, View view);
        void onFormLongClicked(int position,View view);
    }

    public void setListener(IFormListener listener) {this.listener = listener;}

    public FormAdapter(ArrayList<Form> forms) {this.forms = forms;}


    public class FormViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTv;
        ImageView picIv;
        ImageButton infoIB;

        public FormViewHolder(View itemView) {
            super(itemView);
            descriptionTv = itemView.findViewById(R.id.description);
            picIv = itemView.findViewById(R.id.fix_image);

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
                        listener.onFormClicked(getAdapterPosition(),view);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(listener!=null)
                        listener.onFormLongClicked(getAdapterPosition(),view);
                    return false;
                }
            });
        }
    }

    @Override
    public FormViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.form_cell,parent,false);
        FormViewHolder formViewHolder = new FormViewHolder(view);
        return formViewHolder;
    }

    @Override
    public void onBindViewHolder(FormViewHolder holder, int position) {
        Form form = forms.get(position);
        holder.descriptionTv.setText(form.getDescription());
        if(form.getPhotoPath()!= null)
            holder.picIv.setImageBitmap(BitmapFactory.decodeFile(form.getPhotoPath()));
        else
            holder.picIv.setImageResource(form.getResID());
    }

    @Override
    public int getItemCount() {
        return forms.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}