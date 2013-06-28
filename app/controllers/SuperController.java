package controllers;

import models.User;
import play.data.validation.Required;
import play.i18n.Lang;
import play.mvc.Controller;

public class SuperController extends Controller {

	public static void setLang(String lang, String redirect) {
		Lang.change(lang);

		redirect(redirect);
	}

	public static void logIn(@Required String returnUrl, String login, String password) {
		/* Parameters validation */
		validation.required(login).message("error.field.required");
		validation.required(password).message("error.field.required");

		User user = User.find("byLoginAndPassword", login, password).first();
		if (user == null) {
			validation.addError("login", "user.not.found");
		}

		if (validation.hasErrors()) {
			flash.keep();
			params.flash();
			validation.keep();

			redirect(returnUrl);
		}

		/* Add user to session */
		session.put("user.id", user.id);
		session.put("user.login", user.login);

		redirect(returnUrl);
	}

	public static void logOut(@Required String returnUrl) {
		/* Remove user from session */
		session.remove("user.id");
		session.remove("user.login");

		redirect(returnUrl);
	}
}
