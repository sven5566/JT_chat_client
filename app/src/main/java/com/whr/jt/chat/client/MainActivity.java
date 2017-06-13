package com.whr.jt.chat.client;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.whr.common.app.Activity;
import com.whr.common.app.widget.PortraitView;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener {

	@BindView(R.id.appbar)
	View mLayAppbar;
	@BindView(R.id.im_portrait)
	PortraitView mPortrait;
	@BindView(R.id.txt_title)
	TextView mTitle;
	@BindView(R.id.im_search)
	ImageView imSearch;
	@BindView(R.id.lay_container)
	FrameLayout mContainer;

	@BindView(R.id.navigation)
	BottomNavigationView mNavigation;

	@Override
	protected int getContentId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initWidgt() {
		super.initWidgt();
		mNavigation.setOnNavigationItemSelectedListener(this);
		Glide.with(this).load(R.drawable.bg_src_morning).centerCrop().into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
			@Override
			public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
				this.view.setBackground(resource.getCurrent());
			}
		});
	}

	@OnClick(R.id.btn_action)
	public void clickBtn(){

	}

	@OnClick(R.id.im_search)
	public void clickIm(){

	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		mTitle.setText(item.getTitle());
		return false;
	}
}
