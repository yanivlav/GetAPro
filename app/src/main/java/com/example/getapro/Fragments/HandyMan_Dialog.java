package com.example.getapro.Fragments;
//https://www.youtube.com/watch?v=Yikx61n3oxE

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.getapro.R;

import java.util.ArrayList;

public class HandyMan_Dialog extends DialogFragment {

    private static final String TAG = "DialogFragment";


    ListView lv;
    TextView tv;
    EditText et;
    ArrayAdapter<String> adapter;
//    String [] jobs  = {"Constructor", "Plumber", "Electrician", "Yoga teacher", "Kite instructor", "Skipper","Construction engineer"};
    ArrayList<String> jobs = new ArrayList<>();

    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.handyman_spinner,null);
        Bundle bundle = new Bundle();

        jobs.add(getString(R.string.Constructor));
        jobs.add(getString(R.string.Plumber));
        jobs.add(getString(R.string.Electrician));
        jobs.add(getString(R.string.Yoga_teacher));
        jobs.add(getString(R.string.Kite_instructor));
        jobs.add(getString(R.string.Skipper));
        jobs.add(getString(R.string.Construction_engineer));
        lv = view.findViewById(R.id.list_view);
        et = view.findViewById(R.id.edit_text);
        tv = view.findViewById(R.id.text_view);

        adapter = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, jobs);
        lv.setAdapter(adapter);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String input = adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("bundleKey", input);
                getParentFragmentManager().setFragmentResult("requestKey", bundle);
                getDialog().dismiss();

            }
        });

        return view;
    }
}