package com.simpelexo.kayanwaiter.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.simpelexo.kayanwaiter.ui.Fragment.BaseFragment;


public class BaseActivity extends AppCompatActivity {

   public BaseFragment baseFragment;


    public void superBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
      baseFragment.onBack();
    }
}
