package controllers;

import models.User;
import play.Play;
import play.data.validation.Required;
import play.i18n.Lang;
import play.libs.Crypto;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;
import play.mvc.Router;

import com.google.gson.JsonObject;

public class SuperController extends Controller {

	public static void logIn(String returnUrl, String login, String password) {
		/* Parameters validation */
		validation.required(login).message("error.field.required");
		validation.required(password).message("error.field.required");

		User user = User.find((login.contains("@")) ? "byEmailAndPassword" : "byUsernameAndPassword", login, Crypto.encryptAES(password)).first();
		if (user == null) {
			validation.addError("login", "error.user.not.found");
		}

		if (user != null && user.activated == Boolean.FALSE) {
			validation.addError("login", "error.user.not.activated");
		}

		if (validation.hasErrors()) {
			keepValidation();
			redirectSafely(returnUrl);
		}

		/* Retrieve location by ip */
		String requestSite = Play.configuration.getProperty("location.request.site");
		String responseFormat = Play.configuration.getProperty("location.response.format");
		String cityKey = Play.configuration.getProperty("location.response.key.city");
		String regionKey = Play.configuration.getProperty("location.response.key.region");

		HttpResponse response = WS.url(requestSite + "/" + responseFormat).get();
		JsonObject result = response.getJson().getAsJsonObject();

		String city = result.get(cityKey).getAsString();
		String region = result.get(regionKey).getAsString();
		String location = (city != null) ? city : region;

		/* Add user to session */
		session.put("user.id", user.id);
		session.put("user.username", user.username);
		session.put("user.type", user.type);
		session.put("user.location", location);

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
