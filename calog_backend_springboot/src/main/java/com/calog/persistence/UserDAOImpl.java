package com.calog.persistence;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.calog.domain.UserVO;

@Repository
public class UserDAOImpl implements UserDAO {

	@Inject
	SqlSession session;

	private static String namespace = "UserMapper";

	@Override
	public List<UserVO> userList() throws Exception {
		return session.selectList(namespace + ".userList");
	}

	@Override
	public UserVO userLogin(UserVO vo) throws Exception {
		return session.selectOne(namespace + ".userLogin", vo);
	}

	@Override
	public UserVO readUser(UserVO vo) throws Exception {
		return session.selectOne(namespace + ".readUser", vo);
	}

	@Override
	public void createUser(UserVO vo) throws Exception {
		session.insert(namespace + ".createUser", vo);
	}

	@Override
	public void updateUser(UserVO vo) throws Exception {
		session.update(namespace + ".updateUser", vo);

	}

	@Override
	public void deleteUser(String user_id) throws Exception {
		session.delete(namespace + ".deleteUser", user_id);

	}

}
