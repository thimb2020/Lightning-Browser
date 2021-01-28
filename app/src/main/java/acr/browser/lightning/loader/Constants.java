/*
 * global Browser for Android
 * 
 * Copyright (C) 2010 J. Devauchelle and contributors.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package acr.browser.lightning.loader;

import java.text.Collator;
import java.util.Locale;

/**
 * Defines constants.
 */
public class Constants {
	public static int AppType = 0; //0:US, 1: Other
	static final public String PREFS_PASSWORD = "password";
	static final public String PREFS_PASSWORDVD = "passwordvd";
	public static final int REQUEST_PASSWORD_PROTECTION_CODE = 5;
	public static final Collator COLLATOR = Collator.getInstance(Locale.FRANCE);
	public static String USER_AGENT_DESKTOP = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.7 (KHTML, like Gecko) Chrome/7.0.517.44 Safari/534.7";
	
	static final public String DB_NAME = "online";
	static final public String DOWNLOAD_FOLDER = "worldnews";
	public static final float MENU_WITH = 270.0f;
	public static final float MENU_WITH_SMALL = 200.0f;
	
	public static String GOOGLE_FAVIICON_URL= "http://www.google.com/s2/favicons?domain_url=";
	public static String URL_DATA = "https://dl.dropboxusercontent.com/u/256490628/worldnews/";
	static final public String PREFS_CONFIG= "PREFS_CONFIG";
	static final public String PREFS_LOGIN_COUNT= "PREFS_COUNT_LOGIN";
	static final public String SHARED= "SHARED_APP";
	static final public String RATED= "RATED_APP";
	
	
}
