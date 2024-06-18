package manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.UserDao;
import entity.User;
import manager.UserManager;

@Service
public class UserManagerImpl implements UserManager {

	@Autowired
	private UserDao userDao;

	@Override
	public boolean newUserSignup(User user) {
		return userDao.newUserSignup(user);
	}

	@Override
	public User loginUser(String irctcId, String irctcPassword) {
		return userDao.loginUser(irctcId, irctcPassword);
	}

}
