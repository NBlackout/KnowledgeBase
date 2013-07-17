package controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import models.Comment;
import models.Knowledge;
import models.Tag;
import models.User;
import play.data.binding.As;
import play.data.validation.Required;

public class KnowledgeController extends SuperController {

	public static void createKnowledge() {
		if (!session.contains("user.id")) {
			Application.index();
		}

		render();
	}

	public static void editKnowledge(Long knowledgeId) {
		if (!session.contains("user.id")) {
			Application.index();
		}

		Knowledge knowledge = Knowledge.findById(knowledgeId);
		if (knowledge.user.id != Long.parseLong(session.get("user.id"))) {
			Application.index();
		}

		render(knowledge);
	}

	public static void exportKnowledges() throws IOException {
		StringBuilder builder = new StringBuilder();

		/* Headers */
		builder.append("Title");
		builder.append(";");
		builder.append("Description");
		builder.append(";");
		builder.append("Tags");
		builder.append(System.getProperty("line.separator"));

		/* Lines */
		for (Knowledge knowledge : Knowledge.<Knowledge> findAll()) {
			builder.append(knowledge.title);
			builder.append(";");
			builder.append(knowledge.description.replace(System.getProperty("line.separator"), ""));
			builder.append(";");

			for (int i = 0; i < knowledge.tags.size(); i++) {
				builder.append(knowledge.tags.get(i).name);
				if (i < knowledge.tags.size() - 1) {
					builder.append(",");
				}
			}

			builder.append(System.getProperty("line.separator"));
		}

		response.setContentTypeIfNotSet("text/csv");
		response.setHeader("Content-Disposition", "attachment;filename=knowledges.csv");
		renderBinary(new ByteArrayInputStream(builder.toString().getBytes()));
	}

	public static void removeKnowledge(Long knowledgeId) {
		if (!session.contains("user.id")) {
			Application.index();
		}

		Knowledge knowledge = Knowledge.findById(knowledgeId);
		if (knowledge.user.id != Long.parseLong(session.get("user.id"))) {
			Application.index();
		}

		knowledge.delete();

		showKnowledges();
	}

	public static void saveKnowledge(Long knowledgeId, @Required Long userId, String title, String description, @As(",") List<Long> tagIds) {
		/* Parameters validation */
		validation.required(title).message("error.field.required");
		validation.required(description).message("error.field.required");
		validation.required(tagIds).message("error.field.required");

		if (validation.hasErrors()) {
			keepValidation();

			if (knowledgeId != null) {
				editKnowledge(knowledgeId);
			} else {
				createKnowledge();
			}
		}

		/* Knowledge creation */
		Knowledge knowledge = (knowledgeId != null) ? Knowledge.<Knowledge> findById(knowledgeId) : new Knowledge();
		knowledge.user = User.findById(userId);
		knowledge.title = title;
		knowledge.description = description;
		knowledge.createdDate = Calendar.getInstance().getTime();
		knowledge.tags = Tag.find("id in :tagIds").bind("tagIds", tagIds).fetch();
		knowledge.save();

		showKnowledge(knowledge.id);
	}

	public static void showKnowledge(Long knowledgeId) {
		Knowledge knowledge = Knowledge.findById(knowledgeId);
		List<Comment> comments = Comment.find("byKnowledge", knowledge).fetch();

		render(knowledge, comments);
	}

	public static void showKnowledges() {
		List<Knowledge> knowledges = Knowledge.findAll();

		render(knowledges);
	}
}
