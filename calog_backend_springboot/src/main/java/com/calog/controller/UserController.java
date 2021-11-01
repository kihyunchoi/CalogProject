package com.calog.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.calog.domain.UserVO;
import com.calog.persistence.UserDAO;

@RestController
@RequestMapping("/calog")
public class UserController {

	@Inject
	UserDAO userDao;

	/* ==================== GET Method ==================== */
	//User list
	@RequestMapping(value = "/user/list", method = RequestMethod.GET)
	public List<UserVO> userList() throws Exception {
		return userDao.userList();
	}

	//User signIn
	@RequestMapping(value = "/user/login", method = RequestMethod.GET)
	public UserVO userLogin(String user_id, String password) throws Exception {
		UserVO vo = new UserVO();
		vo.setUser_id(user_id);
		vo.setPassword(password);

		return userDao.userLogin(vo);
	}

	//Read user info from the database
	@RequestMapping(value = "/user/read", method = RequestMethod.GET)
	public UserVO readUser(String user_id) throws Exception {
		UserVO vo = new UserVO();
		vo.setUser_id(user_id);

		return userDao.readUser(vo);
	}

	/* ==================== POST Method ==================== */
	//Create a user and insert into database
	@RequestMapping(value = "/user/create", method = RequestMethod.POST)
	public void createUser(@RequestBody UserVO vo) throws Exception {
		userDao.createUser(vo);
	}

	//Update a user info
	@RequestMapping(value = "/user/update", method = RequestMethod.POST)
	public void updateUser(@RequestBody UserVO vo) throws Exception {
		userDao.updateUser(vo);
	}

	//Delete user info
	@RequestMapping(value = "/user/delete", method = RequestMethod.POST)
	public void deleteUser(String user_id) throws Exception {
		userDao.deleteUser(user_id);
	}

}
