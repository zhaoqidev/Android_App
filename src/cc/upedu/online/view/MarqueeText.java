package cc.upedu.online.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import cc.upedu.online.utils.ScreenUtil;

/**
 * @author Jerome
 */
public class MarqueeText extends TextView implements Runnable {
	private int currentScrollX;// 当前滚动的位置
	private boolean isStop = false;
	private int textWidth;
	private boolean isMeasure = false;

	public MarqueeText(Context context) {
		super(context);
		this.setMinWidth(ScreenUtil.getInstance(context).getScreenWidth());
	}

	public MarqueeText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setMinWidth(ScreenUtil.getInstance(context).getScreenWidth());
	}

	public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setMinWidth(ScreenUtil.getInstance(context).getScreenWidth());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!isMeasure) {
			getTextWidth();
			isMeasure = true;
		}
	}

	public int getTextWidth() {
		Paint paint = this.getPaint();
		String str = this.getText().toString();
		textWidth = (int) paint.measureText(str);
		return textWidth;
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		super.setText(text, type);
		this.isMeasure = false;
	}

	@Override
	public void run() {
		currentScrollX += 2;// 滚动速度
		scrollTo(currentScrollX, 0);
		if (isStop) {
			return;
		}
		if (getScrollX() >= textWidth) {
			scrollTo(-this.getWidth(), 0);
			currentScrollX = -this.getWidth();
		}
		postDelayed(this, 10);
	}
	public boolean isScrolling(){
		return !isStop;
	}
	
	public void startScroll() {
		isStop = false;
		this.removeCallbacks(this);
		post(this);
	}

	public void stopScroll() {
		currentScrollX = 0;
		isStop = true;
	}
	
	public void pauseScroll() {
		isStop = true;
	}
	
	public void startFor0() {
		currentScrollX = 0;
		startScroll();
	}
}

