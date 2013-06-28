package models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import play.db.jpa.GenericModel;

@MappedSuperclass
public class SuperModel extends GenericModel {

	@Id
	@GeneratedValue
	@Column(name = "id")
	public Long id;
}
