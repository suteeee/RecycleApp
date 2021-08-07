package com.kt.recycleapp.java.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.kt.recycleapp.kt.activity.MainActivity;

import java.io.IOException;
import java.recycleapp.R;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class RecycleDayInfoFragment extends Fragment {
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.KOREA);
    Calendar calendar = Calendar.getInstance();
    String weekDay = dateFormat.format(calendar.getTime());
    long tmp = System.currentTimeMillis() + (1000 * 60 * 60 * 24);
    String nextDay = dateFormat.format(tmp);





    private static final int GPS_ENABLE_REQUEST_CODE = 2000;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};



    public String getCurrentAddress( double latitude, double longitude, Context context) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation( latitude, longitude, 100);
        }
        catch (IOException ioException) {
            Toast.makeText(context, "서비스 사용불가", Toast.LENGTH_LONG).show();
            //showDialogForLocationServiceSetting(context);
            return "서비스 사용불가";
        }
        catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(context, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            //showDialogForLocationServiceSetting(context);
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(context, "현재 위치 미발견", Toast.LENGTH_LONG).show();
            //showDialogForLocationServiceSetting(context);
            return "현재 위치 미발견";
        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";
    }


    //여기부터는 GPS 활성화를 위한 메소드
    /*
    private void showDialogForLocationServiceSetting(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) { dialog.cancel();
            }
        });


        builder.create().show();


    }
    */

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE: //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                } break;
        }
    }
    */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycle_day_info, container, false);
        ((MainActivity)getActivity()).viewModel.getToolbarText().setValue("분리수거 요일제 안내");
        int ASDFASDF = 1;

        if(ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},ASDFASDF);
        }

        if(ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},ASDFASDF);
        }


        TextView textViewWhere = rootView.findViewById(R.id.nowwhere_bt1);
        TextView textViewToday = rootView.findViewById(R.id.dayoftheweektoday_bt1);
        TextView textViewNextday = rootView.findViewById(R.id.dayoftheweektomorrow_bt1);

        GpsTracker gpsTracker = new GpsTracker(rootView.getContext());
        double latitude = gpsTracker.getLatitude();// 위도
        double longitude = gpsTracker.getLongitude(); //경도
        String address = getCurrentAddress(latitude, longitude, rootView.getContext());

        String str = address;
        if(address!=null){
            List<String> addressList = Arrays.asList(str.split(" "));
            textViewWhere.setText("현재위치 : "+ addressList.get(1) + " " +  addressList.get(2));
        }
        else{
            textViewWhere.setText("위치권환을 허용해주세요.");
        }



        textViewToday.setText("오늘은 " + weekDay + "입니다");
        textViewNextday.setText("내일은 " + nextDay + "입니다");

        Spannable span1 = (Spannable) textViewToday.getText();
        Spannable span2 = (Spannable) textViewNextday.getText();
        span1.setSpan(new ForegroundColorSpan(Color.BLUE), 4, 7, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        span2.setSpan(new ForegroundColorSpan(Color.RED), 4, 7, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);


        return rootView;

    }
}