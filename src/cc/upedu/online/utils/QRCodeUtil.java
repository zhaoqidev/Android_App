package cc.upedu.online.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Hashtable;
/**
 * 生成二维码图片的工具类
 * @author Administrator
 *
 */
public class QRCodeUtil {
	public static final int QR_WIDTH = 480;
	public static final int QR_HEIGHT = 480;
	// 生成QR图
	public String createImage(String text) {
        try {
            // 需要引入core包
            QRCodeWriter writer = new QRCodeWriter();

            if (StringUtil.isEmpty(text)) {
                return null;
            }

            // 把输入的文本转为二维码
            BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
                    QR_WIDTH, QR_HEIGHT);

            System.out.println("w:" + martix.getWidth() + "h:"
                    + martix.getHeight());

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }

                }
            }

            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            String filePath = getTrimLocalImagePath(text);
			saveBitmapToSDCard(bitmap, filePath);
            return filePath;

        } catch (WriterException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
			// TODO: handle exception
        	 e.printStackTrace();
		}
		return null;
    }
	/**
	 * @param bitmap
	 * @param filePath
	 */
	private static void saveBitmapToSDCard(Bitmap bitmap, String filePath) {
		FileOutputStream b = null;
		try {
			b = new FileOutputStream(filePath);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				b.flush();
				b.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * urlpath to localpath translate
	 * @param imagePath
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private  String getTrimLocalImagePath(String imagePath)
			throws UnsupportedEncodingException {
		
		String imageLocalPath = new File(initDir(), URLEncoder.encode(
				imagePath, "utf-8").replace("http://", "")).getAbsolutePath();
		return imageLocalPath;
	}
	
	/**
	 * 图片路径
	 * @return file
	 */
	private static File initDir() {
		File dir;
		dir = new File(Environment.getExternalStorageDirectory().getPath()+"/Upedu/picCahce");
		if(!dir.exists()){
			dir.mkdirs();
		}
		return dir;
	}
}
