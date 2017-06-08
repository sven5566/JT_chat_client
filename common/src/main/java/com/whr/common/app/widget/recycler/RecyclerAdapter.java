package com.whr.common.app.widget.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by whrr5 on 2017/6/7.
 */

public class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>{
	List<Data> mDataList=new ArrayList<>();

	@Override
	public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
		return null;
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
	 * @param <Data>
	 */
	public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder{
		private Data mData;
		public ViewHolder(View itemView) {
			super(itemView);
		}
		void bind(Data data){
			mData=data;
			onBind(data);
		}
		protected abstract void onBind(Data data);
		public void updataData(Data data){
		}
	}
}
