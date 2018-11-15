package com.test.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.R;
import com.test.framework.BaseFragment;
import com.test.presenters.DestinationDownloadPresenter;
import com.test.presenters.IDestinationDownloadOperation;
import com.tomtom.online.sdk.map.MapFragment;
import com.tomtom.online.sdk.map.OnMapReadyCallback;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.model.MapTilesType;

import java.util.Locale;

import butterknife.ButterKnife;

public class DestinationDownloadFragment extends BaseFragment<DestinationDownloadPresenter>
        implements IDestinationDownloadOperation, OnMapReadyCallback {

    private TomtomMap mTomTomMap;
    private static final int PERMISSIONS_REQUEST_LOCATION = 1002;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected DestinationDownloadPresenter initPresenter() {
        return new DestinationDownloadPresenter(this, this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private boolean arePermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination_download, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        if (!arePermissionsGranted()) {
            requestPermissions(REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_LOCATION);
        }

        MapFragment mapFragment = (MapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getAsyncMap(this);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mTomTomMap.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull TomtomMap tomtomMap) {
        mTomTomMap = tomtomMap;
        mTomTomMap.setMyLocationEnabled(true);
        mTomTomMap.setLanguage(Locale.getDefault().getLanguage());
        mTomTomMap.getUiSettings().setMapTilesType(MapTilesType.VECTOR);
        mTomTomMap.getUiSettings().getCurrentLocationView().hide();
    }
}
