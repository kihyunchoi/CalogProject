package com.example.calog.diet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

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

public class FoodSearchFragment extends Fragment {

    EditText edtSearch;
    ImageView btnSearch;
    DietFoodSearchAdapter adapter;
    RecyclerView dietMenuList;
    List<DietMenuVO> dietMenuArray;
    String keyword;
    BundleAdapter bundleAdapter;
    Retrofit retrofit;
    RemoteService rs;
    List<DietMenuVO> myList;

    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_search, container, false);

        dietMenuList = view.findViewById(R.id.dietMenuList);
        edtSearch = view.findViewById(R.id.edtSearch);
        btnSearch = view.findViewById(R.id.btnSearch);

        Bundle bundle = getArguments();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        dietMenuList.setLayoutManager(manager);

        bundleAdapter = (BundleAdapter) bundle.getSerializable("bundleAdapter");
        myList = bundle.getParcelableArrayList("myList");

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = edtSearch.getText().toString();

                /**
                 * Show diet menu list by given keyword
                 */
                Call<List<DietMenuVO>> call = rs.dietMenuList(keyword);
                call.enqueue(new Callback<List<DietMenuVO>>() {
                    @Override
                    public void onResponse(Call<List<DietMenuVO>> call, Response<List<DietMenuVO>> response) {
                        dietMenuArray = response.body();
                        adapter = new DietFoodSearchAdapter(getContext(), dietMenuArray, myList, bundleAdapter);
                        dietMenuList.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<DietMenuVO>> call, Throwable t) {
                        System.out.println("=====FoodSearchFragment btnSearch onFailure: " + t.toString());
                    }
                });
            }
        });
        return view;
    }
}