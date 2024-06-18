package dao.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dao.UserDao;
import entity.User;

@Repository
public class UserDaoImpl implements UserDao{

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	@Transactional
	public boolean newUserSignup(User user) {
		if(user.getSignUpDate() == null) {
			user.setSignUpDate(LocalDateTime.now());
		}
		try {
			hibernateTemplate.persist(user);
		}catch(Exception e) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public User loginUser(String irctcId, String irctcPassword) {
		String query = "from User where irctcId = :irctcId and irctcPassword = :irctcPassword";
		Query<User> hql = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(query);
		hql.setParameter("irctcId", irctcId);
		hql.setParameter("irctcPassword", irctcPassword);
		List<User> users = hql.list();
		if(users.size() == 1) {
			return users.get(0);
		}
		return null;
	}
	
}
