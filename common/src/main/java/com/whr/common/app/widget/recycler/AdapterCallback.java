package com.whr.common.app.widget.recycler;

/**
 * Created by whrr5 on 2017/6/7.
 */

public interface AdapterCallback <DATA>{
	void update(DATA data, RecyclerAdapter.ViewHolder<DATA> holder);
}
