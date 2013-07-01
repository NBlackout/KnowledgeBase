package models;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "Tag")
public class Tag extends SuperModel {

	private static final long serialVersionUID = -5880369341747526042L;

	@Column(name = "name")
	public String name;

	@Column(name = "description")
	public String description;
}
