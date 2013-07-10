package controllers;

import models.User;
import play.data.validation.Required;
import play.i18n.Lang;
import play.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Router;

public class SuperController extends Controller {

	public static void logIn(String returnUrl, String login, String password) {
		/* Parameters validation */
		validation.required(login).message("error.field.required");
		validation.required(password).message("error.field.required");

		User user = User.find((login.contains("@")) ? "byEmailAndPassword" : "byUsernameAndPassword", login, Crypto.encryptAES(password)).first();
		if (user == null) {
			validation.addError("login", "error.user.not.found");
		}

		if (user.activated == Boolean.FALSE) {
			validation.addError("login", "error.user.not.activated");
		}

		if (validation.hasErrors()) {
			keepValidation();
			redirectSafely(returnUrl);
		}

		/* Add user to session */
		session.put("user.id", user.id);
		session.put("user.username", user.username);
		session.put("user.type", user.type);

		redirectSafely(returnUrl);
	}

	public static void logOut(@Required String returnUrl) {
		/* Remove user from session */
		session.remove("user.id");
		session.remove("user.login");
		session.remove("user.type");

		redirectSafely(returnUrl);
	}

	public static void setLang(String returnUrl, String locale) {
		Lang.change(locale);

		redirectSafely(returnUrl);
	}

	protected static void keepValidation() {
		flash.keep();
		params.flash();
		validation.keep();
	}

	protected static void redirectSafely(String returnUrl) {
		redirect(returnUrl != null ? returnUrl : Router.getFullUrl("Application.index"));
	}
}
