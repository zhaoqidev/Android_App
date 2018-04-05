package cc.upedu.online.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.Observable;
import java.util.Observer;

//	实现方式
//
//·共包括三个类：
//
// （1）ImageZoomView.java : 该类继承了View类，相当于观察者，随状态改变更新图片显示。
//
// 
//
// （2）ImageZoomState.java : 相当于被观察者，记录图片缩放和移动等状态。
//
// 
//
// （3）SimpleImageZoomListener.java: 这个类继承了View的OnTouchListener接口，
//相当于一个协调者，工作方式是：监听ImageZoomView上的手势动作------->根据手势改变ImageZoomState的值并调用notifyObservers()方法通知ImageZoomView（观察者）执行update()方法
//------->update()方法调用ImageZoomView的 invalidate()方法，
//该方法会自动调用onDraw()方法重绘图像更新图片显示。

public class ImageZoomView extends View implements Observer {

	/**
	 * 画布
	 */
	private Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);

	/**
	 * bitmap的Rect
	 */
	private Rect mRectSrc = new Rect();

	/**
	 * View 的Rect
	 */
	private Rect mRectDst = new Rect();

	/**
	 * 
	 */
	private float mAspectQuotient;

	/**
	 * 
	 */
	private Bitmap mBitmap;

	private ImageZoomState mZoomState;

	/**
	 * @param context
	 * @param attrs
	 * 构造方法
	 */
	public ImageZoomView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void update(Observable observable, Object data) {
		this.invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {

		if (mBitmap != null && mZoomState != null) {
			int viewWidth = this.getWidth();
			int viewHeight = this.getHeight();
			int bitmapWidth = mBitmap.getWidth();
			int bitmapHeight = mBitmap.getHeight();
			float panX = mZoomState.getmPanX();
			float panY = mZoomState.getmPanY();
			// 相当于viewHeight/bitmapHeight*mZoom
			float zoomX = mZoomState.getZoomX(mAspectQuotient) * viewWidth
			/ bitmapWidth;
			// 相当于viewWidth/bitmapWidth*mZoom
			float zoomY = mZoomState.getZoomY(mAspectQuotient) * viewHeight
			/ bitmapHeight;

			// 这里假定图片的高和宽都大于显示区域的高和宽，如果不是在下面做调整
			mRectSrc.left = (int) (panX * bitmapWidth - viewWidth / (zoomX * 2));
			mRectSrc.top = (int) (panY * bitmapHeight - viewHeight
					/ (zoomY * 2));
			mRectSrc.right = (int) (mRectSrc.left + viewWidth / zoomX);
			mRectSrc.bottom = (int) (mRectSrc.top + viewHeight / zoomY);
			mRectDst.left = this.getLeft();
			mRectDst.top = this.getTop();
			mRectDst.right = this.getRight();
			mRectDst.bottom = this.getBottom();
			// Adjust source rectangle so that it fits within the source image.
			// 如果图片宽或高小于显示区域宽或高（组件大小）或者由于移动或缩放引起的下面条件成立则调整矩形区域边界
			if (mRectSrc.left < 0) {
				mRectDst.left += -mRectSrc.left * zoomX;
				mRectSrc.left = 0;
			}
			if (mRectSrc.right > bitmapWidth) {
				mRectDst.right -= (mRectSrc.right - bitmapWidth) * zoomX;
				mRectSrc.right = bitmapWidth;
			}

			if (mRectSrc.top < 0) {
				mRectDst.top += -mRectSrc.top * zoomY;
				mRectSrc.top = 0;
			}
			if (mRectSrc.bottom > bitmapHeight) {
				mRectDst.bottom -= (mRectSrc.bottom - bitmapHeight) * zoomY;
				mRectSrc.bottom = bitmapHeight;
			}

			// 把bitmap的一部分(就是src所包括的部分)绘制到显示区中dst指定的矩形处.关键就是dst,它确定了bitmap要画的大小跟位置

			// 注：两个矩形中的坐标位置是相对于各自本身的而不是相对于屏幕的。

			canvas.drawBitmap(mBitmap, mRectSrc, mRectDst, mPaint);

		}

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
	int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		this.calculateAspectQuotient();
	}

	/**
	 * ImageZoomView.java [V1.00]
	 * classpath:wa.android.task.view.zoomimageview   
	 * MethodName : setImageZoomState
	 * Return : void
	 * guowla create at 2014-11-11上午10:42:21
	 * 设置View的缩放状态对象，添加观察对象监听
	 */
	public void setImageZoomState(ImageZoomState zoomState) {
		if (mZoomState != null) {
			mZoomState.deleteObserver(this);
		}
		mZoomState = zoomState;
		mZoomState.addObserver(this);
		invalidate();
	}

	public void setImage(Bitmap bitmap) {
		mBitmap = bitmap;
		this.calculateAspectQuotient();
		//该方法调用重绘界面逻辑，回调Ondraw()
		invalidate();
	}

	private void calculateAspectQuotient() {
		if (mBitmap != null) {
			mAspectQuotient = (float) (((float) mBitmap.getWidth() / mBitmap
			.getHeight()) / ((float) this.getWidth() / this.getHeight()));
		}

	}
	public Rect getmRectSrc() {
		return mRectSrc;
	}

	public void setmRectSrc(Rect mRectSrc) {
		this.mRectSrc = mRectSrc;
	}
}
