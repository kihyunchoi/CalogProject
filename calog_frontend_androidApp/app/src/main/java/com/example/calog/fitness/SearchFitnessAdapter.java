package com.example.calog.fitness;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calog.R;
import com.example.calog.vo.FitnessMenuVO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchFitnessAdapter extends RecyclerView.Adapter<SearchFitnessAdapter.ViewHolder> {
    Context context;
    List<FitnessMenuVO> array;
    String fitnessDate;
    String userId;

    public SearchFitnessAdapter(Context context, List<FitnessMenuVO> array, String date, String userId) {
        this.context = context;
        this.array = array;
        this.fitnessDate = date;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, final int i) {

        Picasso.with(context).load(array.get(i).getFitness_menu_image()).into(viewholder.fitness_menu_image);
        viewholder.fitness_menu_name.setText(array.get(i).getFitness_menu_name());
        viewholder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ExerciseActivity.class);
                intent.putExtra("fitness_menu_id", array.get(i).getFitness_menu_id());
                intent.putExtra("unit_calorie", array.get(i).getUnit_calorie());
                intent.putExtra("select_date", fitnessDate);
                intent.putExtra("user_id", userId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fitness_menu_name;
        ImageView fitness_menu_image;
        RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fitness_menu_name = itemView.findViewById(R.id.FitnessName);
            fitness_menu_image = itemView.findViewById(R.id.image);

            layout = itemView.findViewById(R.id.layout);

        }
    }
}
