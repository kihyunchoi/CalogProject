package com.calog.persistence;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.calog.domain.DietTypeDailyCalorieSumVO;
import com.calog.domain.DietMenuVO;
import com.calog.domain.DietVO;
import com.calog.domain.UserDietViewVO;
import com.calog.domain.UserDietTotalCaloriesViewVO;

@Repository
public class DietDAOImpl implements DietDAO {
	@Inject
	SqlSession session;

	private static String namespace = "DietMapper";

	@Override
	public UserDietTotalCaloriesViewVO userDietDailyTotalCalorie(UserDietTotalCaloriesViewVO vo) throws Exception {
		return session.selectOne(namespace + ".userDietDailyTotalCalorie", vo);
	}

	@Override
	public List<UserDietTotalCaloriesViewVO> userDietDailyTotalCalorieList(UserDietTotalCaloriesViewVO vo)
			throws Exception {
		return session.selectList(namespace + ".userDietDailyTotalCalorieList", vo);
	}

	@Override
	public List<UserDietViewVO> userDietDailyMenuList(UserDietViewVO vo) throws Exception {
		return session.selectList(namespace + ".userDietDailyMenuList", vo);
	}

	@Override
	public List<DietMenuVO> dietMenuList(String keyword) throws Exception {
		return session.selectList(namespace + ".dietMenuList", keyword);
	}

	@Override
	public List<DietTypeDailyCalorieSumVO> userDietTypeDailyCalorieSum(DietTypeDailyCalorieSumVO vo) throws Exception {
		return session.selectList(namespace + ".userDietTypeDailyCalorieSum", vo);
	}

	@Override
	public List<UserDietTotalCaloriesViewVO> graphDietData(UserDietTotalCaloriesViewVO vo, String unit)
			throws Exception {
		return session.selectList(namespace + ".graphDietData", vo);
	}

	@Override
	public List<DietMenuVO> userFavoriteMenuList(String user_id) {
		return session.selectList(namespace + ".userFavoriteMenuList", user_id);
	}

	@Override
	public void insertMenu(DietVO vo) {
		session.insert(namespace + ".insertMenu", vo);
	}

}