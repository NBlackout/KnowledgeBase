package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

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

	@ManyToMany
	@JoinTable(name = "Knowledge_Tag", joinColumns = { @JoinColumn(name = "knowledgeId") }, inverseJoinColumns = { @JoinColumn(name = "tagId") })
	public List<Tag> tags;

	@OneToMany(mappedBy = "knowledge")
	public List<Comment> comments;

	@PreRemove
	public void preRemove() {
		for (Comment comment : comments) {
			comment.delete();
		}
	}
}
