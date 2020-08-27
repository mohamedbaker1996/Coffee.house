package com.simpelexo.kayanwaiter.ui.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simpelexo.kayanwaiter.R;


public class EmptyFragment extends BaseFragment {



    public EmptyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_empty, container, false);
        //  setUpActivity();
        return view;
    }
    @Override
    public void onBack(){
        super.onBack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
