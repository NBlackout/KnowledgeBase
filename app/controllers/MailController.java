package controllers;

import models.User;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import play.Logger;

public class MailController extends SuperController {

	public static void activateUser(Long userId) {
		User user = User.findById(userId);
		user.activated = Boolean.TRUE;
		user.save();
	}

	public static void sendActivationEmail(User user) {
		try {
			HtmlEmail activation = new HtmlEmail();
			activation.setFrom("knowledge-base@no-reply.fr", "KnowledgeBase");
			activation.addTo(user.email);
			activation.setSubject("Registration");
			activation.setHtmlMsg("<a href=\"@{UserController.validateUser(user.id)}\">Cliquez-ici</a>");
		} catch (EmailException e) {
			Logger.error(e, "Erreur lors de l'envoi de l'email");
		}
	}
}
