package com.example.calog.diet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calog.R;
import com.example.calog.vo.UserDietViewVO;

import java.util.List;

public class DietDailyMenuAdapter extends RecyclerView.Adapter<DietDailyMenuAdapter.ViewHolder> {

    List<UserDietViewVO> userDietdailyMenu;
    Context context;

    public DietDailyMenuAdapter(Context context, List<UserDietViewVO> userDietdailyMenu) {
        this.userDietdailyMenu = userDietdailyMenu;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_diet_menu_daily, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtMenuName.setText(userDietdailyMenu.get(position).getDiet_menu_name());
        holder.txtCalorie.setText(userDietdailyMenu.get(position).getSum_calorie() + "kcal");

        if (userDietdailyMenu.get(position).getDiet_type_id() == 1) {
            holder.itemMenu.setBackgroundColor(Color.rgb(255, 255, 255));
            holder.txtMenuName.setTextColor(Color.rgb(000, 000, 000));
            holder.txtCalorie.setTextColor(Color.rgb(000, 000, 000));
        } else if (userDietdailyMenu.get(position).getDiet_type_id() == 2) {
            holder.txtMenuName.setTextColor(Color.rgb(255, 255, 255));
            holder.txtCalorie.setTextColor(Color.rgb(255, 255, 255));
        } else if (userDietdailyMenu.get(position).getDiet_type_id() == 3) {
            holder.itemMenu.setBackgroundColor(Color.rgb(255, 255, 255));
            holder.txtMenuName.setTextColor(Color.rgb(000, 000, 000));
            holder.txtCalorie.setTextColor(Color.rgb(000, 000, 000));
        } else if (userDietdailyMenu.get(position).getDiet_type_id() == 4) {
            holder.txtMenuName.setTextColor(Color.rgb(255, 255, 255));
            holder.txtCalorie.setTextColor(Color.rgb(255, 255, 255));
        }
    }

    @Override
    public int getItemCount() {
        return userDietdailyMenu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtMenuName, txtCalorie;
        RelativeLayout itemMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMenuName = itemView.findViewById(R.id.txtMenuName);
            txtCalorie = itemView.findViewById(R.id.txtCalorie);
            itemMenu = itemView.findViewById(R.id.itemMenu);
        }
    }
}
