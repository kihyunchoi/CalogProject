package com.example.calog.diet;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.vo.DietMenuVO;
import com.example.calog.vo.DietVO;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class BundleAdapter extends RecyclerView.Adapter<BundleAdapter.ViewHolder> implements Serializable, Parcelable {

    Context context;
    List<DietMenuVO> dietMenus;

    Retrofit retrofit;
    RemoteService rs;

    public BundleAdapter(Context context, List<DietMenuVO> dietMenus) {
        this.context = context;
        this.dietMenus = dietMenus;
    }


    protected BundleAdapter(Parcel in) {
        dietMenus = in.createTypedArrayList(DietMenuVO.CREATOR);
    }

    public static final Creator<BundleAdapter> CREATOR = new Creator<BundleAdapter>() {
        @Override
        public BundleAdapter createFromParcel(Parcel in) {
            return new BundleAdapter(in);
        }

        @Override
        public BundleAdapter[] newArray(int size) {
            return new BundleAdapter[size];
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diet_menu, parent, false);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rs = retrofit.create(RemoteService.class);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.txtMenuName.setText(dietMenus.get(position).getDiet_menu_name());
        holder.txtCalorie.setText(String.valueOf(dietMenus.get(position).getCalorie()) + " kal");
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()) {
                    dietMenus.get(position).setChecked(true);
                } else {
                    dietMenus.get(position).setChecked(false);
                }
            }
        });
    }

    /**
     * Insert user selected menus to the database
     *
     * @param user_id
     * @param diet_type_id
     * @param select_date
     */
    public void insertFood(String user_id, int diet_type_id, String select_date) {

        for (int i = 0; i < dietMenus.size(); i++) {

            if (dietMenus.get(i).isChecked()) {
                DietMenuVO dietMenuVO = dietMenus.get(i);
                dietMenuVO.setSelect_date(select_date);

                DietVO dietVO = new DietVO();
                dietVO.setUser_id(user_id);
                dietVO.setDiet_type_id(diet_type_id);
                dietVO.setDiet_menu_id(dietMenuVO.getDiet_menu_id());
                dietVO.setDiet_amount(1);
                dietVO.setDiet_date(dietMenuVO.getSelect_date());
                dietVO.setSum_calorie(dietMenuVO.getCalorie());

                Call<Void> call = rs.insertMenu(dietVO);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(context, "Saved.",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        System.out.println("=====BundleAdapter insertFood Error : " + t.toString());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return dietMenus.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(dietMenus);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtMenuName, txtCalorie;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMenuName = itemView.findViewById(R.id.txtMenuName);
            txtCalorie = itemView.findViewById(R.id.txtCalorie);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
