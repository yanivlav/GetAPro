package com.example.getapro.Fragments;

//https://www.youtube.com/watch?v=Yikx61n3oxE

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.getapro.MyObjects.Form;
import com.example.getapro.MyObjects.Spetz;
import com.example.getapro.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Form_Dialog extends DialogFragment {

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    ImageView image;
    TextView desc,address,spetzName, spetzPhone, userName, userPhone;
    Form form;
    int pos;
    private String username;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseUser user = firebaseAuth.getCurrentUser();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("username");
            pos = getArguments().getInt("position");
            form  = getArguments().getParcelable("form");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.form_dialog,null);
        desc = view.findViewById(R.id.problom_desc);
        if (desc != null){
            desc.setText(form.getDescription());
        }
        desc.setText("no desc");

        address = view.findViewById(R.id.problom_address);
        address.setText(form.getAddress());

        spetzName = view.findViewById(R.id.problem_spetzName);
        spetzName.setText(form.getSpetzName());

        spetzPhone = view.findViewById(R.id.problem_spetzPhone);
        spetzPhone.setText(form.getSpetzPhone());

        userName = view.findViewById(R.id.problem_userName);
        userName.setText(form.getUsersName());

        userPhone = view.findViewById(R.id.problem_userPhone);
        userPhone.setText(form.getUsersPhone());

        image = view.findViewById(R.id.imageIv);

        StorageReference pathReference;
        String path = "Problems/"+user.getUid()+"_Form_Number_"+pos+".jpg";
        pathReference = storageReference.child(path);
        pathReference.getDownloadUrl();


        storageReference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).into(image);
            }
        });

        return view;
    }
}