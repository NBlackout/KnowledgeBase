package controllers;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import models.Comment;
import models.Knowledge;
import models.User;
import play.mvc.Router;
import play.mvc.Router.ActionDefinition;

public class CommentController extends SuperController {

	public static void deleteComment(Long commentId) {
		/* Comment deletion */
		Comment comment = Comment.<Comment> findById(commentId);
		Long knowledgeId = comment.knowledge.id;
		comment.delete();

		KnowledgeController.showKnowledge(knowledgeId);
	}

	public static void saveComment(Long commentId, Long knowledgeId, String content) {
		if (!session.contains("user.id")) {
			KnowledgeController.showKnowledge(knowledgeId);
		}

		/* Parameters validation */
		validation.required(content).message("error.field.required");

		if (validation.hasErrors()) {
			keepValidation();
			KnowledgeController.showKnowledge(knowledgeId);
		}

		/* Create or update a comment */
		Comment comment = null;
		Long userId = Long.parseLong(session.get("user.id"));
		if (commentId == null) {
			comment = new Comment();
			comment.user = User.findById(userId);
			comment.knowledge = Knowledge.findById(knowledgeId);
			comment.content = content;
			comment.createdDate = Calendar.getInstance().getTime();
			comment.save();
		} else {
			comment = Comment.findById(commentId);

			if (comment == null || comment.user.id != userId) {
				KnowledgeController.showKnowledge(knowledgeId);
			}

			comment.content = content;
			comment.save();
		}

		/* Add anchor for redirection */
		Map<String, Object> args = new HashMap<>();
		args.put("knowledgeId", comment.knowledge.id);

		ActionDefinition action = Router.reverse("KnowledgeController.showKnowledge", args);
		action.addRef(Long.toString(comment.id));

		redirect(action.url);
	}
}
