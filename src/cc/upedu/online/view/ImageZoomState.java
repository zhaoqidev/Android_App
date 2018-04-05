package cc.upedu.online.view;

import java.util.Observable;

/**
 * wa.android.task.view.zoomimageview.ImageZoomState
 * @author damilong
 * create at 2014-11-10 上午8:39:43
 */
public class ImageZoomState extends Observable {

    private float mZoom = 1.0f;// 控制图片缩放的变量，表示缩放倍数，值越大图像越大
    private float mPanX = 0.5f;// 控制图片水平方向移动的变量，值越大图片可视区域的左边界距离图片左边界越远，图像越靠左，值为0.5f时居中
    private float mPanY = 0.5f;// 控制图片水平方向移动的变量，值越大图片可视区域的上边界距离图片上边界越远，图像越靠上，值为0.5f时居中

 

    /**
     * ImageZoomState.java [V1.00]
     * classpath:wa.android.task.view.zoomimageview   
     * MethodName : getmZoom
     * Return : float
     * guowla create at 2014-11-11上午10:05:22
     * 获取缩放比例
     */
    public float getmZoom() {

       return mZoom;

    }

 

    /**
     * ImageZoomState.java [V1.00]
     * classpath:wa.android.task.view.zoomimageview   
     * MethodName : setmZoom
     * Return : void
     * guowla create at 2014-11-11上午10:05:27
     * 设置缩放比例
     */
    public void setmZoom(float mZoom) {

       if (this.mZoom != mZoom) {
           this.mZoom = mZoom < 1.0f ? 1.0f : mZoom;// 保证图片最小为原始状态
           if (this.mZoom == 1.0f) {// 返回初始大小时，使其位置也恢复原始位置
              this.mPanX = 0.5f;
              this.mPanY = 0.5f;
           }
           this.setChanged();
       }
    }

 

    /**
     * ImageZoomState.java [V1.00]
     * classpath:wa.android.task.view.zoomimageview   
     * MethodName : getmPanX
     * Return : float
     * guowla create at 2014-11-11上午10:09:23
     * 获取X方向的缩放比例值
     */
    public float getmPanX() {

       return mPanX;

    }

 

    /**
     * ImageZoomState.java [V1.00]
     * classpath:wa.android.task.view.zoomimageview   
     * MethodName : setmPanX
     * Return : void
     * guowla create at 2014-11-11上午10:15:53
     * 设置X方向的缩放比例值
     */
    public void setmPanX(float mPanX) {

       if (mZoom == 1.0f) {// 使图为原始大小时不能移动
           return;
       }
       if (this.mPanX != mPanX) {
           this.mPanX = mPanX;
           this.setChanged();

       }

    }

 

    /**
     * ImageZoomState.java [V1.00]
     * classpath:wa.android.task.view.zoomimageview   
     * MethodName : getmPanY
     * Return : float
     * guowla create at 2014-11-11上午10:16:10
     * 获取Y方向的缩放比例值
     */
    public float getmPanY() {

       return mPanY;

    }

 

    /**
     * ImageZoomState.java [V1.00]
     * classpath:wa.android.task.view.zoomimageview   
     * MethodName : setmPanY
     * Return : void
     * guowla create at 2014-11-11上午10:16:48
     * 设置Y方向的缩放比例值
     */
    public void setmPanY(float mPanY) {

       if (mZoom == 1.0f) {// 使图为原始大小时不能移动
           return;
       }
       if (this.mPanY != mPanY) {

           this.mPanY = mPanY;

           this.setChanged();

       }

    }

 

    /**
     * ImageZoomState.java [V1.00]
     * classpath:wa.android.task.view.zoomimageview   
     * MethodName : getZoomX
     * Return : float
     * guowla create at 2014-11-11上午10:17:05
     * 用于Ondraw 调用
     */
    public float getZoomX(float aspectQuotient) {

       return Math.min(mZoom, mZoom * aspectQuotient);

    }

 

    /**
     * ImageZoomState.java [V1.00]
     * classpath:wa.android.task.view.zoomimageview   
     * MethodName : getZoomY
     * Return : float
     * guowla create at 2014-11-11上午10:18:09
     */
    public float getZoomY(float aspectQuotient) {

       return Math.min(mZoom, mZoom / aspectQuotient);

    }

}



 
