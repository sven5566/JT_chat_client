package com.whr.jt.chat.client;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.whr.common.app.Activity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity {
	@BindView(R.id.edit_query)
	EditText mEtQuery;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;
	@BindView(R.id.tv_result)
	TextView mTvResult;
	@Override
	protected int getContentId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initWidgt() {
		super.initWidgt();
	}

	@OnClick(R.id.btn_submit)
	public void clickBtn(){
	}

}
