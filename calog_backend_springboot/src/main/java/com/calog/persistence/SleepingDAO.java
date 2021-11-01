package com.calog.persistence;

import java.util.List;

import com.calog.domain.SleepingVO;

public interface SleepingDAO {

	public List<SleepingVO> graphSleepingData(SleepingVO vo);

	public SleepingVO userSleepingDailyInfo(SleepingVO vo) throws Exception;

	public void userSleepInsert(SleepingVO vo) throws Exception;
}
