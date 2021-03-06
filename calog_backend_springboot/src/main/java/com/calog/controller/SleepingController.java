package com.calog.controller;

import java.sql.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.calog.domain.SleepingVO;
import com.calog.persistence.SleepingDAO;

@RestController
@RequestMapping("/calog")
public class SleepingController {

	@Inject
	SleepingDAO sleepingDAO;

	/* ==================== GET Method ==================== */
	//User sleep daily info
	@RequestMapping(value = "/sleep/userSleepingDailyInfo", method = RequestMethod.GET)
	public SleepingVO userSleepingDailyInfo(String user_id, String sleeping_date) throws Exception {
		SleepingVO vo = new SleepingVO();
		vo.setUser_id(user_id);
		vo.setSleeping_date(Date.valueOf(sleeping_date));

		return sleepingDAO.userSleepingDailyInfo(vo);
	}

	//User sleep graph data
	@RequestMapping(value = "/sleep/graphSleepingData", method = RequestMethod.GET)
	public List<SleepingVO> graphSleepingData(String user_id, String start_date, String unit_date) throws Exception {
		SleepingVO vo = new SleepingVO();
		vo.setUser_id(user_id);
		vo.setSleeping_date(Date.valueOf(start_date));

		return sleepingDAO.graphSleepingData(vo);
	}

	/* ==================== POST Method ==================== */
	//User sleep info insert into database
	@RequestMapping(value = "/sleep/userSleepInsert", method = RequestMethod.POST)
	public void userSleepinsert(@RequestBody SleepingVO vo) throws Exception {
		sleepingDAO.userSleepInsert(vo);
	}
}
