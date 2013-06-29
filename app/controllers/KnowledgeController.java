package controllers;

import java.util.Calendar;
import java.util.List;

import models.Knowledge;
import models.User;
import play.data.validation.Required;

public class KnowledgeController extends SuperController {

	public static void showKnowledges() {
		List<Knowledge> knowledges = Knowledge.findAll();

		render(knowledges);
	}

	public static void showKnowledge(Long knowledgeId) {
		Knowledge knowledge = Knowledge.findById(knowledgeId);

		render(knowledge);
	}

	public static void createKnowledge() {
		if (!session.contains("user.id")) {
			Application.index();
		}

		render();
	}

	public static void saveKnowledge(@Required Long userId, String title, String description) {
		/* Parameters validation */
		validation.required(title).message("error.field.required");
		validation.required(description).message("error.field.required");

		if (validation.hasErrors()) {
			keepValidation();
			createKnowledge();
		}

		/* Knowledge creation */
		Knowledge knowledge = new Knowledge();
		knowledge.user = User.findById(userId);
		knowledge.title = title;
		knowledge.description = description;
		knowledge.createdDate = Calendar.getInstance().getTime();
		knowledge.save();

		showKnowledges();
	}
}
