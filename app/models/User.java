package models;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "User")
public class User extends SuperModel {

	private static final long serialVersionUID = 1021743377392045379L;

	@Column(name = "email")
	public String email;

	@Column(name = "username")
	public String username;

	@Column(name = "password")
	public String password;

	@Column(name = "activated")
	public Boolean activated;

	@Column(name = "type")
	public String type;
}
