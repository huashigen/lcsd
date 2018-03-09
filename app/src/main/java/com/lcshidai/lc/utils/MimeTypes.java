package com.lcshidai.lc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.lcshidai.lc.R;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.webkit.MimeTypeMap;

public class MimeTypes {
	public static final String TAG_MIMETYPES = "MimeTypes";
	public static final String TAG_TYPE = "type";
	public static final String ATTR_EXTENSION = "extension";
	public static final String ATTR_MIMETYPE = "mimetype";

	private static Object mLock = new Object();
	private static XmlPullParser mXpp;
	private static MimeTypes mMimeTypes;
	private Map<String, String> mMimeTypeMap = new HashMap<String, String>();

	private MimeTypes() {
		Locale.setDefault(Locale.CHINESE);
	}

	public void put(String type, String extension) {
		// Convert extensions to lower case letters for easier comparison
		extension = extension.toLowerCase();
		mMimeTypeMap.put(type, extension);
	}

	public String getMimeType(String filename) {

		String extension =  FileUtils.getExtension(filename) ;
		String mimetype = null;
		// Let's check the official map first. Webkit has a nice
		// extension-to-MIME map.
		if (extension.length() > 0) {
			String webkitMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

			if (webkitMimeType != null) {
				// Found one. Let's take it!
				mimetype = webkitMimeType;
			} else {
				// Convert extensions to lower case letters for easier
				// comparison
				extension = extension.toLowerCase();
				mimetype = mMimeTypeMap.get(extension);
			}
		}
		return mimetype;
	}

	/**
	 * get MIME types from an input stream
	 * 
	 * @param in
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static MimeTypes fromXml(InputStream in) throws XmlPullParserException, IOException {
		synchronized (mLock) {
			if (mMimeTypes != null) return mMimeTypes;
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			mXpp = factory.newPullParser();
			mXpp.setInput(new InputStreamReader(in));
			return parse();
		}
	}

	/**
	 * get MIME types from a XmlResourceParser
	 * 
	 * @param in
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static MimeTypes fromXmlResource(XmlResourceParser in) throws XmlPullParserException, IOException {
		synchronized (mLock) {
			if (mMimeTypes != null) return mMimeTypes;
			mXpp = in;
			return parse();
		}
	}
	
	public static MimeTypes fromXmlResource(Context context) throws XmlPullParserException, IOException {
		return fromXmlResource(context.getResources().getXml(R.xml.mimetypes));
	}
	
	/**
	 * parse the XML from a XmlPullParser
	 * 
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private static MimeTypes parse() throws XmlPullParserException, IOException {
		mMimeTypes = new MimeTypes();
		int eventType = mXpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			String tag = mXpp.getName();
			if (eventType == XmlPullParser.START_TAG) {
				if (tag.equals(TAG_MIMETYPES)) {

				} else if (tag.equals(TAG_TYPE)) {
					addMimeTypeStart();
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (tag.equals(TAG_MIMETYPES)) {
				}
			}
			eventType = mXpp.next();
		}
		return mMimeTypes;
	}

	private static void addMimeTypeStart() {
		String extension = mXpp.getAttributeValue(null, ATTR_EXTENSION);
		String mimetype = mXpp.getAttributeValue(null, ATTR_MIMETYPE);

		mMimeTypes.put(extension, mimetype);
	}

}
