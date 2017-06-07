package com.whr.jt.chat.client;

import android.widget.TextView;

import com.whr.common.app.Activity;

import butterknife.BindView;

public class MainActivity extends Activity {
	@BindView(R.id.main_tv)
	TextView mainTv;

	@Override
	protected int getContentId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initWidgt() {
		super.initWidgt();
		mainTv.setText("asdfww");
	}
}
