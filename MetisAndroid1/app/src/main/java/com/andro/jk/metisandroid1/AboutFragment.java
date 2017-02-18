package com.andro.jk.metisandroid1;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Saikiran on 4/19/2016.
 */
public class AboutFragment extends Fragment{

    String userType;

    public static AboutFragment newInstance(String userType) {

        AboutFragment fragment = new AboutFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userType", userType);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userType = getArguments().get("userType").toString();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment, container, false);

        return view;
    }
}
