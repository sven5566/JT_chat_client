package com.whr.jt.chat.client.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

/**
 * Created by whrr5 on 2017/6/13.
 */

public class NavHelper<T> {
	private final SparseArray<Tab<T>> tabs = new SparseArray<>();
	private final FragmentManager fragmentManager;
	private final int conttainerId;
	private final Context context;
	private final OnTabChangedListener<T> listener;
	private Tab<T> currentTab;

	public NavHelper( Context context,FragmentManager fragmentManager, int conttainerId,OnTabChangedListener<T> listener) {
		this.fragmentManager = fragmentManager;
		this.conttainerId = conttainerId;
		this.context = context;
		this.listener = listener;
	}

	public NavHelper<T> add(int menuId, Tab<T> tab) {
		tabs.put(menuId, tab);
		return this;
	}

	public Tab<T> getCurrentTab() {
		return currentTab;
	}

	/**
	 * 点击
	 * @param menuId
	 * @return
	 */
	public boolean performMenuClick(int menuId) {
		Tab tab = tabs.get(menuId);
		if (tab != null) {
			doSelect(tab);
			return true;
		}
		return false;
	}

	private void doSelect(Tab<T> tab) {
		Tab<T> oldTab=null;
		if (currentTab != null) {
			oldTab = currentTab;
			if (oldTab == tab) {
				notifyTabReselect(tab);
				return;
			}
		}
		currentTab=tab;
		doTabChanged(currentTab,oldTab);
	}

	private void doTabChanged(Tab<T>newTab,Tab<T>oldTab){
		final FragmentTransaction t = fragmentManager.beginTransaction();
		if(oldTab!=null){
			if (oldTab.fragment!=null){
				//从界面移除，但是还在Fragment的缓存中
				t.detach(oldTab.fragment);
			}
		}
		if(newTab!=null){
			if(newTab.fragment==null){
				Fragment fragment=Fragment.instantiate(context,newTab.clx.getName(),null);
				newTab.fragment=fragment;
				t.add(conttainerId,fragment,newTab.clx.getName());
			}else{
				t.attach(newTab.fragment);
			}
		}
		t.commit();
		notifyTabSelect(newTab,oldTab);
	}
	private void notifyTabSelect(Tab<T> newTab,Tab<T>oldTab){
		if(listener!=null){
			listener.onTabChanged(newTab, oldTab);
		}
	}
	private void notifyTabReselect(Tab<T> tab){
		//TODO 二次点击
	}

	public static class Tab<T> {
		public Tab(Class<? extends Fragment> clx, T extra) {
			this.clx = clx;
			this.extra = extra;
		}

		public Class<? extends Fragment> clx;
		public T extra;
		//内部缓存的对应的Fragment
		private Fragment fragment;
	}

	public interface OnTabChangedListener<T> {
		void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
	}
}
