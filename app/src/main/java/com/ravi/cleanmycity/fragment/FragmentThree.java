package com.ravi.cleanmycity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ravi.cleanmycity.R;

public class FragmentThree extends Fragment {

    ImageView buttonSecond;
    TextView buttonFirst;

    public static final FragmentThree newInstance()
    {
        FragmentThree f = new FragmentThree();
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_three, container, false);
        buttonSecond = (ImageView) getActivity().findViewById(R.id.buttonSecond);
        buttonFirst = (TextView) getActivity().findViewById(R.id.buttonFirst);
        return v;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            buttonFirst.setText("");
            buttonFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            buttonSecond.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
            buttonSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }
    }
}