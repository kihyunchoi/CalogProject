package com.example.calog.fitness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calog.R;
import com.example.calog.vo.UserFitnessViewVO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyFitnessListAdapter extends RecyclerView.Adapter<MyFitnessListAdapter.ViewHolder> {
    Context context;
    List<UserFitnessViewVO> array;

    public MyFitnessListAdapter(Context context, List<UserFitnessViewVO> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ondayexercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, final int i) {
        Picasso.with(context).load(array.get(i).getFitness_menu_image()).into(viewholder.fitness_menu_image);
        viewholder.fitness_menu_name.setText(array.get(i).getFitness_menu_name());
        double unitCal = array.get(i).getUnit_calorie();
        int seconds = array.get(i).getFitness_seconds();
        String eachConsumedCalrorie = String.format("%.1f", unitCal * seconds);
        viewholder.eachCalorie.setText(eachConsumedCalrorie + "kcal");

        long fitnessTime = array.get(i).getFitness_seconds();

        int fth = (int) (fitnessTime / 3600);
        int ftm = (int) (fitnessTime - fth * 3600) / 60;
        int fts = (int) (fitnessTime - fth * 3600 - ftm * 60);
        String strH = fth < 10 ? "0" + fth : fth + "";
        String strM = ftm < 10 ? "0" + ftm : ftm + "";
        String strS = fts < 10 ? "0" + fts : fts + "";
        viewholder.eachTime.setText(strH + "h " + strM + "m " + strS + "s");
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fitness_menu_name, eachTime, eachCalorie;
        ImageView fitness_menu_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fitness_menu_name = itemView.findViewById(R.id.FitnessName);
            fitness_menu_image = itemView.findViewById(R.id.image);
            eachTime = itemView.findViewById(R.id.eachTime);
            eachCalorie = itemView.findViewById(R.id.eachCalorie);
        }
    }
}
