package sudoku.myself.xhc.com.sudoku.debugutil.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/**

 */
public class FileUtil {

	/*
	 * public static List<String>getEmoJiList(Context context){ String[]
	 * stringArray =
	 * context.getResources().getStringArray(R.array.default_simily); return
	 * Arrays.asList(stringArray); }
	 */
	public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	public static final String PATH = Environment.getExternalStorageDirectory().toString();
	public static final String BASE_FILE_PATH = BASE_PATH + "/ipear";
	public static final String IMAGE_FILE_PATH = BASE_FILE_PATH + "/images";
	public static final String POSE_HISTORY = "mnt/sdcard/" + File.separator + "ipear" + File.separator + "posehistory";

	public static boolean isSdcardMounted() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return true;
		}
		return false;
	}

	public static String getHistoryPath() throws IOException {
		if (!isSdcardMounted()) {
			return null;
		}
		File sdCardDir = Environment.getExternalStorageDirectory();
		String path = sdCardDir.getAbsolutePath() + File.separator + "ipear";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
		File file2 = new File(POSE_HISTORY);
		if (!file2.exists()) {
			file2.createNewFile();
		}
		return POSE_HISTORY;
	}

	public static void saveBitmapToFile(Bitmap bitmap, String imageName) {
		if (TextUtils.isEmpty(imageName)) {
			return;
		}
		FileOutputStream fos = null;
		try {
			File dir = new File(FileUtil.IMAGE_FILE_PATH);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File f = new File(imageName);
			f.createNewFile();
			fos = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void writeFileSdcardFile(String fileName, String write_str, boolean append) throws IOException {
		try {

			Log.e("xhc", "查看path+" + fileName + " 内容  " + write_str);
			FileOutputStream fout = new FileOutputStream(fileName, append);
			byte[] bytes = write_str.getBytes();

			fout.write(bytes);
			fout.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

//	// 读SD中的文件
//	public static String readFileSdcardFile(String fileName) throws IOException {
//		String res = "";
//		try {
//			FileInputStream fin = new FileInputStream(fileName);
//
//			int length = fin.available();
//
//			byte[] buffer = new byte[length];
//			fin.read(buffer);
//
//			res = EncodingUtils.getString(buffer, "UTF-8");
//
//			fin.close();
//		}

//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		return res;
//	}

	/**
	 * Delete corresponding path, file or directory.
	 *
	 * @param file
	 *            path to delete.
	 */
	public static void delete(File file) {
		delete(file, false);
	}

	/**
	 * Delete corresponding path, file or directory.
	 *
	 * @param file
	 *            path to delete.
	 * @param ignoreDir
	 *            whether ignore directory. If true, all files will be deleted
	 *            while directories is reserved.
	 */
	public static void delete(File file, boolean ignoreDir) {
		if (file == null || !file.exists()) {
			return;
		}
		if (file.isFile()) {
			file.delete();
			return;
		}

		File[] fileList = file.listFiles();
		if (fileList == null) {
			return;
		}

		for (File f : fileList) {
			delete(f, ignoreDir);
		}
		// delete the folder if need.
		if (!ignoreDir)
			file.delete();
	}

	public static String[] getListFileFromAssets(Context ctx, String path) {
		String str[] = null;
		try {
			str = ctx.getAssets().list(path);
            if(str != null && str.length > 0){
            	return str;
            }
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	// public void deepFile(Context ctxDealFile, String path) {
	// try {
	// String str[] = ctxDealFile.getAssets().list(path);
	// if (str.length > 0) {//如果是目录
	// File file = new File("/data/" + path);
	// file.mkdirs();
	// for (String string : str) {
	// path = path + "/" + string;
	// // textView.setText(textView.getText()+"\t"+path+"\t");
	// deepFile(ctxDealFile, path);
	// path = path.substring(0, path.lastIndexOf('/'));
	// }
	// } else {//如果是文件
	// InputStream is = ctxDealFile.getAssets().open(path);
	// FileOutputStream fos = new FileOutputStream(new File("/data/"
	// + path));
	// byte[] buffer = new byte[1024];
	// int count = 0;
	// while (true) {
	// count++;
	// int len = is.read(buffer);
	// if (len == -1) {
	// break;
	// }
	// fos.write(buffer, 0, len);
	// }
	// is.close();
	// fos.close();
	// }
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
}
