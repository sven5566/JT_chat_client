package com.whr.jt.chat.client;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.whr.common.app.Activity;
import com.whr.common.app.widget.PortraitView;
import com.whr.jt.chat.client.frags.main.ActiveFragment;
import com.whr.jt.chat.client.frags.main.ContactFragment;
import com.whr.jt.chat.client.frags.main.GroupFragment;
import com.whr.jt.chat.client.helper.NavHelper;
import com.whr.lang.util.ObjUtil;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity implements BottomNavigationView
		.OnNavigationItemSelectedListener {

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

	@BindView(R.id.btn_action)
	FloatActionButton mAction;
	@BindView(R.id.navigation)
	BottomNavigationView mNavigation;
	NavHelper<Integer> mNavHelper;

	@Override
	protected int getContentId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initWidgt() {
		super.initWidgt();
		mNavigation.setOnNavigationItemSelectedListener(this);

		mNavHelper = new NavHelper<>(this, getSupportFragmentManager(), R.id.lay_container,
				listener);
		mNavHelper.add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string
				.action_home));
		mNavHelper.add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string
				.action_contact));
		mNavHelper.add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string
				.action_group));
		Glide.with(this).load(R.drawable.bg_src_morning).centerCrop().into(new ViewTarget<View,
				GlideDrawable>(mLayAppbar) {
			@Override
			public void onResourceReady(GlideDrawable resource, GlideAnimation<? super
					GlideDrawable> glideAnimation) {
				this.view.setBackground(resource.getCurrent());
			}
		});
		mNavigation.getMenu().performIdentifierAction(R.id.action_home, 0);
	}

	final NavHelper.OnTabChangedListener<Integer> listener = new NavHelper
			.OnTabChangedListener<Integer>() {
		@Override
		public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
			mTitle.setText(newTab.extra);
			float transY = 0f;
			float rotation = 0f;
			if (ObjUtil.equals(newTab.extra, R.string.action_home)) {
				transY = Ui.dipToPx(getResources(),76);
			} else {
				if (ObjUtil.equals(newTab.extra, R.string.action_group)) {
					mAction.setImageResource(R.drawable.ic_group_add);
					rotation=-360f;
				}else{
					mAction.setImageResource(R.drawable.ic_contact_add);
					rotation=360f;
				}

			}
			mAction.animate().rotation(rotation).translationY(transY).setInterpolator(new
					AnticipateInterpolator(1)).setDuration(480).start();


		}
	};

	@OnClick(R.id.btn_action)
	public void clickBtn() {

	}

	@OnClick(R.id.im_search)
	public void clickIm() {

	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		return mNavHelper.performMenuClick(item.getItemId());
	}
}
