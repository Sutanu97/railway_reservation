package dao;

import entity.User;

public interface UserDao {

	public boolean newUserSignup(User user);

	public User loginUser(String irctcId, String irctcPassword);

}
