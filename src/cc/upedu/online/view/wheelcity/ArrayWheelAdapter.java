/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cc.upedu.online.view.wheelcity;

import android.content.Context;

import java.util.ArrayList;

/**
 * The simple Array wheel adapter
 * @param <T> the element type
 */
public class ArrayWheelAdapter extends AbstractWheelTextAdapter {
    
    // items
    private ArrayList<AddressBean> list;

    /**
     * Constructor
     * @param context the current context
     * @param items the items
     */
    public ArrayWheelAdapter(Context context, ArrayList<AddressBean> list) {
    	super(context);
//    	super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
//    	setItemTextResource(R.id.wheelcity_country_name);
        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
        this.list = list;
    }
    
    @Override
    public CharSequence getItemText(int index) {
    	if (list.size() == 0) {
    		return "";
    	}else {
    		if (index >= 0 && index < list.size()) {
//            T item = items[index];
//            if (item instanceof CharSequence) {
//                return (CharSequence) item;
//            }
//            return item.toString();
    			return list.get(index).getArea_name();
    		}
    		return null;
    	}
    }

    @Override
    public int getItemsCount() {
    	if (list.size() == 0) {
			return 1;
		}else {
			return list.size();
		}
    }
    
    public int getItemId(int index) {
		// TODO Auto-generated method stub
    	if (list.size() == 0) {
			return -1;
		}else {
			return list.get(index).getId();
		}
	}
}
