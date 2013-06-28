package models;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "User")
public class User extends SuperModel {

	@Column(name = "login")
	public String login;

	@Column(name = "password")
	public String password;
}
