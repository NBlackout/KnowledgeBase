package controllers;

import java.util.List;

import models.Tag;
import play.Play;

public class TagController extends SuperController {

	public static void createTag() {
		render();
	}

	public static void editTag(Long tagId) {
		if (!session.contains("user.id")) {
			Application.index();
		}

		if (!session.contains("user.type") || !session.get("user.type").equals("Administrator")) {
			Application.index();
		}

		Tag tag = Tag.findById(tagId);

		render(tag);
	}

	public static void findTagsByNameLike(String name) {
		Integer maxSize = Integer.parseInt(Play.configuration.getProperty("suggestions.max.size"));
		List<Tag> tags = Tag.find("name like ? order by name asc", "%" + name + "%").<Tag> fetch(maxSize);

		renderJSON(tags);
	}

	public static void saveTag(Long tagId, String name, String description) {
		/* Parameters validation */
		validation.required(name).message("error.field.required");
		validation.required(description).message("error.field.required");

		if (validation.hasErrors()) {
			keepValidation();

			if (tagId != null) {
				editTag(tagId);
			} else {
				createTag();
			}
		}

		/* Tag creation */
		Tag tag = (tagId != null) ? Tag.<Tag> findById(tagId) : new Tag();
		tag.name = name;
		tag.description = description;
		tag.save();

		showTag(tag.id);
	}

	public static void showTag(Long tagId) {
		Tag tag = Tag.findById(tagId);

		render(tag);
	}

	public static void showTags() {
		List<Tag> tags = Tag.findAll();

		render(tags);
	}
}
