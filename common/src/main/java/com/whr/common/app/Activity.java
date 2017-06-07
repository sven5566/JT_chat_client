package com.whr.common.app;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by whrr5 on 2017/6/7.
 */

public abstract class Activity extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initWindow();
		if(initArgs(getIntent().getExtras())){
			setContentView(getContentId());
			initWidgt();
			initData();
		}else{
			finish();
		}
	}

	/**
	 * 初始化窗口
	 */
	protected void initWindow() {
	}

	/**
	 * 初始化参数
	 * @param bundle
	 * @return 如果初始化未成功，返回是否走后续逻辑
	 */
	protected boolean initArgs(Bundle bundle) {
		return true;
	}

	/**
	 * 初始化控件
	 */
	protected void initWidgt(){
		ButterKnife.bind(this);
	}

	/**
	 * 初始化数据
	 */
	protected void initData(){

	}

	/**
	 * 获取布局文件的id
	 * @return
	 */
	@LayoutRes
	protected abstract  int getContentId();

	@Override
	public boolean onNavigateUp() {
		finish();
		return super.onNavigateUp();
	}

	@Override
	public void onBackPressed() {
		final List<android.support.v4.app.Fragment> fragmentList = getSupportFragmentManager().getFragments();
		for(android.support.v4.app.Fragment item:fragmentList){
			if(item instanceof Fragment&&((Fragment)item).onBackPressed()){//里面的fragment如果已经消费了，就return
				return;
			}
		}
		super.onBackPressed();
		finish();
	}
}
