package com.example.calog.diet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MyFoodFragment extends Fragment {
    RecyclerView myMenuList;
    BundleAdapter adapter;
    FloatingActionButton btnInsert;

    Intent intent;
    String userId;
    int dietTypeId;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_food, container, false);

        myMenuList = view.findViewById(R.id.myMenuList);
        btnInsert = view.findViewById(R.id.btnInsert);

        Bundle bundle = getArguments();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        myMenuList.setLayoutManager(manager);

        adapter = (BundleAdapter) bundle.getSerializable("bundleAdapter");
        myMenuList.setAdapter(adapter);

        intent = getActivity().getIntent();

        userId = intent.getStringExtra("user_id");
        dietTypeId = intent.getIntExtra("diet_type_id", 0);

        /**
         * Insert menu button to user selected menu list into the database
         */
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectDate = intent.getStringExtra("select_date");
                adapter.insertFood(userId, dietTypeId, selectDate);

                intent = new Intent(getContext(), DietActivity.class);
                intent.putExtra("select_date", selectDate);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

}