package cc.upedu.online.contextmenu.lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.upedu.online.R;

public class Utils {

    public static int getDefaultActionBarSize(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarSize;
    }

    public static TextView getItemTextView(Context context, MenuObject menuItem, int menuItemSize,int id) {
        TextView itemTextView = new TextView(context);
        RelativeLayout.LayoutParams textLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, menuItemSize);
        itemTextView.setLayoutParams(textLayoutParams);
//        itemTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.menu_text_size));
        itemTextView.setText(menuItem.getTitle());
        itemTextView.setPadding((int) context.getResources().getDimension(R.dimen.text_left_padding), 0, (int) context.getResources().getDimension(R.dimen.text_right_padding), 0);
        itemTextView.setGravity(Gravity.CENTER_VERTICAL);
        int textColor = menuItem.getTextColor() == 0 ?
                context.getResources().getColor(android.R.color.white) :
                menuItem.getTextColor();
	    int textBgColor = menuItem.getTextColor() == 0 ?
	    		context.getResources().getColor(android.R.color.transparent) :
	    			menuItem.getTextBgColor();
        itemTextView.setBackgroundColor(textBgColor);
        itemTextView.setTextColor(textColor);
        itemTextView.setTextAppearance(context, menuItem.getMenuTextAppearanceStyle() > 0
                ? menuItem.getMenuTextAppearanceStyle()
                : R.style.TextView_DefaultStyle);
        itemTextView.setId(id);
        return itemTextView;
    }

    public static ImageView getItemImageButton(Context context, MenuObject item,View rightOf,int id) {
        ImageView imageView = new ImageButton(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.menu_image_size),
        			(int) context.getResources().getDimension(R.dimen.menu_image_size));
        lp.addRule(RelativeLayout.RIGHT_OF, rightOf.getId());
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        lp.setMargins(0, 0, (int) context.getResources().getDimension(R.dimen.divider_left_padding), 0);
        imageView.setLayoutParams(lp);
        imageView.setPadding((int) context.getResources().getDimension(R.dimen.menu_item_padding),
                (int) context.getResources().getDimension(R.dimen.menu_item_padding),
                (int) context.getResources().getDimension(R.dimen.menu_item_padding),
                (int) context.getResources().getDimension(R.dimen.menu_item_padding));
        imageView.setClickable(false);
        imageView.setFocusable(false);
        imageView.setBackgroundColor(Color.TRANSPARENT);

        if (item.getColor() != 0) {
            Drawable color = new ColorDrawable(item.getColor());
            imageView.setImageDrawable(color);
        } else if (item.getResource() != 0) {
            imageView.setImageResource(item.getResource());
        } else if (item.getBitmap() != null) {
            imageView.setImageBitmap(item.getBitmap());
        } else if (item.getDrawable() != null) {
            imageView.setImageDrawable(item.getDrawable());
        }
        imageView.setScaleType(item.getScaleType());
        imageView.setId(id);
        return imageView;
    }

    public static View getDivider(Context context, MenuObject menuItem,View alignLeft,View alignRight) {
        View dividerView = new View(context);
        RelativeLayout.LayoutParams viewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.divider_height));
        viewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        viewLayoutParams.addRule(RelativeLayout.ALIGN_LEFT, alignLeft.getId());
        viewLayoutParams.addRule(RelativeLayout.ALIGN_RIGHT, alignRight.getId());
        viewLayoutParams.setMargins((int) context.getResources().getDimension(R.dimen.divider_left_padding), 0, 0, 0);
        dividerView.setLayoutParams(viewLayoutParams);
        dividerView.setClickable(true);
        int dividerColor = menuItem.getDividerColor() == Integer.MAX_VALUE ?
                context.getResources().getColor(R.color.white_4c) :
                menuItem.getDividerColor();
        dividerView.setBackgroundColor(dividerColor);
        return dividerView;
    }

//    public static RelativeLayout getImageWrapper(Context context, MenuObject menuItem, int menuItemSize,
//                                                 View.OnClickListener onCLick, View.OnLongClickListener onLongClick) {
//        RelativeLayout imageWrapper = new RelativeLayout(context);
//        LinearLayout.LayoutParams imageWrapperLayoutParams = new LinearLayout.LayoutParams(menuItemSize, menuItemSize);
//        imageWrapper.setLayoutParams(imageWrapperLayoutParams);
//        imageWrapper.addView(Utils.getItemImageButton(context, menuItem));
//
//        if (menuItem.getBgColor() != 0) {
//            imageWrapper.setBackgroundColor(menuItem.getBgColor());
//        } else if (menuItem.getBgDrawable() != null) {
//            imageWrapper.setBackground(menuItem.getBgDrawable());
//        } else if (menuItem.getBgResource() != 0) {
//            imageWrapper.setBackgroundResource(menuItem.getBgResource());
//        } else {
////            imageWrapper.setBackgroundColor(context.getResources().getColor(R.color.menu_fragment_background));
//        }
//        return imageWrapper;
//    }
    /**
     * 
     * @param mContext
     * @param menuObject
     * @param mMenuItemSize
     * @param clickItem
     * @param longClickItem
     * @param i
     * @return
     */
	@SuppressLint("NewApi")
	public static RelativeLayout getLayoutWrapper(Context mContext,
			MenuObject menuObject, int mMenuItemSize,
			OnClickListener clickItem, OnLongClickListener longClickItem, int totalSize,int position) {
		// TODO Auto-generated method stub
		RelativeLayout layoutWrapper = new RelativeLayout(mContext);
		RelativeLayout.LayoutParams layoutWrapperLayoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, mMenuItemSize);
		layoutWrapper.setLayoutParams(layoutWrapperLayoutParams);
		layoutWrapper.setClipChildren(true);
		layoutWrapper.setOnClickListener(clickItem);
		layoutWrapper.setOnLongClickListener(longClickItem);
		
		TextView itemTextView = Utils.getItemTextView(mContext, menuObject, mMenuItemSize,position);
		layoutWrapper.addView(itemTextView);
		ImageView itemImageButton = Utils.getItemImageButton(mContext, menuObject,itemTextView,position+totalSize);
		layoutWrapper.addView(itemImageButton);
		if (menuObject.getBgColor() != 0) {
			layoutWrapper.setBackgroundColor(menuObject.getBgColor());
        } else if (menuObject.getBgDrawable() != null) {
        	layoutWrapper.setBackground(menuObject.getBgDrawable());
        } else if (menuObject.getBgResource() != 0) {
        	layoutWrapper.setBackgroundResource(menuObject.getBgResource());
        } else {
//            imageWrapper.setBackgroundColor(context.getResources().getColor(R.color.menu_fragment_background));
        }
		
        if (position == 0 || position != totalSize - 1) {
        	layoutWrapper.addView(getDivider(mContext, menuObject,itemTextView,itemImageButton));
        }
        if (position == 0) {
			layoutWrapper.setVisibility(View.INVISIBLE);
		}
		return layoutWrapper;
	}

}
