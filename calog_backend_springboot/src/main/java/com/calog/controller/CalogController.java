package com.calog.controller;

import java.sql.Date;
import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.calog.domain.MainHealthVO;
import com.calog.domain.SleepingVO;
import com.calog.domain.UserDietTotalCaloriesViewVO;
import com.calog.domain.UserFitnessTotalCaloriesViewVO;
import com.calog.persistence.DietDAO;
import com.calog.persistence.FitnessDAO;
import com.calog.persistence.SleepingDAO;

@RestController
@RequestMapping("/calog")
public class CalogController {
	@Inject
	FitnessDAO fitnessDAO;

	@Inject
	DietDAO dietDAO;

	@Inject
	SleepingDAO sleepingDAO;

	/* ==================== GET Method ==================== */
	@RequestMapping(value = "/userMainHealthData", method = RequestMethod.GET)
	public MainHealthVO userMainHealthData(String user_id, String select_date) throws Exception {
		double sum_diet_calorie = 0.0;
		double sum_fitness_calorie = 0.0;
		int sleeping_seconds = 0;
		System.out.println("sqldate==================================" + select_date);

		//User total diet calorie intake for a selected date
		UserDietTotalCaloriesViewVO dietVO = new UserDietTotalCaloriesViewVO();
		dietVO.setUser_id(user_id);
		dietVO.setDiet_date(Date.valueOf(select_date));
		dietVO = dietDAO.userDietDailyTotalCalorie(dietVO);

		if (dietVO != null) {
			sum_diet_calorie = dietVO.getSum_calorie();
		}

		//User total fitness calorie consumption for a selected date
		UserFitnessTotalCaloriesViewVO fitnessVO = new UserFitnessTotalCaloriesViewVO();
		fitnessVO.setUser_id(user_id);
		fitnessVO.setFitness_date(Date.valueOf(select_date));

		fitnessVO = fitnessDAO.userFitnessDailyTotalCalorie(fitnessVO);

		if (fitnessVO != null) {
			sum_fitness_calorie = fitnessVO.getFitness_sum_calorie_consumption();
		}

		//User total sleeping hours for a selected date
		SleepingVO sleepingVO = new SleepingVO();
		sleepingVO.setUser_id(user_id);
		sleepingVO.setSleeping_date(Date.valueOf(select_date));

		sleepingVO = sleepingDAO.userSleepingDailyInfo(sleepingVO);
		if (sleepingVO != null) {
			sleeping_seconds = sleepingVO.getSleeping_seconds();
		}

		MainHealthVO main = new MainHealthVO(user_id, Date.valueOf(select_date), sum_diet_calorie, sum_fitness_calorie,
				sleeping_seconds);

		return main;
	}
}