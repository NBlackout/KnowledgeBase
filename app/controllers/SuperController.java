package controllers;

import models.User;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import play.data.validation.Required;
import play.i18n.Lang;
import play.libs.Crypto;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Router;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SuperController extends Controller {

	protected static void keepValidation() {
		flash.keep();
		params.flash();
		validation.keep();
	}

	protected static void redirectSafely(String returnUrl) {
		redirect((returnUrl != null) ? returnUrl : Router.getFullUrl("Application.index"));
	}

	protected static void updateSession(User user) {
		if (user != null) {
			session.put("user.id", user.id);
			session.put("user.username", user.username);
			session.put("user.type", user.type);
			session.put("user.location", SuperController.getLocation(request.remoteAddress));
		} else {
			session.remove("user.id");
			session.remove("user.login");
			session.remove("user.type");
			session.remove("user.location");
		}
	}

	public static void logIn(String returnUrl, String login, String password) {
		/* Parameters validation */
		validation.required(login).message("error.field.required");
		validation.required(password).message("error.field.required");

		User user = null;
		if (!validation.hasErrors()) {
			user = User.find((login.contains("@")) ? "byEmailAndPassword" : "byUsernameAndPassword", login, Crypto.encryptAES(password)).first();
			if (user == null) {
				validation.addError("login", "error.user.not.found");
			}
		}

		if (!validation.hasErrors() && user.activated == Boolean.FALSE) {
			validation.addError("login", "error.user.not.activated");
		}

		if (!validation.hasErrors()) {
			/* Add user to session */
			updateSession(user);

			redirectSafely(returnUrl);
		}

		keepValidation();

		redirectSafely(returnUrl);
	}

	public static void logOut(@Required String returnUrl) {
		/* Remove user from session */
		updateSession(null);

		redirectSafely(returnUrl);
	}

	@Before
	public static void logRequest() {
		Logger.info("[==============================================================================]");
		Logger.info(request.remoteAddress + " (" + getLocation(request.remoteAddress) + ") :");
		Logger.info("\t- URL    : " + request.method + " http://" + request.domain + ":" + request.port);
		Logger.info("\t- Action : " + request.controller + "." + request.actionMethod + "(" + new Gson().toJson(params.data) + ")");
	}

	public static void setLang(String returnUrl, String locale) {
		Lang.change(locale);

		redirectSafely(returnUrl);
	}

	private static String getLocation(String remoteAddress) {
		/* Application constants */
		String requestSite = Play.configuration.getProperty("location.request.site");
		String responseFormat = Play.configuration.getProperty("location.response.format");
		String cityKey = Play.configuration.getProperty("location.response.key.city");
		String regionKey = Play.configuration.getProperty("location.response.key.region");
		String countryKey = Play.configuration.getProperty("location.response.key.country");

		/* WebService call */
		HttpResponse response = WS.url(requestSite + "/" + responseFormat + "/" + remoteAddress).get();
		JsonObject result = response.getJson().getAsJsonObject();

		/* Retrieve JSON data */
		String city = result.get(cityKey).getAsString();
		String region = result.get(regionKey).getAsString();
		String country = result.get(countryKey).getAsString();

		return (StringUtils.isNotEmpty(city)) ? city : (StringUtils.isNotEmpty(region)) ? region : country;
	}
}
