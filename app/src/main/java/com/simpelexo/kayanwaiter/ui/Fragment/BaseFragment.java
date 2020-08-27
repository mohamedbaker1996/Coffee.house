package com.simpelexo.kayanwaiter.ui.Fragment;

import androidx.fragment.app.Fragment;

import com.simpelexo.kayanwaiter.ui.Activity.BaseActivity;


public class BaseFragment extends Fragment {
 public BaseActivity baseActivity;
    public void setUpActivity() {
        baseActivity = (BaseActivity) getActivity();

        baseActivity.baseFragment = this;
    }
 public void onBack(){
     baseActivity.superBackPressed();
    }
}