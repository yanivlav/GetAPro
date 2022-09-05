package com.example.getapro.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.getapro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignupFragment() {
        // Required empty public constructor
    }

    TextInputLayout fullnameTiEt,emailTiet,passwordTiEt;
    Button completeSignupBtn, backBtn;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
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
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

//        fullnameTiEt = view.findViewById(R.id.signup_fullname);
        emailTiet = view.findViewById(R.id.signup_email);
        passwordTiEt = view.findViewById(R.id.signup_password);




        completeSignupBtn = view.findViewById(R.id.signup_complete_btn);
        completeSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String fullname = fullnameTiEt.getEditText().getText().toString();
                String username = emailTiet.getEditText().getText().toString();
                String password = passwordTiEt.getEditText().getText().toString();


                if (username.length() == 0 || password.length() == 0) {
                    Toast.makeText(getContext(), "null field was detected!", Toast.LENGTH_SHORT).show();
                }
                else{

                    firebaseAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){//                signup to user
//                    Snackbar.make(coordinatorLayout,"Signup success",Snackbar.LENGTH_SHORT);
                                Toast.makeText(getContext(), "Welcome "+username+"!", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_clientDashboard);
                            }//email is already in use
                            else
                                Toast.makeText(getContext(), "Email is already in use.", Toast.LENGTH_SHORT).show();
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