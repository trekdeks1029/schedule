package com.domain.train.ui.fragment.settings;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.domain.train.R;


import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutFragment extends Fragment {
    public static final String TAG = "AboutFragment";


    @BindView(R.id.about_version)
    TextView version;

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        createPage();
    }

    public void createPage(){
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String v = pInfo.versionName;
            this.version.setText(v);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            this.version.setText("1.0");
        }


    }

}
