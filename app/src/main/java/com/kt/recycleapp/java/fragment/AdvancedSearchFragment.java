package com.kt.recycleapp.java.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kt.recycleapp.kotlin.Internet;
import com.kt.recycleapp.kotlin.alert.AlertFragment;
import com.kt.recycleapp.kotlin.main.MainActivity;
import com.kt.recycleapp.kotlin.main.MainFragment;
import com.kt.recycleapp.kotlin.main.MainViewModel;
import com.kt.recycleapp.manager.MyPreferenceManager;

import java.recycleapp.R;


public class AdvancedSearchFragment extends Fragment{

    private WebView webView;
    private String url = "http://m.me.go.kr/m/mob/main.do"; //모바일 링크

    MainActivity act;
    MainViewModel viewModel;

    MyPreferenceManager prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //rootView는 액티비티를 나타냄 (container는 우리끼리 mainactivity레이아웃을 의미하는 것으로 약속)
        //아래 코드는 액티비티 자체를 가져오는 것이다
        View rootView = inflater.inflate(R.layout.fragment_advanced_search, container, false);
       // ((MainActivity)getActivity()).viewModel.getToolbarText().setValue("상세정보검색");

        prefs = new MyPreferenceManager(requireContext());
        webView = (WebView)rootView.findViewById(R.id.webresult_wv1);

        int iStatus = Internet.INSTANCE.getStatus(requireContext());

        if(iStatus == Internet.INSTANCE.getNOT_CONNECT()){
            AlertFragment.Companion.showAlert(act, "CantWebOpen", true);
            act.replaceFragment(new MainFragment());
        }
        else if(iStatus == Internet.INSTANCE.getMOBILE_DATA() && !(prefs.getMobileInternetShow())) {
            act.showToast(getResources().getString(R.string.mobileData));
            prefs.setMobileInternetShow(true);
            webInit(webView);
        }
        else{
            webInit(webView);
        }

        return rootView;
    }

    public void webInit(WebView webView) {
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

    }

    public void onAttach(Context context) {
        super.onAttach(context);
        act = (MainActivity)getActivity();
        viewModel = act.viewModel;

        viewModel.getSelectedFragment().setValue("adv");
        viewModel.getFragmentStack().push("adv");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        viewModel.getFragmentStack().pop();
        viewModel.getSelectedFragment().setValue(viewModel.getFragmentStack().peek());

        if(viewModel.getFragmentStack().peek().equals("main"))
            act.replaceFragment(new MainFragment());
            //act.getSupportFragmentManager().beginTransaction().replace(R.id.small_layout1,new MainFragment()).commit();
    }
}