package manager;

import entity.User;

public interface UserManager {
	public boolean newUserSignup(User user);

	public User loginUser(String irctcId, String irctcPassword);
}
