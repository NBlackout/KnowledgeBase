package controllers;

import models.User;

import org.apache.commons.mail.EmailException;

import play.libs.Crypto;

public class UserController extends SuperController {

	public static void saveUser(String email, String username, String password) throws EmailException {
		/* Parameters validation */
		validation.required(email).message("error.field.required");
		validation.required(username).message("error.field.required");
		validation.required(password).message("error.field.required");

		if (User.count("byEmail", email) != 0) {
			validation.addError("email", "error.user.email.already.exists");
		}

		if (User.count("byUsername", username) != 0) {
			validation.addError("username", "error.user.username.already.exists");
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
		MailController.sendActivationEmail(user);

		Application.index();
	}

	public static void signUp() {
		if (session.contains("user.id")) {
			Application.index();
		}

		render();
	}
}
