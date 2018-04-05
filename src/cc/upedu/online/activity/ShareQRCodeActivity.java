package cc.upedu.online.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.CommonUtil;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.QRCodeUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.UserStateUtil;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 分享二维码界面
 * Created by Administrator on 2016/2/19 0019.
 */
public class ShareQRCodeActivity extends TitleBaseActivity{
    private ImageView headImg;
    private ImageView qrImg;
    private ImageView bgImg;//背景图片


    private String courseId;//课程ID
    private String qrCodePath;//二维码本地存储路径
    private OnekeyShare oks;

//  type=1;//分享课程二维码
//  type=2;//分享导师二维码
//  type=3;//分享用户名片二维码
    int type;


    @Override
    protected void initTitle() {
        setTitleText("分享二维码");
        setRightText("分享", new OnClickMyListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveToSD(myShot(), Environment.getExternalStorageDirectory().getPath() + "/Upedu/shot/", "share");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String shotPath = Environment.getExternalStorageDirectory().getPath() + "/Upedu/shot/" + "share";
//                oks = ShareUtil.getInstance().showShare(ShareUtil.STYE_COURSE_QR, null, null, shotPath, true, null, null);
//                oks.show(context);
                shareSingleImage(shotPath);


            }
        });

        type=this.getIntent().getIntExtra("type",0);
        courseId = getIntent().getStringExtra("courseId");

    }


    //调用系统功能，分享单张图片
    public void shareSingleImage(String imagePath) {
//        String imagePath = Environment.getExternalStorageDirectory() + File.separator + "test.jpg";
        //由文件得到uri
        Uri imageUri = Uri.fromFile(new File(imagePath));
        Log.d("share", "uri:" + imageUri);  //输出：file:///storage/emulated/0/test.jpg

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    @Override
    protected void initData() {

        if (type==1){
//            qrCodePath = new QRCodeUtil().createImage(ConstantsOnline.SHAR_COURSE + courseId + "?shareBy=" + SharedPreferencesUtil.getInstance().spGetString("secretUid"));
              qrCodePath = new QRCodeUtil().createImage(ConstantsOnline.SHAR_REGIEST + UserStateUtil.getUserId() + "/" +courseId );
        }else if (type==2){
            qrCodePath = new QRCodeUtil().createImage(ConstantsOnline.SHAR_REGIEST + UserStateUtil.getUserId() + "/" +"0");
        }else if (type==3){
            qrCodePath = new QRCodeUtil().createImage(ConstantsOnline.SHAR_REGIEST + UserStateUtil.getUserId() + "/" +"0");
        }else{

        }



        //显示二维码
        Bitmap bm = BitmapFactory.decodeFile(qrCodePath);
        qrImg.setImageBitmap(bm);

    }
    @Override
    protected View initContentView() {
        View view;
        int i = (int)(Math.random() * 100); //0-100以内的随机数，用Matn.random()方式



        if (type==1){
            view = View.inflate(context, R.layout.activity_shareqrcode2, null);
            headImg=(ImageView)view.findViewById(R.id.headimg);
            qrImg=(ImageView)view.findViewById(R.id.qrimg);
            bgImg=(ImageView)view.findViewById(R.id.bg_image);
            //显示课程图片
            ImageUtils.setImage(getIntent().getStringExtra("courseImg"), headImg, 0);
            if ("117".equals(courseId)) {
                bgImg.setBackgroundResource( R.drawable.share_shangyemoshi);
            }else{
                switch (i%2) {
                    case 0:
                        bgImg.setBackgroundResource( R.drawable.share_course1);
                        break;
                    case 1:
                        bgImg.setBackgroundResource( R.drawable.share_course2);
                        break;
                    default:
                        bgImg.setBackgroundResource( R.drawable.share_course1);
                        break;
                }

            }

        }else if (type==2){
            view = View.inflate(context, R.layout.activity_shareqrcode1, null);
            headImg=(ImageView)view.findViewById(R.id.headimg);
            qrImg=(ImageView)view.findViewById(R.id.qrimg);
            bgImg=(ImageView)view.findViewById(R.id.bg_image);
            //显示导师头像
            ImageUtils.setImage(getIntent().getStringExtra("teacherImage"), headImg, 0);
            switch (i%3){
                case 0:
                    bgImg.setBackgroundResource(R.drawable.share_teacher1);
                    break;
                case 1:
                    bgImg.setBackgroundResource( R.drawable.share_teacher2);
                    break;
                case 2:
                    bgImg.setBackgroundResource( R.drawable.share_teacher3);
                    break;
                default:
                    bgImg.setBackgroundResource( R.drawable.share_teacher1);
            }
//            bgImg.setBackgroundResource( R.drawable.share_teacher1));
        }else{
            view = View.inflate(context, R.layout.activity_shareqrcode1, null);
            headImg=(ImageView)view.findViewById(R.id.headimg);
            qrImg=(ImageView)view.findViewById(R.id.qrimg);
            bgImg=(ImageView)view.findViewById(R.id.bg_image);
            //显示分享者头像
            ImageUtils.setImage(SharedPreferencesUtil.getInstance().spGetString("avatar"), headImg, 0);

            switch (i%4) {
                case 0:
                    bgImg.setBackgroundResource( R.drawable.share_upguwen1);
                    break;
                case 1:
                    bgImg.setBackgroundResource( R.drawable.share_upguwen2);
                    break;
                case 2:
                    bgImg.setBackgroundResource( R.drawable.share_upguwen3);
                    break;
                case 3:
                    bgImg.setBackgroundResource( R.drawable.share_upguwen4);
                    break;
                default:
                    bgImg.setBackgroundResource( R.drawable.share_upguwen1);
                    break;
            }
//            switch (i%2) {
//                case 0:
//                    bgImg.setBackgroundResource( R.drawable.share_upguwen1));
//                    break;
//                case 1:
//                    bgImg.setBackgroundResource( R.drawable.share_upguwen3));
//                    break;
//                default:
//                    bgImg.setBackgroundResource( R.drawable.share_upguwen1));
//                    break;
//            }

        }


        return view;
    }

    /**
     * 获取当前activity的屏幕截图
     * @return  截图的bitmap
     */
    public Bitmap myShot() {
        // 获取windows中最顶层的view
        View view = this.getWindow().getDecorView();
        view.buildDrawingCache();

        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;//状态栏高度
        int titleBarHeights= CommonUtil.dip2px(context,45);//titleBar高度
        Display display = this.getWindowManager().getDefaultDisplay();

        // 获取屏幕宽和高
        int widths = display.getWidth();
        int heights = display.getHeight();

        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);

        // 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                statusBarHeights + titleBarHeights, widths, heights - statusBarHeights-titleBarHeights);

        // 销毁缓存信息
        view.destroyDrawingCache();

        return bmp;
    }

    /**
     * 将屏幕截图保存到sd卡中
     * @param bmp 图片
     * @param dirName 输出的文件夹名称
     * @param fileName 输出的文件名
     * @throws IOException
     */
    private void saveToSD(Bitmap bmp, String dirName,String fileName) throws IOException {
        // 判断sd卡是否存在
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(dirName);
            // 判断文件夹是否存在，不存在则创建
            if(!dir.exists()){
                dir.mkdir();
            }

            File file = new File(dirName + fileName);
            // 判断文件是否存在，不存在则创建
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                if (fos != null) {
                    // 第一参数是图片格式，第二个是图片质量，第三个是输出流
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    // 用完关闭
                    fos.flush();
                    fos.close();
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
