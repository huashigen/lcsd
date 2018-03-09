package com.lcshidai.lc.widget.ppwindow;

import java.io.File;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.View;
import android.view.View.OnClickListener;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.ResultImpl;
import com.lcshidai.lc.impl.account.AccountSaveimgImpl;
import com.lcshidai.lc.impl.account.AccountSavepathImpl;
import com.lcshidai.lc.model.account.AccountSaveimgJson;
import com.lcshidai.lc.model.account.AccountSavepathJson;
import com.lcshidai.lc.service.account.HttpAccountSaveimgService;
import com.lcshidai.lc.service.account.HttpAccountSavepathService;
import com.lcshidai.lc.ui.account.ChangeUsernameActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.ScreenShot;

/**
 * 图片上传弹出框
 * 
 * @author 000814
 * 
 */
public class CameraOptionPopupWindow extends SelectPopupWindow implements AccountSaveimgImpl,AccountSavepathImpl{
	HttpAccountSaveimgService hassi;
	HttpAccountSavepathService hassp;
	private static final int FROM_CAMERA = 101; // 相机
	private static final int FROM_SDCARD = 102; // 相册
	private static final int PHOTO_MAX_SIZE = 1 * 1024 * 1024;
	public String mCurrentPartId = "";
	public ResultImpl resultImpl;

	Dialog loadDialog;
	private File fileImg;

	public CameraOptionPopupWindow(TRJActivity context) {
		super(context);
		btn_pick_photo.setOnClickListener(itemsOnClick);
		btn_take_photo.setOnClickListener(itemsOnClick);
		btn_change_uname.setOnClickListener(itemsOnClick);
		loadDialog = TRJActivity.createLoadingDialog(context, "发送中...", true);
	}

	public CameraOptionPopupWindow(TRJActivity context, ResultImpl resultImpl) {
		super(context);
		// 设置按钮监听
		btn_pick_photo.setOnClickListener(itemsOnClick);
		btn_take_photo.setOnClickListener(itemsOnClick);
		btn_change_uname.setOnClickListener(itemsOnClick);
		loadDialog = TRJActivity.createLoadingDialog(context, "发送中...", true);
		this.resultImpl = resultImpl;
		hassi = new HttpAccountSaveimgService(context, this);
		hassp = new HttpAccountSavepathService(context, this); 
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View v) {
			CameraOptionPopupWindow.this.dismiss();
			createSDCardDir();
			mCurrentPartId = "head";
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.btn_take_photo:
				intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				// 下面这句指定调用相机拍照后的照片存储的路径
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getFile("head")));
				mContext.startActivityForResult(intent, FROM_CAMERA);
				break;
			case R.id.btn_pick_photo:
				intent.setAction(Intent.ACTION_PICK);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				mContext.startActivityForResult(intent, FROM_SDCARD);
				break;
			case R.id.btn_change_uname:
				intent.setClass(mContext, ChangeUsernameActivity.class);
				mContext.startActivity(intent);
				break;
			default:
				break;
			}
		}
	};
	String pathFile;

	private File getFile(String id) {
		pathFile = Environment.getExternalStorageDirectory() + "/trj/TRJ" + id + ".jpg";
		return new File(Environment.getExternalStorageDirectory() + "/trj/TRJ" + id + ".jpg");
	}

	public String reback(int requestCode, int resultCode, Intent data,
			boolean isShow) {
		File tmp = new File("");
		String file_path = "";
		switch (requestCode) {
		case FROM_CAMERA:
			tmp = getFile("head");
			file_path = pathFile;
			break;
		case FROM_SDCARD:
			file_path = getFilePathFromContentUri(data.getData());
			tmp = new File(file_path);
			break;
		}
		if (requestCode < 100) {
			return file_path;
		}
		fileImg = tmp;

		if (isShow) {
			return file_path;
		} else {
			upLoad(tmp);

		}

		return file_path;
	}

	public void upLoad(final File tmp) {
		if (tmp != null && tmp.exists()) {
			if (loadDialog != null)
				loadDialog.show();
			if (tmp.length() > PHOTO_MAX_SIZE) {
				new AsyncTask<File, Void, File>() {
					@Override
					protected File doInBackground(File... params) {
						File tmp = params[0];
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 4;
						Bitmap b = BitmapFactory.decodeFile(tmp.getAbsolutePath(), options);
						File t = new File(tmp.getParent(), "new_" + tmp.getName());
						ScreenShot.savePicture(b, t);
						b.recycle();
						return t;
					}

					protected void onPostExecute(File result) {
						if (result != null && result.exists()) {
							upLoadImg(result);
						} else {
							upLoadImg(tmp);
						}

					}
				}.execute(tmp);
			} else {
				upLoadImg(tmp);
			}
		} else {
			mContext.showToast("上传文件失败");
		}
	}

	private void upLoadImg(File result) {
		hassi.gainAccountSaveimg(result);
	}

	public void createSDCardDir() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			// 创建一个文件夹对象，赋值为外部存储器的目录
			File sdcardDir = Environment.getExternalStorageDirectory();
			// 得到一个路径，内容是sdcard的文件夹路径和名字
			String path = sdcardDir.getPath() + "/trj";
			File file_path = new File(path);
			if (!file_path.exists()) {
				// 若不存在，创建目录，可以在应用启动的时候创建
				file_path.mkdirs();
			}
		}
	}

	private void onSendImgPath(String path) {
		hassp.gainAccountSavepath(path);
	}

	private String getFilePathFromContentUri(Uri selectedVideoUri) {
		String filePath;
		String[] filePathColumn = { MediaColumns.DATA };
		Cursor cursor = mContext.getContentResolver().query(selectedVideoUri,
				filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		filePath = cursor.getString(columnIndex);
		cursor.close();
		return filePath;
	}

	public File getFileImg() {
		return fileImg;
	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
	}

	@Override
	public void gainAccountSavepathsuccess(AccountSavepathJson response) {
		int result = response.getBoolen();
		String message = response.getMessage();
		if (result == 1) {
			mContext.showToast("上传成功");
			// loadData();
			resultImpl.goBackSuccess();
		} else {
			mContext.showToast(message);
		}
		if (loadDialog != null && loadDialog.isShowing())
			loadDialog.dismiss();
	}

	@Override
	public void gainAccountSavepathfail() {
		if (loadDialog != null && loadDialog.isShowing())
			loadDialog.dismiss();
	}

	@Override
	public void gainAccountSaveimgsuccess(AccountSaveimgJson response) {
		int result = response.getBoolen();
		String str = response.getResult();
		if (result == 1) {
			onSendImgPath(str);
		} else {
			mContext.showToast(response.getMessage());
			if (loadDialog != null && loadDialog.isShowing())
				loadDialog.dismiss();
		}
	}

	@Override
	public void gainAccountSaveimgfail() {
		if (loadDialog != null && loadDialog.isShowing())
			loadDialog.dismiss();
	}
	
}
