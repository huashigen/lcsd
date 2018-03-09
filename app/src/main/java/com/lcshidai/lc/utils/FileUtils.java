package com.lcshidai.lc.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class FileUtils {
    public static final String FILE_TYPE_IMG = "jpg,gif,png,jpeg";
    public static final String FILE_TYPE_FILE = "bmp,zip,rar,doc,xls,ppt,docx,xlsx,pptx,pdf,txt,html,htm,wps,et,dps";
    public static final String FILE_TYPE_SOUND = "mp3,wma,flac,aac,mmf,amr,m4a,m4r,ogg,mp2,wav,wv";
    public static final String FILE_TYPE_MEDIA = "ra,mp4,rm,rmvb,wmv,mov";

    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot + 1).toLowerCase();
        } else {
            return "";
        }
    }

    public static String getAttacheType(String filename) {
        String ex = getExtension(filename);
        if (FILE_TYPE_FILE.contains(ex))
            return "file";
        if (FILE_TYPE_IMG.contains(ex))
            return "img";
        if (FILE_TYPE_SOUND.contains(ex))
            return "sound";
        if (FILE_TYPE_MEDIA.contains(ex))
            return "media";
        return null;
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size
                / Math.pow(1024, digitGroups))
                + " " + units[digitGroups];
    }

    public static String getFileType(String fileName) {
        String type = "*/*";
        if (null != fileName && fileName.contains(".")) {
            int lastIndexOf = fileName.lastIndexOf(".");
            String end = fileName.substring(lastIndexOf + 1);
            if ("jpg".equalsIgnoreCase(end) || "png".equalsIgnoreCase(end)
                    || "gif".equalsIgnoreCase(end)
                    || "bmp".equalsIgnoreCase(end)) {
                type = "image/*";
            } else if ("mp3".equalsIgnoreCase(end)) {
                type = "audio/*";
            } else if ("mp4".equalsIgnoreCase(end)
                    || "3gp".equalsIgnoreCase(end)) {
                type = "video/*";
            } else if ("pdf".equalsIgnoreCase(end)
                    || "docx".equalsIgnoreCase(end)
                    || "doc".equalsIgnoreCase(end)
                    || "xls".equalsIgnoreCase(end)
                    || "xlsx".equalsIgnoreCase(end)
                    || "ppt".equalsIgnoreCase(end)
                    || "pptx".equalsIgnoreCase(end)) {
                if (end.equals("xlsx") || end.equals("xls")) {
                    type = "application/vnd.ms-excel";
                } else if (end.equals("docx") || end.equals("doc")) {
                    type = "application/msword";
                } else if (end.equals("ppt") || end.equals("pptx")) {
                    type = "application/vnd.ms-powerpoint";
                } else {
                    type = "application/" + end;
                }
            }
        }
        return type;
    }

    public static boolean deleteFolder(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (f.isDirectory()) {
                    if (!deleteFolder(f))
                        return false;
                } else {
                    Log.d("----正在删除 %s", f.getAbsolutePath());
                    if (!f.delete())
                        return false;
                }
            }
        }
        return file.delete();
    }

    public static void openFile(Activity context, File file) {
        Uri data = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type = null;
        try {
            type = MimeTypes.fromXmlResource(context).getMimeType(
                    file.getName());
        } catch (XmlPullParserException e) {
        } catch (IOException e) {
        }
        if (type == null)
            type = "*/*";
        intent.setDataAndType(data, type);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "不支持的文件格式", Toast.LENGTH_SHORT).show();
        }
    }


    public static void writeFileWithBugLog(Context context, String message) {
        String filename = "trj-error-" + TimeUtil.getCurTime() + ".txt";
        try {
            writeFileInCacheStorage(context, filename, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use getFilesDir()
     *
     * @param context
     * @param fileName
     * @param byteArr
     * @throws IOException
     */
    public static void writeFileInCacheStorage(Context context,
                                               String fileName, byte[] byteArr) throws IOException {
        writeFile(context, Environment.getExternalStorageDirectory(), fileName, byteArr);
    }


    private static void writeFile(Context context, File fileDir,
                                  String fileName, byte[] byteArr) throws IOException {
        File file = new File(fileDir, fileName);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(byteArr);
        outputStream.close();
    }

}
