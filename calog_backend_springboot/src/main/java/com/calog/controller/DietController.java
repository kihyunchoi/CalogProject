package com.calog.controller;

import java.sql.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.calog.domain.DietTypeDailyCalorieSumVO;
import com.calog.domain.DietVO;
import com.calog.domain.DietMenuVO;
import com.calog.domain.UserDietViewVO;
import com.calog.domain.UserDietTotalCaloriesViewVO;
import com.calog.persistence.DietDAO;

@RestController
@RequestMapping("/calog")
public class DietController {

	@Inject
	DietDAO dietDAO;

	/* ==================== GET Method ==================== */
	//User graph diet data
	@RequestMapping(value = "/diet/graphDietData", method = RequestMethod.GET)
	public List<UserDietTotalCaloriesViewVO> graphDietData(String user_id, String start_date, String unit)
			throws Exception {
		UserDietTotalCaloriesViewVO vo = new UserDietTotalCaloriesViewVO();
		vo.setUser_id(user_id);
		vo.setDiet_date(Date.valueOf(start_date));

		return dietDAO.graphDietData(vo, unit);
	}

	//User diet daily calorie sum for each meal given selected date
	@RequestMapping(value = "/diet/userDietTypeDailyCalorieSum", method = RequestMethod.GET)
	public List<DietTypeDailyCalorieSumVO> userDietTypeDailyCalorieSum(String user_id, String diet_date)
			throws Exception {
		DietTypeDailyCalorieSumVO vo = new DietTypeDailyCalorieSumVO();
		vo.setUser_id(user_id);
		vo.setDiet_date(Date.valueOf(diet_date));

		return dietDAO.userDietTypeDailyCalorieSum(vo);
	}

	//User daily diet menu list for each meal given selected date
	@RequestMapping(value = "/diet/userDietDailyMenuList", method = RequestMethod.GET)
	public List<UserDietViewVO> UserDietDailyMenuList(String user_id, String diet_date) throws Exception {
		UserDietViewVO vo = new UserDietViewVO();
		vo.setUser_id(user_id);
		vo.setDiet_date(Date.valueOf(diet_date));

		return dietDAO.userDietDailyMenuList(vo);
	}

	//User diet menu list which is already exist in the database
	@RequestMapping(value = "/diet/userFavoriteMenuList", method = RequestMethod.GET)
	public List<DietMenuVO> UserFavoriteMenuList(String user_id) throws Exception {
		return dietDAO.userFavoriteMenuList(user_id);
	}

	//User diet menu list by given keyword
	@RequestMapping(value = "/diet/dietMenuList", method = RequestMethod.GET)
	public List<DietMenuVO> DietMenuList(String keyword) throws Exception {
		return dietDAO.dietMenuList(keyword);
	}

	//User diet daily calorie sum 
	@RequestMapping(value = "/diet/userDietDailyTotalCalorie", method = RequestMethod.GET)
	public UserDietTotalCaloriesViewVO userDietDailyTotalCalorie(String user_id, String diet_date) throws Exception {
		UserDietTotalCaloriesViewVO vo = new UserDietTotalCaloriesViewVO();
		vo.setUser_id(user_id);
		vo.setDiet_date(Date.valueOf(diet_date));

		return dietDAO.userDietDailyTotalCalorie(vo);
	}

	//User diet daily calorie sum list
	@RequestMapping(value = "/diet/userDietDailyTotalCalorieList", method = RequestMethod.GET)
	public List<UserDietTotalCaloriesViewVO> userDietDailyTotalCalorie(String user_id) throws Exception {
		UserDietTotalCaloriesViewVO vo = new UserDietTotalCaloriesViewVO();
		vo.setUser_id(user_id);

		return dietDAO.userDietDailyTotalCalorieList(vo);
	}
	
	/* ==================== POST Method ==================== */

	//User insert diet menu into the database
	@RequestMapping(value = "/diet/insertMenu", method = RequestMethod.POST)
	public void insertMenu(@RequestBody DietVO vo) throws Exception {
		dietDAO.insertMenu(vo);
	}

}
