package com.lcshidai.lc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class XML3R3Manager {

	public static final String XML3R3M = "XML3R3M";
	public static final String XML3R3MVALUE = "XML3R3MVALUE";
	public static final String XML3R3VERSION = "XML3R3VERSION";

	public static void saveXml(Context content, ValueVersion model) {
		try {
			SharedPreferences user = content.getSharedPreferences(XML3R3M, 0);
			Editor editor = user.edit();
			editor.putString(XML3R3MVALUE, model.value);
			editor.putString(XML3R3VERSION, model.version);
			editor.apply();
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static ValueVersion getCrcmodel(Context context) {
		ValueVersion model = new XML3R3Manager().new ValueVersion();
		try {
			SharedPreferences xml = context.getSharedPreferences(XML3R3M, 0);
			model.value = xml.getString(XML3R3MVALUE, "");
			model.version = xml.getString(XML3R3VERSION, "");
		} catch (Exception e) {
		}
		return model;
	}

	public class ValueVersion {

		public String value;
		public String version;
	}
}
