package controllers;

import play.i18n.Lang;
import play.mvc.Controller;

public class SuperController extends Controller {

	public static void setLang(String lang, String redirect) {
		Lang.change(lang);

		redirect(redirect);
	}
}
