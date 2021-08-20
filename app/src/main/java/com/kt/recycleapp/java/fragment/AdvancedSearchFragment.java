package com.kt.recycleapp.java.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

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


public class AdvancedSearchFragment extends Fragment{

    private WebView webView;
    private String url = "http://m.me.go.kr/m/mob/main.do"; //모바일 링크

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //rootView는 액티비티를 나타냄 (container는 우리끼리 mainactivity레이아웃을 의미하는 것으로 약속)
        //아래 코드는 액티비티 자체를 가져오는 것이다
        View rootView = inflater.inflate(R.layout.fragment_advanced_search, container, false);
        ((MainActivity)getActivity()).viewModel.getToolbarText().setValue("상세정보검색");



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

        //속도향상위해 코드 추가
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            //기기에 따라서 동작할수도있는걸 확인
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

            //최신 SDK 에서는 Deprecated 이나 아직 성능상에서는 유용
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

            //부드러운 전환 또한 아직 동작
            webSettings.setEnableSmoothTransition(true);
        }
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setAppCacheEnabled(true);

        return rootView;
    }



    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)getActivity()).viewModel.getSelectedFragment().setValue("adv");
        ((MainActivity)getActivity()).viewModel.getFragmentStack().push("adv");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MainActivity act = ((MainActivity)getActivity());
        act.viewModel.getFragmentStack().pop();
        ((MainActivity)getActivity()).viewModel.getSelectedFragment().setValue(
                ((MainActivity)getActivity()).viewModel.getFragmentStack().peek());

        if(((MainActivity)getActivity()).viewModel.getFragmentStack().peek().equals("main"))
            act.getSupportFragmentManager().beginTransaction().replace(R.id.small_layout1,new MainFragment()).commit();

    }
}