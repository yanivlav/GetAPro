package com.example.getapro.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.getapro.MyObjects.Form;
import com.example.getapro.MyObjects.Spetz;
import com.example.getapro.MyObjects.User;
import com.example.getapro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link SignupFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class SignupFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String username = "username";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String username;

    ArrayList<Spetz> spetzs_local = new ArrayList<>();
    ArrayList<User> users_local = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //name of the instance of the Forms table
    DatabaseReference users_fire = database.getReference("Users");

    public SignupFragment() {
        // Required empty public constructor
    }

    TextInputLayout emailTiet,passwordTiEt,fullnameTiEt,phoneTiEt,districtTiEt;
    CheckBox spetzCheckBox;
    EditText occupationEt;
    Button completeSignupBtn, backBtn;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment SignupFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static SignupFragment newInstance(String param1, String param2) {
//        SignupFragment fragment = new SignupFragment();
//        Bundle args = new Bundle();
//        args.putString(username, "username");
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(username);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

//        fullnameTiEt = view.findViewById(R.id.signup_fullname);
        emailTiet = view.findViewById(R.id.signup_email);
        passwordTiEt = view.findViewById(R.id.signup_password);
        fullnameTiEt = view.findViewById(R.id.signup_fullname);
        phoneTiEt = view.findViewById(R.id.signup_phone);
        districtTiEt = view.findViewById(R.id.signup_district);
        spetzCheckBox = view.findViewById(R.id.spetz_CheckBox);
        occupationEt = view.findViewById(R.id.signup_spetz_occupation);

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
                                    Spetz spetz = new Spetz(fullname,district,username,phone,R.drawable.field_username_icon,occupation);
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
                                    User user1 = new User(fullname,district,username,phone,1);
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
}