package com.calog.controller;

import java.sql.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.calog.domain.FitnessMenuVO;
import com.calog.domain.FitnessVO;
import com.calog.domain.UserFitnessTotalCaloriesViewVO;
import com.calog.domain.UserFitnessViewVO;
import com.calog.persistence.FitnessDAO;

@RestController
@RequestMapping("/calog")
public class FitnessController {

	@Inject
	FitnessDAO fitnessDAO;

	/* ==================== GET Method ==================== */
	//User fitness menu list
	@RequestMapping(value = "/fitness/fitnessMenuList", method = RequestMethod.GET)
	public List<FitnessMenuVO> fitnessMenuList() throws Exception {
		System.out.println("sddssdsddsds");
		return fitnessDAO.fitnessMenuList();
	}

	//User fitness graph data
	@RequestMapping(value = "/fitness/graphFitnessData", method = RequestMethod.GET)
	public List<UserFitnessTotalCaloriesViewVO> graphFitnessData(String user_id, String start_date, String unit_date)
			throws Exception {
		UserFitnessTotalCaloriesViewVO vo = new UserFitnessTotalCaloriesViewVO();
		vo.setUser_id(user_id);
		vo.setFitness_date(Date.valueOf(start_date));

		return fitnessDAO.graphFitnessData(vo);
	}

	//User fitness daily total calorie consumption given selected date
	@RequestMapping(value = "/fitness/userFitnessDailyTotalCalorie", method = RequestMethod.GET)
	public UserFitnessTotalCaloriesViewVO userFitnessDailyTotalCalorie(String user_id, String fitness_date)
			throws Exception {
		System.out.println("wewewewe");
		UserFitnessTotalCaloriesViewVO vo = new UserFitnessTotalCaloriesViewVO();
		vo.setUser_id(user_id);
		vo.setFitness_date(Date.valueOf(fitness_date));

		return fitnessDAO.userFitnessDailyTotalCalorie(vo);
	}

	//User fitness daily menu list given selected date
	@RequestMapping(value = "/fitness/userFitnessDailyMenuList", method = RequestMethod.GET)
	public List<UserFitnessViewVO> userFitnessDailyMenuList(String user_id, String fitness_date) throws Exception {
		UserFitnessViewVO vo = new UserFitnessViewVO();
		vo.setUser_id(user_id);
		vo.setFitness_date(Date.valueOf(fitness_date));

		return fitnessDAO.userFitnessDailyMenuList(vo);
	}

	/* ==================== POST Method ==================== */
	//User fitness menu insert into database
	@RequestMapping(value = "/fitness/userFitnessInsert", method = RequestMethod.POST)
	public void userFitnessInsert(@RequestBody FitnessVO vo) throws Exception {
		fitnessDAO.userFitnessInsert(vo);
	}

}
