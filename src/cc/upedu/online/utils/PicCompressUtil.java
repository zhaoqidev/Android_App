package cc.upedu.online.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import cc.upedu.online.OnlineApp;

/**
 * wa.android.common.utils.PicCompressUtil
 * 
 * @author guowla create at 2014-8-8 下午4:35:54 图片压缩的Util方法
 */
public class PicCompressUtil {
	
	/**
	 * PicCompressUtil.java [V1.00] classpath:wa.android.common.utils MethodName
	 * : getCompressBitmap Return : String guowla create at 2014-8-8下午4:36:07
	 * 图片压缩的工具方法 入参： String filePath,文件路径 String filename,文件名称 int width 图片宽度
	 */
	private static int picMaxSize = 204800;
	public static String getCompressBitmap(String filePath, int width) {
		String Bitmapcontent = "";
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		int type = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		File file = new File(filePath);

		if (file.getName().toLowerCase(Locale.US).contains(".png")) {
			type = 1;
			if (file.length() > picMaxSize) {
				options.inSampleSize = PicCompressUtil.calculateInSampleSize(
						options, width, type);
			} else {
				options.inSampleSize = 1;
			}

			options.inJustDecodeBounds = false;
			BitmapFactory.decodeFile(filePath, options).compress(
					Bitmap.CompressFormat.PNG, 100, baos);
		} else {
			type = 2;
			if (file.length() > picMaxSize) {
				options.inSampleSize = PicCompressUtil.calculateInSampleSize(
						options, width, type);
			} else {
				options.inSampleSize = 1;
			}
			options.inJustDecodeBounds = false;
			BitmapFactory.decodeFile(filePath, options).compress(Bitmap.CompressFormat.JPEG, 100, baos);
		}

		if (baos.size() > picMaxSize) {
			Bitmapcontent = bitmapRateHandler(filePath,
					BitmapFactory.decodeFile(filePath, options), type);
		} else {
			if (type == 1) {
				BitmapFactory.decodeFile(filePath, options).compress(
						Bitmap.CompressFormat.PNG, 100, baos);
			} else {
				BitmapFactory.decodeFile(filePath, options).compress(
						Bitmap.CompressFormat.JPEG, 100, baos);
			}
//			byte[] b = baos.toByteArray();
			try {
				String name = filePath.substring(filePath.lastIndexOf("/")+1);
				String dir = OnlineApp.myApp.context.getCacheDir().getAbsolutePath();
				File file2 =getFilePath(dir, name);
				
				FileOutputStream out = new FileOutputStream(file2);
//				baos.writeTo(out);
				out.write(baos.toByteArray());
				out.close();
				
				baos.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Bitmapcontent = OnlineApp.myApp.context.getCacheDir().getAbsolutePath()+filePath.substring(filePath.lastIndexOf("/")+1);
//			Bitmapcontent = new String(b);
//			Bitmapcontent = Base64.encodeToString(b, Base64.DEFAULT);
		}
		return Bitmapcontent;
	}

	public static void CompressBitmap2JPEG(String filePath, String filename,int quality,
			int minwidth) throws IOException {
		File file = new File(filePath);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), options);

		float scale;
		if (options.outHeight > options.outWidth) {
			int inSampleSize = options.outWidth / minwidth;
			options.inSampleSize = inSampleSize;
			scale = (float) minwidth / (options.outWidth / inSampleSize);
		} else {
			int inSampleSize = options.outHeight / minwidth;
			options.inSampleSize = inSampleSize;
			scale = (float) minwidth / (options.outHeight / inSampleSize);
		}
		options.inJustDecodeBounds = false;
		Bitmap end = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		Matrix m = new Matrix();
		m.setScale(scale, scale);

