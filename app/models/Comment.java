package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "Comment")
public class Comment extends SuperModel {

	private static final long serialVersionUID = 7774617948648112335L;

	@ManyToOne
	@JoinColumn(name = "userId")
	public User user;

	@ManyToOne
	@JoinColumn(name = "knowledgeId")
	public Knowledge knowledge;

	@Column(name = "content", columnDefinition = "text")
	public String content;

	@Column(name = "createdDate")
	public Date createdDate;
}
