package controllers;

import models.User;
import notifiers.Mail;

import org.apache.commons.mail.EmailException;
import org.h2.util.StringUtils;

import play.libs.Crypto;

public class UserController extends SuperController {

	public static void activateUser(String activationToken) {
		User user = User.find("byEmail", Crypto.decryptAES(activationToken)).first();

		if (user != null) {
			user.activated = Boolean.TRUE;
			user.save();

			/* Add user to session */
			session.put("user.id", user.id);
			session.put("user.username", user.username);
			session.put("user.type", user.type);
		}

		Application.index();
	}

	public static void editAccount() {
		if (!session.contains("user.id")) {
			Application.index();
		}

		User user = User.findById(Long.parseLong(session.get("user.id")));
		render(user);
	}

	public static void saveAccount(String returnUrl, Long userId, String email, String username, String passwordOld, String passwordNew, String passwordRetype) {
		/* Parameters validation */
		validation.required(email).message("error.field.required");
		validation.required(username).message("error.field.required");
		validation.required(passwordOld).message("error.field.required");
		validation.required(passwordNew).message("error.field.required");
		validation.required(passwordRetype).message("error.field.required");

		User user = User.findById(userId);
		if (!user.password.equals(Crypto.encryptAES(passwordOld))) {
			validation.addError("passwordOld", "error.user.password.wrong");
		}

		if (!passwordNew.equals(passwordRetype)) {
			validation.addError("passwordRetype", "error.user.passwords.not.equals");
		}

		if (validation.hasErrors()) {
			keepValidation();
			editAccount();
		}

		/* Account modification */
		user.email = email;
		user.username = username;
		user.password = Crypto.encryptAES(passwordNew);
		user.save();

		Application.index();
	}

	public static void saveUser(String email, String username, String password, String passwordRetype) throws EmailException {
		/* Parameters validation */
		validation.required(email).message("error.field.required");
		validation.required(username).message("error.field.required");
		validation.required(password).message("error.field.required");
		validation.required(passwordRetype).message("error.field.required");

		if (User.count("byEmail", email) != 0) {
			validation.addError("email", "error.user.email.already.exists");
		}

		if (User.count("byUsername", username) != 0) {
			validation.addError("username", "error.user.username.already.exists");
		}

		if (!password.equals(passwordRetype)) {
			validation.addError("passwordRetype", "error.user.passwords.not.equals");
		}

		if (validation.hasErrors()) {
			keepValidation();
			signUp();
		}

		/* User creation */
		User user = new User();
		user.email = email;
		user.username = username;
		user.password = Crypto.encryptAES(password);
		user.activated = Boolean.FALSE;
		user.type = "User";
		user.save();

		/* Activation mail sending */
		Mail.sendActivationEmail(user);

		Application.index();
	}

	public static void signUp() {
		if (session.contains("user.id")) {
			Application.index();
		}

		render();
	}
}
