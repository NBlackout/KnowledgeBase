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

	public static void createKnowledge() {
		if (!session.contains("user.id")) {
			Application.index();
		}

		render();
	}

	public static void saveKnowledge(@Required Long userId, @Required String title, @Required String description) {
		/* Parameters validation */
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();

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
