package com.ariful.poratechai.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ariful.poratechai.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveChatFragment extends Fragment {


    public ActiveChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_chat, container, false);
    }

}
