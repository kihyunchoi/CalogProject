package com.example.calog.common;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calog.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class GraphFragment extends Fragment {
    String[] labels;

    public GraphFragment() {
    }

    public static ArrayList<GraphVO> sumCalorieListWeek =
            new ArrayList<GraphVO>(Arrays.asList(new GraphVO(0, "Today")));
    public static ArrayList<GraphVO> sumCalorieListMonth =
            new ArrayList<GraphVO>(Arrays.asList(new GraphVO(0, "Today")));
    public static ArrayList<GraphVO> sumCalorieListYear =
            new ArrayList<GraphVO>(Arrays.asList(new GraphVO(0, "Today")));

    String unitDate;

    /**
     * GraphFragment constructor
     *
     * @param unitDate
     */
    public GraphFragment(String unitDate) {
        this.unitDate = unitDate;

        if (unitDate.equals("week")) {
            this.labels = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        } else if (unitDate.equals("month")) {
            this.labels =
                    new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                            "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
                            "24", "25", "26", "27", "28", "29", "30", "31"};
        } else if (unitDate.equals("year")) {
            this.labels = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        BarChart barChart = view.findViewById(R.id.barChart);

        barChart.invalidate();

        ArrayList<BarEntry> entries = new ArrayList<>();

        if (unitDate.equals("week")) {
            GraphVO vo;

            for (int i = 0; i < labels.length; i++) {
                entries.add(new BarEntry(i, 0));
            }

            for (int i = 0; i < labels.length; i++) {
                if (i < sumCalorieListWeek.size()) {
                    vo = sumCalorieListWeek.get(i);

                    String week = "";

                    try {
                        week = getDateName(vo.getDataDate());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for (int j = 0; j < labels.length; j++) {
                        if (week.equals(labels[j])) {
                            entries.set(j, new BarEntry(j, vo.getDataFloat()));
                        }
                    }
                }
            }
        } else if (unitDate.equals("month")) {
            GraphVO vo;

            for (int i = 0; i < labels.length; i++) {
                entries.add(new BarEntry(i, 0));
            }

            for (int i = 0; i < labels.length; i++) {

                if (i < sumCalorieListMonth.size()) {
                    vo = sumCalorieListMonth.get(i);

                    int indexday = Integer.parseInt(vo.getDataDate().substring(8)) - 1;
                    entries.set(indexday, new BarEntry(indexday, vo.getDataFloat()));
                }
            }
        } else if (unitDate.equals("year")) {
            GraphVO vo;

            for (int i = 0; i < labels.length; i++) {
                entries.add(new BarEntry(i, 0));
            }

            for (int i = 0; i < labels.length; i++) {
                if (i < sumCalorieListYear.size()) {
                    vo = sumCalorieListYear.get(i);

                    int indexMonth = Integer.parseInt(vo.getDataDate()) - 1;
                    entries.set(indexMonth, new BarEntry(indexMonth, vo.getDataFloat()));
                }
            }
        }

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);

        BarDataSet bardataset = new BarDataSet(entries, "");

        BarData data = new BarData(bardataset);

        barChart.setData(data);

        barChart.getDescription().setEnabled(false);

        barChart.getLegend().setEnabled(false);

        barChart.animateXY(2000, 2000);

        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        return view;
    }

    /**
     * Get Date of Name
     *
     * @param date
     * @return
     * @throws Exception
     */
    private String getDateName(String date) throws Exception {


        String day = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date nDate = dateFormat.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                day = "Sun";
                break;
            case 2:
                day = "Mon";
                break;
            case 3:
                day = "Tue";
                break;
            case 4:
                day = "Wed";
                break;
            case 5:
                day = "Thu";
                break;
            case 6:
                day = "Fri";
                break;
            case 7:
                day = "Sat";
                break;
        }
        return day;
    }

}