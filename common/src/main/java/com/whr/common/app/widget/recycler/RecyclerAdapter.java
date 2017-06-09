package com.whr.common.app.widget.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whr.common.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by whrr5 on 2017/6/7.
 */

public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>> implements
		View.OnClickListener,
		View.OnLongClickListener,
		AdapterCallback<Data> {
		List<Data> mDataList = new ArrayList<>();
	/**
	 * @param parent
	 * @param viewType 约定为xml布局的id
	 * @return
	 */
	@Override
	public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
		final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View root = inflater.inflate(viewType, parent, false);
		final ViewHolder<Data> viewHolder = onCreateViewHolder(root, viewType);
		root.setOnClickListener(this);
		root.setOnLongClickListener(this);
		root.setTag(R.id.tag_recycler_holder, viewHolder);
		viewHolder.unbinder = ButterKnife.bind(viewHolder, root);
		viewHolder.callback = this;
		return viewHolder;
	}

	public abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}


	@Override
	public void onBindViewHolder(ViewHolder<Data> holder, int position) {
		holder.bind(mDataList.get(position));
	}

	@Override
	public int getItemCount() {
		return mDataList.size();
	}


	/**
	 * 用于绑定数据的触发
	 *
	 * @param <Data>
	 */
	public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {
		private Data mData;
		private Unbinder unbinder;
		private AdapterCallback<Data> callback;

		public ViewHolder(View itemView) {
			super(itemView);
		}

		void bind(Data data) {
			mData = data;
			onBind(data);
		}

		protected abstract void onBind(Data data);

		public void updataData(Data data) {
			if (callback != null) {
				callback.update(mData, this);
			}
		}
	}
}
