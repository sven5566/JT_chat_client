package com.whr.common.app.widget.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whr.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by whrr5 on 2017/6/7.
 */
@SuppressWarnings("unused")
public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>> implements
		View.OnClickListener,
		View.OnLongClickListener,
		AdapterCallback<Data> {
	List<Data> mDataList = new ArrayList<>();
	private AdapterListener<Data> mListener;

	public RecyclerAdapter() {
		this(null);
	}
	public RecyclerAdapter(AdapterListener<Data> mListener) {
		this(new ArrayList<Data>(),mListener);
	}
	public RecyclerAdapter(List<Data> mDataList, AdapterListener<Data> mListener) {
		this.mDataList = mDataList;
		this.mListener = mListener;
	}

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
		root.setTag(R.id.tag_recycler_holder, viewHolder);
		root.setOnClickListener(this);
		root.setOnLongClickListener(this);
		viewHolder.unbinder = ButterKnife.bind(viewHolder, root);
		viewHolder.callback = this;
		return viewHolder;
	}

	public abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

	@Override
	public int getItemViewType(int position) {
		return getItemViewType(position, mDataList.get(position));
	}

	@LayoutRes
	protected abstract int getItemViewType(int position, Data data);

	public void add(Data data) {
		mDataList.add(data);
		notifyItemInserted(mDataList.size() - 1);
	}

	public void add(Data... datas) {
		if (datas != null && datas.length > 0) {
			int startPoint = mDataList.size();
			Collections.addAll(mDataList, datas);
			notifyItemRangeInserted(startPoint, datas.length);
		}
	}

	public void add(Collection<Data> datas) {
		if (datas != null && datas.size() > 0) {
			int startPoint = mDataList.size();
			mDataList.addAll(datas);
			notifyItemRangeInserted(startPoint, datas.size());
		}
	}

	public void clear() {
		mDataList.clear();
		notifyDataSetChanged();
	}

	public void replace(Collection<Data> datas) {
		mDataList.clear();
		if (datas == null || datas.size() < 1) {
			return;
		}
		mDataList.addAll(datas);
		notifyDataSetChanged();
	}

	@Override
	public void update(Data data, ViewHolder<Data> holder) {
		int pos=holder.getAdapterPosition();
		if(pos>=0){
			mDataList.remove(pos);
			mDataList.add(pos,data);
			notifyItemChanged(pos);
		}
	}

	@Override
	public void onClick(View v) {
		ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
		if(mListener!=null){
			int position=viewHolder.getAdapterPosition();
			mListener.onItemClick(viewHolder,mDataList.get(position));
		}
	}

	@Override
	public boolean onLongClick(View v) {
		ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
		if(mListener!=null){
			int position=viewHolder.getAdapterPosition();
			mListener.onItemClick(viewHolder,mDataList.get(position));
			return true;
		}
		return false;
	}

	public void setListener(AdapterListener<Data> listener){
		this.mListener=listener;
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

	public interface AdapterListener<Data> {
		void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);

		void onItemLongClick(RecyclerAdapter.ViewHolder holder, Data data);
	}

	/**
	 * 对回调接口进行默认实现
	 * @param <Data>
	 */
	public static abstract class AdapterListenerImpl<Data> implements AdapterListener<Data>{

		@Override
		public void onItemClick(ViewHolder holder, Data data) {
		}

		@Override
		public void onItemLongClick(ViewHolder holder, Data data) {
		}
	}
}
