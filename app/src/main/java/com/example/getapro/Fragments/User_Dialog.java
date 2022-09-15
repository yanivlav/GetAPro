package com.example.getapro.Fragments;
//https://www.youtube.com/watch?v=Yikx61n3oxE

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.getapro.Helpers.SpetzAdapter;
import com.example.getapro.MyObjects.Form;
import com.example.getapro.MyObjects.Spetz;
import com.example.getapro.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class User_Dialog extends DialogFragment {

//    private static final String TAG = "DialogFragment";
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    ImageView image;
    TextView name, district, occupation, number;
    Spetz spetz;
    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("username");
            spetz  = getArguments().getParcelable("spetzs");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.user_dialog,null);
        name = view.findViewById(R.id.nameTv);
        name.setText(spetz.getUserName());

        district = view.findViewById(R.id.districtTv);
        district.setText(spetz.getDistrict());

        occupation = view.findViewById(R.id.occupationTv);
        occupation.setText(spetz.getOccupation());

        number = view.findViewById(R.id.numberTv);
        number.setText(spetz.getNumber());

        image = view.findViewById(R.id.imageIv);

        StorageReference pathReference;
        String path = "UsersProfilePhotos/"+spetz.getEmail()+".jpg";
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