package com.example.calog.diet;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calog.R;
import com.example.calog.vo.DietMenuVO;

import java.util.List;


public class DietFragment extends Fragment {
    List<DietMenuVO> dietMenuArray;
    DietMenuAdapter menuAdapter;

    boolean isSearchView;

    public DietFragment(List dietMenuArray, boolean isSearchView) {
        this.dietMenuArray = dietMenuArray;
        this.isSearchView = isSearchView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.dietList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        menuAdapter = new DietMenuAdapter(getContext(), dietMenuArray);
        recyclerView.setAdapter(menuAdapter);

        if (isSearchView) {
            EditText search = view.findViewById(R.id.searchEdit);
            search.setVisibility(View.VISIBLE);

            search.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == event.KEYCODE_ENTER) {
                        return true;
                    }
                    return false;
                }
            });
        }
        return view;
    }
}
