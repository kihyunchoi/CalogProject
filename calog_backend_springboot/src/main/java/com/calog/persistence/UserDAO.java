package com.calog.persistence;

import java.util.List;

import com.calog.domain.UserVO;

public interface UserDAO {

	public List<UserVO> userList() throws Exception;

	public UserVO userLogin(UserVO vo) throws Exception;

	public UserVO readUser(UserVO vo) throws Exception;

	public void createUser(UserVO vo) throws Exception;

	public void updateUser(UserVO vo) throws Exception;

	public void deleteUser(String user_id) throws Exception;

}
