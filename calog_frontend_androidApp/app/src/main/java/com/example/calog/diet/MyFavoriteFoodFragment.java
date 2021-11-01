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

import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.vo.DietMenuVO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class MyFavoriteFoodFragment extends Fragment {

    DietFoodSearchAdapter adapter;
    RecyclerView dietMenuList;
    List<DietMenuVO> dietMenuArray;
    BundleAdapter bundleAdapter;
    List<DietMenuVO> myList;
    Intent intent;
    String userId;

    Retrofit retrofit;
    RemoteService rs;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_favorite_food, container, false);

        dietMenuList = view.findViewById(R.id.dietMenuList);

        Bundle bundle = getArguments();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        dietMenuList.setLayoutManager(manager);

        bundleAdapter = (BundleAdapter) bundle.getSerializable("bundleAdapter");
        myList = bundle.getParcelableArrayList("myList");

        intent = getActivity().getIntent();

        userId = intent.getStringExtra("user_id");

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);

        /**
         * Show a user registered menu list from database
         */
        Call<List<DietMenuVO>> call = rs.userFavoriteMenuList(userId);
        call.enqueue(new Callback<List<DietMenuVO>>() {
            @Override
            public void onResponse(Call<List<DietMenuVO>> call, Response<List<DietMenuVO>> response) {
                dietMenuArray = response.body();
                adapter = new DietFoodSearchAdapter(getContext(), dietMenuArray, myList, bundleAdapter);
                dietMenuList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<DietMenuVO>> call, Throwable t) {
                System.out.println("MyFavoriteFoodFragment userFavoriteMenuList onFailure" + t.toString());
            }
        });

        return view;
    }
}
