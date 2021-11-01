package com.calog.persistence;

import java.util.List;

import com.calog.domain.FitnessMenuVO;
import com.calog.domain.FitnessVO;
import com.calog.domain.UserFitnessTotalCaloriesViewVO;
import com.calog.domain.UserFitnessViewVO;

public interface FitnessDAO {
	public List<UserFitnessTotalCaloriesViewVO> graphFitnessData(UserFitnessTotalCaloriesViewVO vo);

	public List<FitnessMenuVO> fitnessMenuList() throws Exception;

	public UserFitnessTotalCaloriesViewVO userFitnessDailyTotalCalorie(UserFitnessTotalCaloriesViewVO vo)
			throws Exception;

	public List<UserFitnessViewVO> userFitnessDailyMenuList(UserFitnessViewVO vo) throws Exception;

	public void userFitnessInsert(FitnessVO vo) throws Exception;
}
