package com.whr.jt.chat.client.frags.main;

import com.whr.common.app.Fragment;
import com.whr.common.app.widget.GalleyView;
import com.whr.jt.chat.client.R;

import butterknife.BindView;

public class ActiveFragment extends Fragment {
	@BindView(R.id.galleyView)
	GalleyView mGalleyView;
	@Override
	protected int getContentLayoutId() {
		return R.layout.fragment_active;
	}

	@Override
	protected void initData() {
		super.initData();
		mGalleyView.setUp(getLoaderManager(), new GalleyView.SelectedChangeListener() {
			@Override
			public void onSelectedCountChanged(int count) {

			}
		});
	}
}
