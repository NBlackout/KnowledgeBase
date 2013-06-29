package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "Knowledge")
public class Knowledge extends SuperModel {

	private static final long serialVersionUID = 8866226355372390055L;

	@ManyToOne
	@JoinColumn(name = "userId")
	public User user;

	@Column(name = "title")
	public String title;

	@Column(name = "description", columnDefinition = "text")
	public String description;

	@Column(name = "createdDate")
	public Date createdDate;
}
