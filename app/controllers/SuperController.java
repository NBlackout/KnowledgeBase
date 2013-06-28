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

	public static void logIn(@Required String returnUrl, @Required String login, @Required String password) {
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
		} else {
			User user = User.find("byLoginAndPassword", login, password).first();
			if (user == null) {
				flash.error("user.not.found");

				flash.keep();
				params.flash();
				validation.keep();
			} else {
				session.put("user.id", user.id);
				session.put("user.login", user.login);
			}
		}

		redirect(returnUrl);
	}

	public static void logOut(@Required String returnUrl) {
		session.remove("user.id");
		session.remove("user.login");

		redirect(returnUrl);
	}
}
