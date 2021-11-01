package com.calog.persistence;

import java.util.List;

import com.calog.domain.DietTypeDailyCalorieSumVO;
import com.calog.domain.DietMenuVO;
import com.calog.domain.DietVO;
import com.calog.domain.UserDietViewVO;
import com.calog.domain.UserDietTotalCaloriesViewVO;

public interface DietDAO {

	public UserDietTotalCaloriesViewVO userDietDailyTotalCalorie(UserDietTotalCaloriesViewVO vo) throws Exception;

	public List<UserDietTotalCaloriesViewVO> graphDietData(UserDietTotalCaloriesViewVO vo, String unit)
			throws Exception;

	public List<UserDietTotalCaloriesViewVO> userDietDailyTotalCalorieList(UserDietTotalCaloriesViewVO vo)
			throws Exception;

	public List<UserDietViewVO> userDietDailyMenuList(UserDietViewVO vo) throws Exception;

	public List<DietMenuVO> dietMenuList(String keyword) throws Exception;

	public List<DietTypeDailyCalorieSumVO> userDietTypeDailyCalorieSum(DietTypeDailyCalorieSumVO vo) throws Exception;

	public List<DietMenuVO> userFavoriteMenuList(String user_id);

	public void insertMenu(DietVO vo);

}