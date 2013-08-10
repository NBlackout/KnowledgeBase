package notifiers;

import models.User;
import play.Play;
import play.i18n.Messages;
import play.libs.Crypto;
import play.mvc.Mailer;

public class Mail extends Mailer {

	private static final String FROM = "Knowledge Base <KnowledgeBase@no-reply.fr>";

	public static void sendActivationEmail(User user) {
		setSubject(Messages.get("mail.activation.subject"));
		setFrom(Play.configuration.getProperty(FROM));
		addRecipient(user.email);

		String userUsername = user.username;
		String userEmail = Crypto.encryptAES(user.email);

		send(userUsername, userEmail);
	}
}
