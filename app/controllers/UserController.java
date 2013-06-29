package controllers;

import models.User;

public class UserController extends SuperController {

	public static void saveUser(String login, String password) {
		/* Parameters validation */
		validation.required(login).message("error.field.required");
		validation.required(password).message("error.field.required");

		if (User.count("byLogin", login) != 0) {
			validation.addError("login", "user.already.exists");
		}

		if (validation.hasErrors()) {
			keepValidation();
			signUp();
		}

		/* User creation */
		User user = new User();
		user.login = login;
		user.password = password;
		user.save();

		/* User connection */
		session.put("user.id", user.id);
		session.put("user.login", user.login);

		Application.index();
	}

	public static void signUp() {
		if (session.contains("user.id")) {
			Application.index();
		}

		render();
	}
}
