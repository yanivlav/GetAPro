package com.example.getapro.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.getapro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    TextInputLayout usernameEt,passwordEt;
    Button signupBtn, skipBtn, loginBtn;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
////                View headerView = navigationView.getHeaderView(0);
////                TextView userTv = headerView.findViewById(R.id.navigation_header_text_view);
//
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//
////                if (user != null){//logon
////                    if(fullname!= null) {//signin = update profile with full name
////
////                        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(fullname).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
////                            @Override
////                            public void onComplete(@NonNull Task<Void> task) {
////                                fullname = null;
////                                if (task.isSuccessful())
////                                    Snackbar.make(coordinatorLayout,user.getDisplayName() + "welcome", Snackbar.LENGTH_SHORT).show();
////                            }
////                        });
////                    }
//
//            }
//        };


        usernameEt = view.findViewById(R.id.email_et);
        passwordEt = view.findViewById(R.id.pass_et);

//        String username = usernameEt.getText().toString();
//        String password = passwordEt.getText().toString();
//
//        if (username.length() == 0 || password.length() == 0) {
//            Toast.makeText(getContext(), "null field was detected!", Toast.LENGTH_SHORT).show();
//        }

        loginBtn = view.findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = usernameEt.getEditText().getText().toString();
                String password = passwordEt.getEditText().getText().toString();

                if (username.length() == 0 || password.length() == 0) {
                    Toast.makeText(getContext(), "null field was detected!", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //move data of user using bundle
                                Toast.makeText(getContext(), "Welcome "+username+"!", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_clientDashboard);
                            }


                            else
                                Toast.makeText(getContext(), "Login failed, wrong email/password", Toast.LENGTH_SHORT).show();

                        }
                    });
                }//else
            }
        });//loginBtn

        signupBtn = view.findViewById(R.id.signup_btn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signupFragment);
            }
        });

        skipBtn = view.findViewById(R.id.login_skip_btn);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_clientDashboard);
            }
        });


        return view;
    }
}