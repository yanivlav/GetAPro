package com.example.getapro.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.getapro.MyObjects.Spetz;
import com.example.getapro.MyObjects.User;
import com.example.getapro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SignupFragment extends Fragment {

    final int CAMERA_REQUEST = 1;
    final int WRITE_PERMISSION_REQUEST = 1;

    private String username;

    ArrayList<Spetz> spetzs_local = new ArrayList<>();
    ArrayList<User> users_local = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference users_fire = database.getReference("Users");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    TextInputLayout emailTiet,passwordTiEt,fullnameTiEt,phoneTiEt,districtTiEt;
    CheckBox spetzCheckBox;
    MaterialTextView occupationEt;
    Button completeSignupBtn, backBtn, imageBtn;
    String result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                result = bundle.getString("bundleKey");
                // Do something with the result
                occupationEt.setText(result);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        emailTiet = view.findViewById(R.id.signup_email);
        passwordTiEt = view.findViewById(R.id.signup_password);
        fullnameTiEt = view.findViewById(R.id.signup_fullname);
        phoneTiEt = view.findViewById(R.id.signup_phone);
        districtTiEt = view.findViewById(R.id.signup_district);
        spetzCheckBox = view.findViewById(R.id.spetz_CheckBox);
        occupationEt = view.findViewById(R.id.signup_spetz_occupation);
        imageBtn = view.findViewById(R.id.userImage);


        occupationEt.setEnabled(false);

        spetzCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(spetzCheckBox.isChecked())
                    occupationEt.setEnabled(true);
            }
        }
        );
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailTiet.getEditText().getText().toString().equals(""))
                    Toast.makeText(getContext(), "Fill in your Email first!", Toast.LENGTH_SHORT).show();
                else {
                    imageBtn.setEnabled(true);
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera_intent, CAMERA_REQUEST);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {
            int hasWritePermission = getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                getActivity().requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST);
            } else imageBtn.setVisibility(View.VISIBLE);
        } else imageBtn.setVisibility(View.VISIBLE);


        occupationEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.handyMan_Dialog);
            }
        });
        completeSignupBtn = view.findViewById(R.id.signup_complete_btn);
        completeSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = emailTiet.getEditText().getText().toString();
                String password = passwordTiEt.getEditText().getText().toString();
                String fullname = fullnameTiEt.getEditText().getText().toString();
                String phone = phoneTiEt.getEditText().getText().toString();
                String district = districtTiEt.getEditText().getText().toString();
                Boolean isSpetz = spetzCheckBox.isChecked();
                String occupation = occupationEt.getText().toString();


                if (username.length() == 0 || password.length() == 0 ) {
                    Toast.makeText(getContext(), "null field was detected!", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), "Welcome "+username+"!", Toast.LENGTH_SHORT).show();
                                final FirebaseUser user = firebaseAuth.getCurrentUser();
                                if(isSpetz){
                                    Spetz spetz = new Spetz(fullname,district,username,phone,R.drawable.field_username_icon, user.getUid(), occupation);
                                    users_fire.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            spetzs_local.clear();

                                            if(dataSnapshot.exists()) {
                                                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    Spetz spetz1 = snapshot.getValue(Spetz.class);
                                                    spetzs_local.add(spetz1);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });

                                    spetzs_local.add(spetz);
                                    users_fire.child(firebaseAuth.getCurrentUser().getUid()).setValue(spetzs_local);
                                } else {
                                    User user1 = new User(fullname,district,username,phone,R.drawable.field_username_icon, user.getUid());
                                    users_fire.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            users_local.clear();

                                            if(dataSnapshot.exists()) {
                                                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    User user2 = snapshot.getValue(User.class);
                                                    users_local.add(user2);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });

                                    users_local.add(user1);
                                    users_fire.child(firebaseAuth.getCurrentUser().getUid()).setValue(users_local);
                                }

                                Bundle bundle = new Bundle();
                                bundle.putString("username",username);
                                Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_clientDashboard,bundle);

                            }//email is already in use
                            else{
                                Toast.makeText(getContext(), "Email is already in use.", Toast.LENGTH_SHORT).show();
                            }

                            //                    Snackbar.make(coordinatorLayout,"Signup failed",Snackbar.LENGTH_SHORT);
                        }
                    });
                }

            }
        });

        backBtn = view.findViewById(R.id.signup_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_loginFragment);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       String  path = emailTiet.getEditText().getText().toString()+".jpg";
        StorageReference problemImagesRef = storageReference.child("UsersProfilePhotos/"+path);

        if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            imageBtn.setBackgroundResource(R.drawable.check_icon);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            problemImagesRef.putBytes(byteArray);
        }

    }
}