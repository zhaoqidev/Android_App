package cc.upedu.online.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

/**
 * wa.android.task.view.zoomimageview.SimpleImageZoomListener
 * @author damilong
 * create at 2014-11-11 上午11:08:30
 * ZoomView 缩放动作处理类
 */
public class SimpleImageZoomListener implements View.OnTouchListener {

	private ImageZoomState mState;// 图片缩放和移动状态
	private Context context;
// 	private final float SENSIBILITY = 0.8f;
	/**
	 * 
	 * 变化的起始点坐标
	 */
	private float sX;
	private float sY;

	/**
	 * 
	 * 不变的起始点坐标，用于判断手指是否进行了移动，从而在UP事件中判断是否为点击事件
	 */
	private float sX01;
	private float sY01;

	/**
	 * 
	 * 两触摸点间的最初距离
	 */
	private float sDistance;
	private View parentView;
	public View getParentView() {
		return parentView;
	}
	public void setParentView(View parentView) {
		this.parentView = parentView;
	}
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		int pointNum = event.getPointerCount();// 获取触摸点数
		if (pointNum == 1) {// 单点触摸,用来实现图像的移动和相应点击事件
			float mX = event.getX();// 记录不断移动的触摸点x坐标
			float mY = event.getY();// 记录不断移动的触摸点y坐标
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				// 记录起始点坐标
				sX01 = mX;
				sY01 = mY;
				sX = mX;
				sY = mY;
				return false;

			case MotionEvent.ACTION_MOVE:
				handleParent(true);
				float dX = (mX - sX) / v.getWidth();
				float dY = (mY - sY) / v.getHeight();
				// if (validation(dX, dY, v, mState.getmZoom()))
				// 拖拽范围控制
				mState.setmPanX(mState.getmPanX() - dX);
				mState.setmPanY(mState.getmPanY() - dY);
				mState.notifyObservers();
				// 更新起始点坐标
				sX = mX;
				sY = mY;
				break;
			case MotionEvent.ACTION_UP:
				if (event.getX() == sX01 && event.getY() == sY01) {
					return false;// return false 执行点击事件
				}
				break;
			case MotionEvent.ACTION_CANCEL:
				handleParent(false);
				break;
			}
		}

		if (pointNum == 2) {// 多点触摸，用来实现图像的缩放
			// 记录不断移动的一个触摸点坐标
			float mX0 = event.getX(event.getPointerId(0));
			float mY0 = event.getY(event.getPointerId(0));
			// 记录不断移动的令一个触摸点坐标
			float mX1 = event.getX(event.getPointerId(1));
			float mY1 = event.getY(event.getPointerId(1));
			float distance = this.getDistance(mX0, mY0, mX1, mY1);
			switch (action) {
			case MotionEvent.ACTION_POINTER_2_DOWN:
			case MotionEvent.ACTION_POINTER_1_DOWN:
				sDistance = distance;
				break;
			case MotionEvent.ACTION_POINTER_1_UP:
				// 松开第一个触摸点后的手指滑动就变成了以第二个触摸点为起始点的移动，以第二个触摸点坐标值为起始点坐标赋值
				sX = mX1;
				sY = mY1;
				break;
			case MotionEvent.ACTION_POINTER_2_UP:
				// 松开第二个触摸点后的手指滑动就变成了以第二个触摸点为起始点的移动，以第一个触摸点坐标值为起始点坐标赋值
				sX = mX0;
				sY = mY0;
				break;
			case MotionEvent.ACTION_MOVE:
				handleParent(true);
				mState.setmZoom(mState.getmZoom() * distance / sDistance);
				mState.notifyObservers();
				sDistance = distance;
				break;
			case MotionEvent.ACTION_CANCEL:
				handleParent(false);
				break;

			}
		}
		return true;// 返回true，返回false的话对点击事件的逻辑处理有影响

	}

	/**
	 * SimpleImageZoomListener.java [V1.00]
	 * classpath:wa.android.task.view.zoomimageview   
	 * MethodName : handleParent
	 * Return : void
	 * guowla create at 2014-11-11上午11:13:07
	 * 处理手势冲突类，目前仅写了父ViewGroup 为ViewPager的情况。
	 * 其他情况需要可以并行补充
	 */
	private void handleParent(boolean flag) {
		// TODO Auto-generated method stub
		if(null!= parentView&&mState.getmZoom() != 1.0f){
			if (parentView instanceof ViewPager) {
				((ViewPager) parentView).requestDisallowInterceptTouchEvent(flag);
			}
		}
	}
	//边框边界拖拽校验类，逻辑未写待补充
	private boolean validation(float dX, float dY, View v, float getmZoom) {
		// TODO Auto-generated method stub
		// ().getWindowManager().getDefaultDisplay().getWidth()
		if (dX > 0 && ((ImageZoomView) v).getLeft() == 0) {
			return true;
		}
		// if(dX>0&&v.getLeft() == 0){
		// return;
		// }
		if (dY > 0 && ((ImageZoomView) v).getmRectSrc().top == 0) {
			return false;
		}
		// if(dX>0&&v.getLeft() == 0){
		// return;
		// }
		return true;

	}


	/**
	 * SimpleImageZoomListener.java [V1.00]
	 * classpath:wa.android.task.view.zoomimageview   
	 * MethodName : getDistance
	 * Return : float
	 * guowla create at 2014-11-11上午11:11:58
	 * 返回（ mX0, mY0）与（（ mX1, mY1）两点间的距离
	 */
	private float getDistance(float mX0, float mY0, float mX1, float mY1) {
		double dX2 = Math.pow(mX0 - mX1, 2);// 两点横坐标差的平法
		double dY2 = Math.pow(mY0 - mY1, 2);// 两点纵坐标差的平法
		return (float) Math.pow(dX2 + dY2, 0.5);
	}

	/**
	 * SimpleImageZoomListener.java [V1.00]
	 * classpath:wa.android.task.view.zoomimageview   
	 * MethodName : setZoomState
	 * Return : void
	 * guowla create at 2014-11-11上午11:12:33
	 * 设置缩放状态对象方法
	 */
	public void setZoomState(ImageZoomState state) {

		mState = state;

	}

}