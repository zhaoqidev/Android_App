package cc.upedu.online.view.factory;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import cc.upedu.online.R;
import cc.upedu.online.utils.CommonUtil;

public class MyVerticalTitleEdit2Item {
	public static int showStyle = 0;
	public static final int TEXT_EDIT = 1;
	public static final int TEXT_EDIT_INDEX = 2;
	//根布局左右默认的padding值
	private static int paddingLeft_RingtDefault = 8;
	//根布局距离上方控件或父控件顶部的距离
	private static int marginTopDefault = 16;
	//控件之间或控件距两端默认的距离
	private static int widgetmarginDefault = 8;
	private static float textSizeDefault = 16;
	private static float editSizeDefault = 14;
	private static int colorDefault = 0xff323232;
	private static int colorEditDefault = 0xff444444;
	private static int colorHintDefault = 0xff5b5b5b;
	private static int maxLengthDefault = 100;
	private Context context;
	private View rootView;
	//左侧图标右边的文本
	private TextView textView;
	//title下边的输入框
	private EditText editText;
	//输入框字数角标
	private TextView indexNum;
	/**
	 * 生成-title输入框图片-样式view的工厂类,
	 * 
	 */
	public MyVerticalTitleEdit2Item(int showStyle) {
		// TODO Auto-generated constructor stub
		this.showStyle = showStyle;
	}
	public void initView(Context context) {
		this.context = context;
		// TODO Auto-generated method stub
		rootView = View.inflate(context, R.layout.factory_vertical_titleedit_2item, null);
		textView = (TextView) rootView.findViewById(R.id.text);
		editText = (EditText) rootView.findViewById(R.id.edit);
		indexNum = (TextView) rootView.findViewById(R.id.indexNum);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
		lp.setMargins(0, CommonUtil.dip2px(context, marginTopDefault), 0, 0);
		rootView.setLayoutParams(lp);
		rootView.setPadding(CommonUtil.dip2px(context, paddingLeft_RingtDefault), 0, CommonUtil.dip2px(context, paddingLeft_RingtDefault), 0);
		if (TEXT_EDIT == showStyle) {
			setIindexNumInVisibility();
		}else {
			editText.addTextChangedListener(mTextWatcher); 
		}
	}
	public static void setPaddingLeft_RingtDefault(int paddingLeft_RingtDefault) {
		MyVerticalTitleEdit2Item.paddingLeft_RingtDefault = paddingLeft_RingtDefault;
	}
	public static void setMarginTopDefault(int marginTopDefault) {
		MyVerticalTitleEdit2Item.marginTopDefault = marginTopDefault;
	}
	public View getRootView() {
		return rootView;
	}
	/**
	 * 设置隐藏右侧的图片
	 */
	public void setIindexNumInVisibility() {
		indexNum.setVisibility(View.GONE);
	}
	/**
	 * 设置文本的样式index
	 * @param textSize 文本字体大小,默认16
	 * @param marginLeft 左右两侧的边距,默认8dp
	 * @param maxWith 最大宽度,默认90dp
	 * @param color 文本字体颜色
	 */
	public void setTextStyle(int textSize,int marginLeft_Reight,int maxWith, int color) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == textView.getVisibility()) {
			RelativeLayout.LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
			if (0 <= widgetmarginDefault) {
				widgetmarginDefault = marginLeft_Reight;
				layoutParams.setMargins(CommonUtil.dip2px(context, widgetmarginDefault), 0, CommonUtil.dip2px(context, widgetmarginDefault), 0);
				textView.setLayoutParams(layoutParams);
			}
			if (0 < textSize) {
				textSizeDefault = textSize;
				textView.setTextSize(textSizeDefault);
			}
			if (0 < maxWith) {
				textView.setMaxWidth(CommonUtil.dip2px(context, maxWith));
			}
			if (0 < color) {
				colorDefault = color;
				textView.setTextColor(colorDefault);
			}
		}
	}
	/**
	 * 设置输入框的样式
	 * @param textSize 文字大小,默认16
	 * @param margin 边距,默认8dp
	 * @param color 输入文字字体颜色
	 * @param colorHint 提示文字字体颜色
	 */
	public void setEditStyle(int textSize,int margin,int color,int colorHint) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == editText.getVisibility()) {
			RelativeLayout.LayoutParams layoutParams = (LayoutParams) editText.getLayoutParams();
			if (0 <= widgetmarginDefault) {
				widgetmarginDefault = margin;
				layoutParams.setMargins(CommonUtil.dip2px(context, widgetmarginDefault), CommonUtil.dip2px(context, widgetmarginDefault-3), CommonUtil.dip2px(context, widgetmarginDefault), CommonUtil.dip2px(context, widgetmarginDefault));
				editText.setLayoutParams(layoutParams);
			}
			if (0 < textSize) {
				editSizeDefault = textSize;
				editText.setTextSize(editSizeDefault);
			}
			if (0 < color) {
				colorEditDefault = color;
				editText.setTextColor(colorEditDefault);
			}
			if (0 < colorHint) {
				colorHintDefault = colorHint;
				editText.setHintTextColor(colorHintDefault);
			}
		}
	}
	/**
	 * 设置字数角标的样式
	 * @param textSize 文本字体大小,默认12
	 * @param marginLeft 右侧的边距,默认8dp
	 * @param color 文本字体颜色
	 */
	public void setIndexNumStyle(int textSize,int marginLeft,int maxLength,int color) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == indexNum.getVisibility()) {
			RelativeLayout.LayoutParams layoutParams = (LayoutParams) indexNum.getLayoutParams();
			if (0 <= widgetmarginDefault) {
				widgetmarginDefault = marginLeft;
				layoutParams.setMargins(0, 0, CommonUtil.dip2px(context, widgetmarginDefault), 0);
				indexNum.setLayoutParams(layoutParams);
			}
			if (0 < textSize) {
				textSizeDefault = textSize;
				indexNum.setTextSize(textSizeDefault);
			}
			if (0 < maxLength) {
				maxLengthDefault = maxLength;
				textView.setFilters(new  InputFilter[]{ new  InputFilter.LengthFilter(maxLengthDefault)});  
			}
			if (0 < color) {
				colorHintDefault = color;
				indexNum.setTextColor(colorHintDefault);
			}
		}
	}
	public void setText(CharSequence text) {
		// TODO Auto-generated method stub
		textView.setText(text);
	}
	public void setEditText(CharSequence text) {
		// TODO Auto-generated method stub
		editText.setText(text);
	}
	public void setEditHintText(CharSequence text) {
		// TODO Auto-generated method stub
		editText.setHint(text);
	}
	private void setIndexNum(int num) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == indexNum.getVisibility()) {
			indexNum.setText(num+"/"+maxLengthDefault);
		}
	}
	public String getEditText() {
		// TODO Auto-generated method stub
		if (View.VISIBLE == editText.getVisibility()) {
			return editText.getText().toString().trim();
		}else {
			return "";
		}
	}
//	public String getIndexNum() {
//		// TODO Auto-generated method stub
//		if (View.VISIBLE == editText.getVisibility()) {
//			return editText.getText().toString().trim().split("/")[0];
//		}else {
//			return "0";
//		}
//	}
	private TextWatcher mTextWatcher = new TextWatcher() {  
        private CharSequence temp;  
        @Override  
        public void onTextChanged(CharSequence s, int start, int before, int count) {  
            // TODO Auto-generated method stub  
             temp = s;  
        }  
          
        @Override  
        public void beforeTextChanged(CharSequence s, int start, int count,  
                int after) {  
            // TODO Auto-generated method stub  
//          mTextView.setText(s);//将输入的内容实时显示  
        }  
          
        @Override  
        public void afterTextChanged(Editable s) {  
            // TODO Auto-generated method stub  
//            editText.setText("您输入了" + temp.length() + "个字符");  
        	setIndexNum(temp.length());
        }  
    };  
}
