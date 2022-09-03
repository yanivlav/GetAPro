package com.example.getapro.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.getapro.Helpers.SpetzAdapter;
import com.example.getapro.MyObjects.Spetz;
import com.example.getapro.R;

import java.util.ArrayList;

public class SpetzList extends Fragment {

    ArrayList<Spetz> spetzs;


    public static SpetzList newInstance(String param1, String param2) {
        SpetzList fragment = new SpetzList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.spetz_list, container, false);

        if (spetzs==null) {
            spetzs = new ArrayList<>();
            spetzs.add(new Spetz("Bar", "North", "B@gmail.com", "123", 022346543, R.drawable.person_icon, "Foundations"));
            spetzs.add(new Spetz("Yaniv", "North", "Y@gmail.com", "123", 022234543, R.drawable.person_icon, "Cleaner"));
            spetzs.add(new Spetz("Eran", "North", "E@gmail.com", "123", 02765543, R.drawable.person_icon, "Shiatsu"));
            spetzs.add(new Spetz("John", "North", "J@gmail.com", "123", 05454567, R.drawable.person_icon, "Surf instructor"));
        }

        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final SpetzAdapter formAdapter = new SpetzAdapter(spetzs);
        formAdapter.setListener(new SpetzAdapter.ISpetzListener() {
            @Override
            public void onInfoClicked(int position, View view) {
                Bundle bundle =  new Bundle();
                bundle.putParcelable("spetzs", spetzs.get(position));//maybe the form class should implement parceble
                Navigation.findNavController(view).navigate(R.id.action_spetzList_to_spetzCard);

            }

            @Override
            public void onSpetzClicked(int position, View view) {
//                Open Chat
            }

            @Override
            public void onSpetzLongClicked(int position, View view) {

            }
        });
            recyclerView.setAdapter(formAdapter);

            return view;
    }
}


