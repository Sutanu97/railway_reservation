package entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name="lu_user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="pk_user_id")
	private Integer pkUserId;
	private String name;
	private Integer age;
	private String gender;
	@Column(name="email_id")
	private String emailId;
	@Column(name="contact_no")
	private Long contactNumber;
	@Column(name = "irctc_id")
	private String irctcId;
	@Column(name = "irctc_password")
	private String irctcPassword;
	@Column(name="signup_date")
	private LocalDateTime signUpDate;
	public Integer getPkUserId() {
		return pkUserId;
	}
	public void setPkUserId(Integer pkUserId) {
		this.pkUserId = pkUserId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public Long getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(Long contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getIrctcId() {
		return irctcId;
	}
	public void setIrctcId(String irctcId) {
		this.irctcId = irctcId;
	}
	public String getIrctcPassword() {
		return irctcPassword;
	}
	public void setIrctcPassword(String irctcPassword) {
		this.irctcPassword = irctcPassword;
	}
	public LocalDateTime getSignUpDate() {
		return signUpDate;
	}
	public void setSignUpDate(LocalDateTime signUpDate) {
		this.signUpDate = signUpDate;
	}
	
	
	
	
}