		Bitmap endFile = Bitmap.createBitmap(end, 0, 0, end.getWidth(),
				end.getHeight(), m, false);
		end.recycle();
		end = endFile;
		FileOutputStream fos = new FileOutputStream(file);
		end.compress(CompressFormat.JPEG, quality, fos);
		fos.close();
	}

	/**
	 * PicCompressUtil.java [V1.00] classpath:wa.android.common.utils MethodName
	 * : getCompressBitmap Return : String guowla create at 2014-8-8下午4:36:07
	 * 图片压缩的工具方法 入参： String filePath,文件路径 String filename,文件名称 int width 图片宽度
	 * 
	 * @throws Exception
	 */

	public static String CompressBitmap(String filePath, int width) throws Exception {
		String Bitmapcontent = "";
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		int type = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		File file = new File(filePath);
		
		if (file.getName().toLowerCase(Locale.US).contains(".png")) {
			type = 1;
			if (file.length() > picMaxSize) {
				options.inSampleSize = PicCompressUtil.calculateInSampleSize(
						options, width, type);
			} else {
				options.inSampleSize = 1;
			}

			options.inJustDecodeBounds = false;
			BitmapFactory.decodeFile(filePath, options).compress(
					Bitmap.CompressFormat.PNG, 100, baos);
		} else {
			type = 2;
			if (file.length() > picMaxSize) {
				options.inSampleSize = PicCompressUtil.calculateInSampleSize(
						options, width, type);
			} else {
				options.inSampleSize = 1;
			}
			options.inJustDecodeBounds = false;
			BitmapFactory.decodeFile(filePath, options).compress(
					Bitmap.CompressFormat.JPEG, 100, baos);
		}

		if (baos.size() > picMaxSize) {
			Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
			int rate = 90;
			if (type == 1) {
				bitmap.compress(Bitmap.CompressFormat.PNG, rate, baos);
			} else {
				bitmap.compress(Bitmap.CompressFormat.JPEG, rate, baos);
				if (baos.size() > picMaxSize)
					while (baos.size() > picMaxSize) {
						if (rate <= 20)
							break;
						rate = rate - 40;
						try {
							baos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						baos = rateCompress(bitmap, rate);
					}
			}
		} else {
			if (type == 1) {
				BitmapFactory.decodeFile(filePath, options).compress(
						Bitmap.CompressFormat.PNG, 100, baos);
			} else {
				BitmapFactory.decodeFile(filePath, options).compress(
						Bitmap.CompressFormat.JPEG, 100, baos);
			}
		}
		byte[] b = baos.toByteArray();
		createFile(filePath, b);
		return Bitmapcontent;
	}

	// 将byte数组写入文件
	public static void createFile(String path, byte[] content) {
		try {
			FileOutputStream fos = OnlineApp.myApp.context.openFileOutput(path, Context.MODE_PRIVATE);
			fos.write(content);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * PicCompressUtil.java [V1.00] classpath:wa.android.common.utils MethodName
	 * : bitmapRateHandler Return : String guowla create at 2014-8-8下午4:38:31
	 * 图片压缩方法
	 */
	@SuppressLint("SdCardPath")
	public static String bitmapRateHandler(String filePath,Bitmap bitmap, int type) {
		int rate = 90;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (type == 1) {
			bitmap.compress(Bitmap.CompressFormat.PNG, rate, baos);
		} else {
			bitmap.compress(Bitmap.CompressFormat.JPEG, rate, baos);
			if (baos.size() > picMaxSize)
				while (baos.size() > picMaxSize) {
					if (rate <= 20)
						break;
					rate = rate - 40;
					try {
						baos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					baos = rateCompress(bitmap, rate);
				}
		}
		try {
			String name = filePath.substring(filePath.lastIndexOf("/")+1);
			String dir = OnlineApp.myApp.context.getCacheDir().getAbsolutePath();
			File file =getFilePath(dir, name);
			
			FileOutputStream out = new FileOutputStream(file);
//			baos.writeTo(out);
			out.write(baos.toByteArray());
			out.close();
			
			baos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		byte[] b = baos.toByteArray();
//		String result = Base64.encodeToString(b, Base64.DEFAULT);
		return OnlineApp.myApp.context.getCacheDir().getAbsolutePath()+filePath.substring(filePath.lastIndexOf("/")+1);
	}
	public static File getFilePath(String filePath,  
            String fileName) {  
		File file = null;  
		makeRootDirectory(filePath);  
		try {  
			file = new File(filePath + fileName);  
		} catch (Exception e) {  
			// TODO Auto-generated catch block  
			e.printStackTrace();  
		}  
		return file;  
	}
		
	public static void makeRootDirectory(String filePath) {  
		File file = null;  
		try {  
		file = new File(filePath);  
		if (!file.exists()) {  
			file.mkdirs();
		}  
		} catch (Exception e) {  
		
		}  
	}  
	/**
	 * PicCompressUtil.java [V1.00] classpath:wa.android.common.utils MethodName
	 * : calculateInSampleSize Return : int guowla create at 2014-8-8下午4:38:59
	 * 图片尺寸修改方法
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int type) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		final int reqHeight = reqWidth / 2 * height / width;
		int inSampleSize = 1;
		if (type == 1) {
			if (height > reqHeight || width > reqWidth) {
				final int heightRatio = Math.round((float) height
						/ (float) (reqHeight / 2));
				final int widthRatio = Math.round((float) width
						/ (float) (reqWidth / 2));
				inSampleSize = heightRatio < widthRatio ? heightRatio
						: widthRatio;
			}
		} else {
			if (height > reqHeight || width > reqWidth) {
				final int heightRatio = Math.round((float) height
						/ (float) (reqHeight / 1));
				final int widthRatio = Math.round((float) width
						/ (float) (reqWidth / 1));
				inSampleSize = heightRatio < widthRatio ? heightRatio
						: widthRatio;
			}
		}
		return inSampleSize;
	}

	/**
	 * PicCompressUtil.java [V1.00] classpath:wa.android.common.utils MethodName
	 * : rateCompress Return : ByteArrayOutputStream guowla create at
	 * 2014-8-8下午4:39:13 像素比例压缩方法
	 */
	public static ByteArrayOutputStream rateCompress(Bitmap bitmap, int rate) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, rate, baos);
		return baos;
	}

	public int getPicMaxSize() {
		return picMaxSize;
	}

	public void setPicMaxSize(int picMaxSize) {
		this.picMaxSize = picMaxSize;
	}
}
