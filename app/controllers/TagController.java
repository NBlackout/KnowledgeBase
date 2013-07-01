package controllers;

import java.util.List;

import models.Tag;
import play.Play;

public class TagController extends SuperController {

	public static void findTagsByNameLike(String name) {
		Integer maxSize = Integer.parseInt(Play.configuration.getProperty("suggestions.max.size"));
		List<Tag> tags = Tag.find("name like ? order by name asc", "%" + name + "%").<Tag> fetch(maxSize);

		renderJSON(tags);
	}
}
