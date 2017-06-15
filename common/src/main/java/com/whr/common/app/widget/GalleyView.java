package com.whr.common.app.widget;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.whr.common.R;
import com.whr.common.app.widget.recycler.RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 图片选择器
 */
public class GalleyView extends RecyclerView {
	private static final int LOADER_ID = 0x0100;
	private static final int MAX_IMAGE_COUNT = 3;//最大的选中图片数量
	private static final int MIN_IMAGE_FILE_SIZE = 10 * 1024;//最小的选中图片数量
	private Adapter mAdapter = new Adapter();
	private LoaderCallback mLoaderCallback = new LoaderCallback();
	private List<Image> mSelectedImages = new LinkedList<>();
	private SelectedChangeListener mListener;

	public GalleyView(Context context) {
		super(context);
		init();
	}

	public GalleyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public GalleyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setLayoutManager(new GridLayoutManager(getContext(), 4));
		setAdapter(mAdapter);
		mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
			@Override
			public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
				if (onItemSelectClick(image)) {
					holder.updataData(image);
				}
			}
		});
	}

	/**
	 * 初始化方法
	 *
	 * @param loaderManager
	 * @return 返回LoaderId可以用于销毁
	 */
	public int setUp(LoaderManager loaderManager, SelectedChangeListener listener) {
		mListener = listener;
		loaderManager.initLoader(LOADER_ID, null, mLoaderCallback);
		return LOADER_ID;
	}

	/**
	 * 得到选中图片的全部地址
	 *
	 * @return
	 */
	public String[] getSelectedPath() {
		String[] paths = new String[mSelectedImages.size()];
		int index = 0;
		for (Image image : mSelectedImages) {
			paths[index++] = image.path;
		}
		return paths;
	}

	/**
	 * 可以进行清空选中的图片
	 */
	public void clear() {
		for (Image image : mSelectedImages) {
			image.isSelect = false;
		}
		mSelectedImages.clear();
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * Cell点击的具体逻辑
	 *
	 * @param image
	 * @return
	 */
	private boolean onItemSelectClick(Image image) {
		boolean notifyRefresh;
		if (mSelectedImages.contains(image)) {
			mSelectedImages.remove(image);
			image.isSelect = false;
			notifyRefresh = true;
		} else {
			if (mSelectedImages.size() >= MAX_IMAGE_COUNT) {
				String str=getResources().getString(R.string.label_gallery_select_max_size);
				final String formatResultStr = String.format(str, MAX_IMAGE_COUNT);
				Toast.makeText(getContext(),formatResultStr, Toast.LENGTH_SHORT).show();
				notifyRefresh = false;
			} else {
				mSelectedImages.add(image);
				image.isSelect = true;
				notifyRefresh = true;
			}

		}
		//如果数据有更改，n我们需要通知外面的监听
		if (notifyRefresh) {
			notifySelectChanged();
		}
		return true;
	}

	/**
	 * 通知选中状态改变
	 */
	private void notifySelectChanged() {
		if (mListener != null) {
			mListener.onSelectedCountChanged(mSelectedImages.size());
		}
	}

	/**
	 * 用于实际的数据加载的Loader
	 */
	private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
		private final String[] IMAGE_PROJECTION = new String[]{
				MediaStore.Images.Media._ID,//id
				MediaStore.Images.Media.DATA,//图片路径
				MediaStore.Images.Media.DATE_ADDED,//图片创建时间

		};

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			//创建一个loader
			if (id == LOADER_ID) {
				return new CursorLoader(getContext(),
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[2] + " DESC");
			}
			return null;
		}

		/**
		 * 通知Adapter数据更改的方法
		 *
		 * @param images
		 */
		private void updateSource(List<Image> images) {
			mAdapter.replace(images);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			List<Image> images = new ArrayList<>();
			if (data != null) {
				int count = data.getCount();
				if (count > 0) {
					data.moveToFirst();
					int indexId = data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
					int indexPath = data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
					int indexDate = data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
					do {
						int id = data.getInt(indexId);
						String path = data.getString(indexPath);
						long dateTime = data.getLong(indexDate);

						File file = new File(path);
						if (!file.exists() || file.length() < MIN_IMAGE_FILE_SIZE) {
							continue;
						}

						Image image = new Image();
						image.id = id;
						image.path = path;
						image.date = dateTime;
						images.add(image);
					} while (data.moveToNext());
				}
			}
			updateSource(images);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			//进行页面清空操作
			updateSource(null);
		}
	}

	private static class Image {
		//数据的id
		int id;
		String path;//图片的路径
		long date;//创建的日期
		boolean isSelect;//是否选中

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Image image = (Image) o;
			return path != null ? path.equals(image.path) : image.path == null;
		}

		@Override
		public int hashCode() {
			return path != null ? path.hashCode() : 0;
		}
	}

	private class Adapter extends RecyclerAdapter<Image> {


		@Override
		public ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
			return new GalleyView.ViewHolder(root);
		}

		@Override
		protected int getItemViewType(int position, Image image) {
			return R.layout.cell_galley;
		}
	}

	private class ViewHolder extends RecyclerAdapter.ViewHolder<Image> {
		private ImageView mPic;
		private View mShade;
		private CheckBox mSelected;


		public ViewHolder(View itemView) {
			super(itemView);
			mPic = (ImageView) itemView.findViewById(R.id.im_img);
			mShade = itemView.findViewById(R.id.view_shade);
			mSelected = (CheckBox) itemView.findViewById(R.id.cb_select);

		}

		@Override
		protected void onBind(Image image) {
			Glide.with(getContext()).load(image.path)
					.diskCacheStrategy(DiskCacheStrategy.NONE)//不用缓存，直接加载原图
					.placeholder(R.color.grey_200)
					.centerCrop().into(mPic);
			mShade.setVisibility(image.isSelect?VISIBLE:INVISIBLE);
			mSelected.setChecked(image.isSelect);
			mSelected.setVisibility(VISIBLE);
		}
	}

	public interface SelectedChangeListener {
		void onSelectedCountChanged(int count);
	}

}
