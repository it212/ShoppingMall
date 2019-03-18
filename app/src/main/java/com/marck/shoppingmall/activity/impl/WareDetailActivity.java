package com.marck.shoppingmall.activity.impl;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.marck.shoppingmall.R;
import com.marck.shoppingmall.activity.BaseActivity;
import com.marck.shoppingmall.model.hotpager.HotSale;
import com.marck.shoppingmall.network.http.HttpConstants;
import com.marck.shoppingmall.util.CartProvider;
import com.marck.shoppingmall.util.Constants;
import com.marck.shoppingmall.widget.CustomToolbar;

/**
 * Description: {@link WareDetailActivity} responsibility is show the
 * wares's detail page. it use a {@link WebView} to show the detail.
 *
 * Created by Marck on 19/03/12
 */
public class WareDetailActivity extends BaseActivity {

    private static final String TAG = WareListActivity.class.getSimpleName();

    /**
     * UI
     */
    private CustomToolbar mToolbar;
    private WebView mWebView;

    /**
     * Support
     */
    private CartProvider mCartProvider;
    private WebAppInterface mAppInterface;

    /**
     * Data
     */
    private HotSale mWare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_detail);
        mCartProvider = CartProvider.getInstance(this);
        getWareData();  // 获取商品的消息
        initView();  // 初始化视图
        initToolbar();  // 初始化标题栏
        initWebView();  // 初始化 WebView
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setAppCacheEnabled(true);

        mWebView.loadUrl(HttpConstants.WARE_DETAIL);
        mAppInterface = new WebAppInterface(this);
        mWebView.addJavascriptInterface(mAppInterface, "appInterface");
        mWebView.setWebViewClient(new WareClient());
    }

    private void initView() {
        mToolbar = (CustomToolbar) findViewById(R.id.ware_detail_toolbar);
        mWebView = (WebView) findViewById(R.id.web_view);
    }

    private void initToolbar() {
        mToolbar.setToolbarStyle(true);
        mToolbar.showEditButton(false);
        mToolbar.setOnButtonClickListener(new CustomToolbar.OnButtonClickListener() {
            @Override
            public void onLeftButtonClick(View view) {
                // 退出 Activity
                finish();
            }

            @Override
            public void onRightButtonClick(View view) {
                // do nothing
            }

            @Override
            public void onEditButtonClick(View view) {
                // do nothing
            }
        });
    }


    private void getWareData() {
        // 通过 Intent 获取 ware 的数据
        mWare = (HotSale) getIntent().getSerializableExtra(Constants.WARES_ID);
        if(mWare == null){
            // 如果 ware 为空，说明无数据需要展示，直接结束该 Activity
            finish();
        }
    }

    class WareClient extends WebViewClient{
        // 开启页面加载完成之后的事件监听，待 HTML5 页面
        // 加载完成后调用showDetail 方法展示数据，该方法
        // 具体逻辑位于 JavaScript 端
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mAppInterface.showDetail();
        }
    }

    class WebAppInterface{
        private Context mContext;

        WebAppInterface(Context context){
            this.mContext = context;
        }

        @JavascriptInterface
        public void showDetail(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 调用 JavaScript 端的方法
                    mWebView.loadUrl("javascript:showDetail("+ mWare.getId()+")");
                }
            });
        }

        // JavaScript 端调用 App 端的方法
        @JavascriptInterface
        public void buy(long id) {
            mCartProvider.put(mWare);
            Toast.makeText(mContext,"已添加到购物车", Toast.LENGTH_SHORT).show();
        }
    }
}
