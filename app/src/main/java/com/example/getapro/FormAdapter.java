package com.example.getapro;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.getapro.Form;
import com.example.getapro.R;

import java.util.List;

/**
 * Created by erankatsav on 19/03/2018.
 */

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.FormViewHolder> {

    private List<Form> forms;
    private IFormListener listener;

    interface IFormListener {
        void onInfoClicked(int position, View view);
        void onFormClicked(int position, View view);
        void onFormLongClicked(int position,View view);
    }

    public void setListener(IFormListener listener) {
        this.listener = listener;
    }

    public FormAdapter(List<Form> forms) {
        this.forms = forms;
    }


    public class FormViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        TextView linkTv;
        ImageView picIv;
        ImageButton infoIB;

        public FormViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.form_name);
            picIv = itemView.findViewById(R.id.album_image);

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
        Form Form = forms.get(position);
        holder.nameTv.setText(Form.getClientName());

        if(Form.getPhotoPath()!= null)
            holder.picIv.setImageBitmap(BitmapFactory.decodeFile(Form.getPhotoPath()));
        else
            holder.picIv.setImageResource(Form.getPhotoPathInt());
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




