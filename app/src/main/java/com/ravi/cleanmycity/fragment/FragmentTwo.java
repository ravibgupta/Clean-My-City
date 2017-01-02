package com.ravi.cleanmycity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ravi.cleanmycity.R;

public class FragmentTwo extends Fragment {

    ImageView buttonSecond;
    ViewPager pager;
    TextView buttonFirst;

    public static final FragmentTwo newInstance()
    {
        FragmentTwo f = new FragmentTwo();
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_two, container, false);
        pager =(ViewPager) getActivity().findViewById(R.id.viewpager);
        buttonSecond = (ImageView) getActivity().findViewById(R.id.buttonSecond);
        buttonFirst = (TextView) getActivity().findViewById(R.id.buttonFirst);
        return v;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            buttonFirst.setText("SKIP");
            buttonFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            buttonSecond.setImageDrawable(getResources().getDrawable(R.drawable.ic_forward));
            buttonSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager.setCurrentItem(2, true);
                }
            });
        }
    }
}