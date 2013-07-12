package notifiers;

import models.User;
import play.Play;
import play.i18n.Messages;
import play.libs.Crypto;
import play.mvc.Mailer;

public class Mail extends Mailer {

	public static void sendActivationEmail(User user) {
		setSubject(Messages.get("mail.activation.subject"));
		setFrom(Play.configuration.getProperty("mail.from"));
		addRecipient(user.email);

		String userUsername = user.username;
		String userEmail = Crypto.encryptAES(user.email);
		send(userUsername, userEmail);
	}
}
