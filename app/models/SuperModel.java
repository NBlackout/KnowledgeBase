package models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import play.db.jpa.GenericModel;

import com.google.gson.annotations.Expose;

@MappedSuperclass
public class SuperModel extends GenericModel {

	private static final long serialVersionUID = -8340965013288317431L;

	@Id
	@GeneratedValue
	@Expose
	@Column(name = "id")
	public Long id;
}
