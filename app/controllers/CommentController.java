package controllers;

import java.util.Calendar;

import models.Comment;
import models.Knowledge;
import models.User;
import play.data.validation.Required;

public class CommentController extends SuperController {

	public static void deleteComment(Long commentId) {
		/* Comment deletion */
		Comment comment = Comment.<Comment> findById(commentId);
		Long knowledgeId = comment.knowledge.id;
		comment.delete();

		KnowledgeController.showKnowledge(knowledgeId);
	}

	public static void saveComment(Long commentId, Long knowledgeId, @Required Long userId, String content) {
		validation.required(content).message("error.field.required");

		if (validation.hasErrors()) {
			keepValidation();
			KnowledgeController.showKnowledge(knowledgeId);
		}

		/* Comment creation */
		Comment comment = (commentId != null) ? Comment.<Comment> findById(commentId) : new Comment();
		comment.user = User.findById(userId);
		comment.knowledge = Knowledge.findById(knowledgeId);
		comment.content = content;
		comment.createdDate = (commentId != null) ? comment.createdDate : Calendar.getInstance().getTime();
		comment.save();

		KnowledgeController.showKnowledge(knowledgeId);
	}
}
