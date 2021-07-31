package com.kt.recycleapp.java.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SearchView;

import com.kt.recycleapp.kt.activity.MainActivity;
import com.kt.recycleapp.kt.activity.OnBackPressListener;
import com.kt.recycleapp.kt.fragment.MainFragment;

import java.recycleapp.R;


public class AdvancedSearchFragment extends Fragment implements OnBackPressListener {

    private WebView webView;
    private String url = "http://m.me.go.kr/m/mob/search/list.do"; //모바일 링크

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //rootView는 액티비티를 나타냄 (container는 우리끼리 mainactivity레이아웃을 의미하는 것으로 약속)
        //아래 코드는 액티비티 자체를 가져오는 것이다
        View rootView = inflater.inflate(R.layout.fragment_advanced_search, container, false);

        SearchView searchView;
        searchView = rootView.findViewById(R.id.searchtext_sv1);

        webView = (WebView)rootView.findViewById(R.id.webresult_wv1);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게



        //아래코드는 무조건 webView아래에 있어야함
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); //자바 스크립트 허용
        webSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        webSettings.setSupportZoom(true); // 화면 줌 허용 여부
        webSettings.setBuiltInZoomControls(true); // 화면 확대 축소 허용 여부
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기





        return rootView;
    }

    public void onBack() {
        MainActivity act = (MainActivity)getActivity();
        ((MainActivity) act).setOnBackPressListener(null);
        act.getSupportFragmentManager().beginTransaction().replace(R.id.small_layout1,new MainFragment()).commit();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setOnBackPressListener(this);
    }
}