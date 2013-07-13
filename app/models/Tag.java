package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;

import com.google.gson.annotations.Expose;

@Entity(name = "Tag")
public class Tag extends SuperModel {

	private static final long serialVersionUID = -5880369341747526042L;

	@Expose
	@Column(name = "name")
	public String name;

	@Expose
	@Column(name = "description")
	public String description;

	@ManyToMany(mappedBy = "tags")
	public List<Knowledge> knowledges;

	@PreRemove
	public void preRemove() {
		for (Knowledge knowledge : knowledges) {
			knowledge.tags.remove(this);
			knowledge.save();
		}
	}
}
