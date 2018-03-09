package com.lcshidai.lc.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.text.TextUtils;

/**
 * @Title: IconUtil.java
 * @Description: 文件缩略图工具类
 * @author: wangming oumei.vip@gmail.com
 * @date: May 23, 2011 2:07:10 PM
 */
public class Thumbnail {

	public static HashMap<String, SoftReference<Bitmap>> mBitmapCache = new HashMap<String, SoftReference<Bitmap>>();
	public static ReferenceQueue<Bitmap> mReferenceQueue = new ReferenceQueue<Bitmap>();
	public static String FOLDER = "folder";
	public static String EMPTY = "empty";
	public static String TEXT = "text";
	public static String AUDIO = "audio";
	public static String VIDEO = "video";
	public static String IMAGE = "image";
	private static Thumbnail mInstance = null;
	private static Object mLock = new Object();
	private static Context mContext;
	private MimeTypes mMimeTypes = null;

	private Thumbnail(Context context) {
		mContext = context;
		try {
			mMimeTypes = MimeTypes.fromXmlResource(context);
		} catch (Exception e) {
		}
	}

	public static Thumbnail init(Context context) {
		synchronized (mLock) {
			if (mInstance == null)
				mInstance = new Thumbnail(context);
			return mInstance;
		}
	}

	public Bitmap parse(String bank) {
		String key = bank;
		SoftReference<Bitmap> sf = mBitmapCache.get(key);
		if (sf == null || sf.get() == null) {
			sf = new SoftReference<Bitmap>(decode(bank), mReferenceQueue);
			mBitmapCache.put(key, sf);
		}
		return sf.get();
	}

	/**
	 * decode MIME type icon for a file named by fileName
	 * 
	 * @param bank
	 * @return a bitmap of a icon
	 */
	public Bitmap decode(String bank) {
		Bitmap bitmap = null;
		bitmap = decodeResource(bank);
		return bitmap;
	}

	public Bitmap decodeResource(String extension) {
		String ext = extension.toLowerCase();
		if (TextUtils.isEmpty(ext))
			extension = EMPTY;
		Resources rs = mContext.getResources();
		int id = rs.getIdentifier(ext, "drawable", mContext.getPackageName());
		Bitmap bitmap = BitmapFactory.decodeResource(rs, id);
		// if(bitmap == null) bitmap = decodeResource(EMPTY);
		return bitmap;
	}

	public Bitmap decodeByMime(String fileName) {
		String mime = getMimetype(fileName);
		String res = null;
		if (mime != null) {
			if (mime.startsWith(TEXT)) {
				res = TEXT;
			} else if (mime.startsWith(VIDEO)) {
				res = VIDEO;
			} else if (mime.startsWith(IMAGE)) {
				res = IMAGE;
			} else if (mime.startsWith(AUDIO)) {
				res = AUDIO;
			}else if (mime.startsWith(FOLDER)) {
				res = FOLDER;
			}
		}

		if (res == null)
			res = FileUtils.getExtension(fileName);
		return decodeResource(res);
	}
	
	public Bitmap getThumbnailPic(String url) {
		String key = url;
		Bitmap bitmap = null;
		SoftReference<Bitmap> sf = mBitmapCache.get(key);
		if (sf == null || sf.get() == null) {
			try {
				bitmap = BitmapFactory.decodeStream(new URL(url).openStream());
			}  catch (NullPointerException e) {
				e.printStackTrace();
				return null;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			sf = new SoftReference<Bitmap>(bitmap, mReferenceQueue);
			mBitmapCache.put(key, sf);
			if(bitmap!=null){
				bitmap.isRecycled();
				bitmap = null;
	    	}
		}
		return sf.get();
	}
	
	
	
	
	public Bitmap getThumbnailPicNotSave(String url) {
		//String urlre=url.replace("%", "%25").replace(" ", "%20").replace("}", "%7D").replace("]", "%5D");;
		Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeStream(new URL(url).openStream());
			}  catch (NullPointerException e) {
				e.printStackTrace();
				return null;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		return bitmap;
	}
	

	public Bitmap getThumbnailPic(File file, String type) {
		String key = file.getAbsolutePath();
		Bitmap bitmap = null, icon = null;
		SoftReference<Bitmap> sf = mBitmapCache.get(key);
		if (sf == null || sf.get() == null) {
			if (type.equals("mp4")) {
				bitmap = ThumbnailUtils.createVideoThumbnail(key, Video.Thumbnails.MINI_KIND);
//				long id = getDbId(key,true);
//				icon = Video.Thumbnails.getThumbnail(mContext.getContentResolver(), id, Video.Thumbnails.MICRO_KIND, null);
				icon = ThumbnailUtils.extractThumbnail(bitmap, 200, 150);
			} else {
				BitmapFactory.Options options = new BitmapFactory.Options();  
				options.inSampleSize = 4;
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeFile(key, options);
//				long id = getDbId(key,false);
//				icon = Images.Thumbnails.getThumbnail(mContext.getContentResolver(), id, Images.Thumbnails.MICRO_KIND, null);
				icon = ThumbnailUtils.extractThumbnail(bitmap, 200, 150, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			}
			if(bitmap!=null){
				bitmap.isRecycled();
				bitmap=null;
	    	}
			sf = new SoftReference<Bitmap>(icon, mReferenceQueue);
			mBitmapCache.put(key, sf);
		}
		return sf.get();
	}
	
	

	private String getMimetype(String fileName) {
		String mime = null;
		if (mMimeTypes != null) {
			mime = mMimeTypes.getMimeType(fileName);
		}
		return mime;
	}
	
	public long getDbId(String path, boolean isVideo) {
		  String volumeName = "external";
		  Uri uri = isVideo ? Video.Media.getContentUri(volumeName) : Images.Media.getContentUri(volumeName);
		  String selection = FileColumns.DATA + "=?";
		  String[] selectionArgs = new String[] {path};
		  String[] columns = new String[] {FileColumns._ID, FileColumns.DATA};
		  Cursor c = mContext.getContentResolver().query(uri, columns, selection, selectionArgs, null);
		  if (c == null) {
		      return 0;
		  }
		  long id = 0;
		  if (c.moveToNext()) {
		      id = c.getLong(0);
		  }
		  c.close();
		  return id;
    }
	
	public Bitmap readBitMap(Context context, int resId){    
	    BitmapFactory.Options opt = new BitmapFactory.Options();    
	    opt.inPreferredConfig = Bitmap.Config.RGB_565;     
	    opt.inPurgeable = true;
	    opt.inInputShareable = true;    
	    //获取资源图片    
	    InputStream is = context.getResources().openRawResource(resId);    
	    return BitmapFactory.decodeStream(is,null,opt);    
	 }  

	
}
