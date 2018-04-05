package cc.upedu.online.view.citychoose;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;

public class CityChooseActity extends TitleBaseActivity {
	public static final int CHOOSE_ONE = 0;
	public static final int CHOOSE_TWO = 1;
	public static final int CHOOSE_THREE = 2;
	public static final int CHOOSE_FOUR = 3;
	private int currentChooseCode = -1;
	private String currentPosition;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("选择城市");
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		currentChooseCode = getIntent().getIntExtra("ChooseCode", -1);
		if (CHOOSE_FOUR == currentChooseCode) {
			currentPosition = getIntent().getStringExtra("currentPosition");
		}
		
		View view = View.inflate(context, R.layout.city_main_act, null);
		mSearchView = (EditText) view.findViewById(R.id.search_view);
		mLoadingView = (ProgressBar) view.findViewById(R.id.loading_view);
		mListView = (PinnedHeaderListView) view.findViewById(R.id.list_view);
		mEmptyView = (TextView) view.findViewById(R.id.empty_view);
		mSearchView.setVisibility(View.GONE);
		
		
		// Array to ArrayList
		mItems = new ArrayList<String>(Arrays.asList(Address.CITIES));
		mListSectionPos = new ArrayList<Integer>();
		mListItems = new ArrayList<String>();

		new Poplulate().execute(mItems);
		return view;
	}
	@Override
	protected void initData() {

	}
	
	/**
	 *  unsorted list items
	 *  元素数据集合
	 */
	ArrayList<String> mItems;

	/**
	 *  array list to store section positions
	 *  导航条数据的集合
	 */
	ArrayList<Integer> mListSectionPos;

	/**
	 *  array list to store listView data
	 *  展示的listview的数据集合
	 */
	ArrayList<String> mListItems;

	/**
	 * custom list view with pinned header
	 * 展示数据的 listview
	 */
	PinnedHeaderListView mListView;

	// custom adapter
	PinnedHeaderAdapter mAdaptor;

	// search box
	EditText mSearchView;

	/**
	 *  loading view
	 *  加载中的布局
	 */
	ProgressBar mLoadingView;

	/**
	 *  empty view
	 *  无数据时的布局
	 */
	TextView mEmptyView;

	

	// I encountered an interesting problem with a TextWatcher listening for
	// changes in an EditText.
	// The afterTextChanged method was called, each time, the device orientation
	// changed.
	// An answer on Stackoverflow let me understand what was happening: Android
	// recreates the activity, and
	// the automatic restoration of the state of the input fields, is happening
	// after onCreate had finished,
	// where the TextWatcher was added as a TextChangedListener.The solution to
	// the problem consisted in adding
	// the TextWatcher in onPostCreate, which is called after restoration has
	// taken place
	//
	// http://stackoverflow.com/questions/6028218/android-retain-callback-state-after-configuration-change/6029070#6029070
	// http://stackoverflow.com/questions/5151095/textwatcher-called-even-if-text-is-set-before-adding-the-watcher
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		mSearchView.addTextChangedListener(filterTextWatcher);
		super.onPostCreate(savedInstanceState);
	}

	private void setListAdaptor() {
		// create instance of PinnedHeaderAdapter and set adapter to list view
		mAdaptor = new PinnedHeaderAdapter(this, mListItems, mListSectionPos);
		mListView.setAdapter(mAdaptor);

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		/**
		 *  set header view
		 *  设置最上方分类标题的布局view
		 */
		View pinnedHeaderView = inflater.inflate(R.layout.city_section_row_view, mListView, false);
		mListView.setPinnedHeaderView(pinnedHeaderView);

		/**
		 *  set index bar view
		 *  设置右边的导航条
		 */
		IndexBarView indexBarView = (IndexBarView) inflater.inflate(R.layout.city_index_bar_view, mListView, false);
		indexBarView.setData(mListView, mListItems, mListSectionPos);
		mListView.setIndexBarView(indexBarView);

		/**
		 *  set preview text view
		 *  设置导航条当前位置内容放大显示的view
		 */
		View previewTextView = inflater.inflate(R.layout.city_preview_view, mListView, false);
		mListView.setPreviewView(previewTextView);

		/**
		 *  for configure pinned header view on scroll change
		 *  为listview设置监听
		 */
		mListView.setOnScrollListener(mAdaptor);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String cityText = mListItems.get(position);
				int cityId = Address.C_ID[position];
				if (cityId != -1) {
					Intent intent = new Intent();
					intent.putExtra("text", cityText);
					intent.putExtra("id", String.valueOf(cityId));
					if (CHOOSE_FOUR == currentChooseCode) {
						intent.putExtra("currentPosition", currentPosition);
					}
					setResult(currentChooseCode, intent);
					finish();
				}
			}
		});
	}

	private TextWatcher filterTextWatcher = new TextWatcher() {
		public void afterTextChanged(Editable s) {
			String str = s.toString();
			if (mAdaptor != null && str != null)
				mAdaptor.getFilter().filter(str);
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}
	};

	public class ListFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			// NOTE: this function is *always* called from a background thread,
			// and
			// not the UI thread.
			String constraintStr = constraint.toString().toLowerCase(Locale.getDefault());
			FilterResults result = new FilterResults();

			if (constraint != null && constraint.toString().length() > 0) {
				ArrayList<String> filterItems = new ArrayList<String>();

				synchronized (this) {
					for (String item : mItems) {
						if (item.toLowerCase(Locale.getDefault()).startsWith(constraintStr)) {
							filterItems.add(item);
						}
					}
					result.count = filterItems.size();
					result.values = filterItems;
				}
			} else {
				synchronized (this) {
					result.count = mItems.size();
					result.values = mItems;
				}
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			ArrayList<String> filtered = (ArrayList<String>) results.values;
			setIndexBarViewVisibility(constraint.toString());
			// sort array and extract sections in background Thread
			new Poplulate().execute(filtered);
		}

	}

	private void setIndexBarViewVisibility(String constraint) {
		// hide index bar for search results
		if (constraint != null && constraint.length() > 0) {
			mListView.setIndexBarVisibility(false);
		} else {
			mListView.setIndexBarVisibility(true);
		}
	}

	// sort array and extract sections in background Thread here we use
	// AsyncTask
	private class Poplulate extends AsyncTask<ArrayList<String>, Void, Void> {

		private void showLoading(View contentView, View loadingView, View emptyView) {
			contentView.setVisibility(View.GONE);
			loadingView.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
		}

		private void showContent(View contentView, View loadingView, View emptyView) {
			contentView.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
			emptyView.setVisibility(View.GONE);
		}

		private void showEmptyText(View contentView, View loadingView, View emptyView) {
			contentView.setVisibility(View.GONE);
			loadingView.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPreExecute() {
			// show loading indicator
			showLoading(mListView, mLoadingView, mEmptyView);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(ArrayList<String>... params) {
			mListItems.clear();
			mListSectionPos.clear();
			ArrayList<String> items = params[0];
			if (mItems.size() > 0) {
				
				mListItems.addAll(items);
				for (String string : Address.C_TITLE) {
					mListSectionPos.add(mListItems.indexOf(string));
				}

				// NOT forget to sort array
				//排序
//				Collections.sort(items, new SortIgnoreCase());
				//当前添加的分类
//				String prev_section = "";
//				for (String current_item : items) {
//					//取首字母作为分类标题
//					String current_section = current_item.substring(0, 1).toUpperCase(Locale.getDefault());
//					//判断当前项是否属于当前分类
//					if (!prev_section.equals(current_section)) {
//						//添加新新的分类标题
//						mListItems.add(current_section);
//						//添加当前元素
//						mListItems.add(current_item);
//						// array list of section positions
//						//导航条的内容,即所有的分类标题的集合
//						mListSectionPos.add(mListItems.indexOf(current_section));
//						//设置当前标题
//						prev_section = current_section;
//					} else {
//						//把当前元素添加到当前的分类
//						mListItems.add(current_item);
//					}
//				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (!isCancelled()) {
				if (mListItems.size() <= 0) {
					showEmptyText(mListView, mLoadingView, mEmptyView);
				} else {
					setListAdaptor();
					showContent(mListView, mLoadingView, mEmptyView);
				}
			}
			super.onPostExecute(result);
		}
	}

	public class SortIgnoreCase implements Comparator<String> {
		public int compare(String s1, String s2) {
			return s1.compareToIgnoreCase(s2);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (mListItems != null && mListItems.size() > 0) {
			outState.putStringArrayList("mListItems", mListItems);
		}
		if (mListSectionPos != null && mListSectionPos.size() > 0) {
			outState.putIntegerArrayList("mListSectionPos", mListSectionPos);
		}
		String searchText = mSearchView.getText().toString();
		if (searchText != null && searchText.length() > 0) {
			outState.putString("constraint", searchText);
		}
		super.onSaveInstanceState(outState);
	}
}
