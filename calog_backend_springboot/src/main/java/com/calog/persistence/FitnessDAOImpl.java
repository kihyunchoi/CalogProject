package com.calog.persistence;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.calog.domain.FitnessMenuVO;
import com.calog.domain.FitnessVO;
import com.calog.domain.UserFitnessTotalCaloriesViewVO;
import com.calog.domain.UserFitnessViewVO;

@Repository
public class FitnessDAOImpl implements FitnessDAO {
	@Inject
	SqlSession session;

	private static String namespace = "FitnessMapper";

	@Override
	public List<FitnessMenuVO> fitnessMenuList() throws Exception {
		return session.selectList(namespace + ".fitnessMenuList");
	}

	@Override
	public List<UserFitnessTotalCaloriesViewVO> graphFitnessData(UserFitnessTotalCaloriesViewVO vo) {
		return session.selectList(namespace + ".graphFitnessData", vo);
	}

	@Override
	public UserFitnessTotalCaloriesViewVO userFitnessDailyTotalCalorie(UserFitnessTotalCaloriesViewVO vo)
			throws Exception {
		return session.selectOne(namespace + ".userFitnessDailyTotalCalorie", vo);
	}

	@Override
	public List<UserFitnessViewVO> userFitnessDailyMenuList(UserFitnessViewVO vo) throws Exception {
		return session.selectList(namespace + ".userFitnessDailyMenuList", vo);
	}

	@Override
	public void userFitnessInsert(FitnessVO vo) throws Exception {
		session.insert(namespace + ".userFitnessInsert", vo);
	}
}