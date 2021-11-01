package com.calog.persistence;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.calog.domain.SleepingVO;

@Repository
public class SleepingDAOImpl implements SleepingDAO {
	@Inject
	SqlSession session;

	private static String namespace = "SleepingMapper";

	@Override
	public List<SleepingVO> graphSleepingData(SleepingVO vo) {
		return session.selectList(namespace + ".graphSleepingData", vo);
	}

	@Override
	public SleepingVO userSleepingDailyInfo(SleepingVO vo) throws Exception {
		return session.selectOne(namespace + ".userSleepingDailyInfo", vo);
	}

	@Override
	public void userSleepInsert(SleepingVO vo) throws Exception {
		session.insert(namespace + ".userSleepInsert", vo);
	}

}
