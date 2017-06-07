package com.whr.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by whrr5 on 2017/6/7.
 */
public abstract class Fragment extends android.support.v4.app.Fragment {

	private View mRoot;
	private Unbinder unbinder;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		initArgs(getArguments());
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if(mRoot==null){
			View root= inflater.inflate(getContentLayoutId(),container,false);
			mRoot = root;
			initWidgt(mRoot);
		}else if(mRoot.getParent()!=null){
			((ViewGroup)mRoot.getParent()).removeView(mRoot);
		}
		return mRoot;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initData();
	}
	protected void initArgs(Bundle arguments){}
	protected void initWidgt(View view){
		unbinder = ButterKnife.bind(this, view);
	}
	protected void initData(){}
	@LayoutRes
	protected abstract  int getContentLayoutId();

	/**
	 * 按返回键时调用
	 * @return true:已处理逻辑，让Activity不用去管
	 */
	public boolean onBackPressed(){
		return false;
	}
}
