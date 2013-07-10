package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity(name = "Tag")
public class Tag extends SuperModel {

	private static final long serialVersionUID = -5880369341747526042L;

	@Column(name = "name")
	public String name;

	@Column(name = "description")
	public String description;

	@ManyToMany(mappedBy = "tags")
	public List<Knowledge> knowledges;
}
