package com.jinwoo.android.mymemo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebViewFragment extends Fragment {

    View view;
    WebView webView;


    public WebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view != null){
            return view;
        }

        view = inflater.inflate(R.layout.fragment_web_view, container, false);
        webView = (WebView)view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        // 줌 사용설정
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        // 웹뷰 클라이언트를 지정
        webView.setWebViewClient(new WebViewClient());
        // https 등을 처리하기 위한 핸들러
        webView.setWebChromeClient(new WebChromeClient());

        // 최초 로드시 google.com 으로 이동
        webView.loadUrl("http://google.com");

       return view;
    }

    public boolean goBack() {
        if(webView.canGoBack()){
            webView.goBack();
            return true;
        }else{
            return false;
        }
    }



}
