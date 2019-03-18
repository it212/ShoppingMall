package com.marck.shoppingmall.network.http;

import com.marck.shoppingmall.model.categorypager.CategoryData;
import com.marck.shoppingmall.model.categorypager.WaresData;
import com.marck.shoppingmall.model.homepager.HomePagerData;
import com.marck.shoppingmall.model.hotpager.HotPagerData;
import com.marck.shoppingmall.network.okhttp.CommonOkHttpClient;
import com.marck.shoppingmall.network.okhttp.listener.DisposeDataHandler;
import com.marck.shoppingmall.network.okhttp.listener.DisposeDataListener;
import com.marck.shoppingmall.network.okhttp.request.CommonRequest;
import com.marck.shoppingmall.network.okhttp.request.RequestParams;

public class HttpRequestCenter {

    /**
     * 根据参数发送所有 post 请求
     *
     * @param url
     * @param params
     * @param listener
     * @param clazz
     */
    private static void postRequest(String url, RequestParams params,
                                    DisposeDataListener listener, Class<?> clazz){
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params),
                new DisposeDataHandler(listener, clazz));
    }

    /**
     * 请求首页推荐的数据
     *
     * @param listener
     */
    public static void requestHomePagerData(DisposeDataListener listener){
        RequestParams params = new RequestParams("type", "1");
        HttpRequestCenter.postRequest(HttpConstants.HOME_PAGER_URL, params, listener, HomePagerData.class);
    }

    /**
     * 请求热卖页数据
     * @param listener
     * @param currentPage
     */
    public static void requestHotPagerData(DisposeDataListener listener, int currentPage){
        RequestParams params = new RequestParams("curPage", Integer.toString(currentPage));
        params.put("pageSize", "10");
        HttpRequestCenter.postRequest(HttpConstants.HOT_PAGER_URL, params, listener, HotPagerData.class);
    }

    /**
     * 请求分类页的分类数据
     *
     * @param listener
     */
    public static void requestCategory(DisposeDataListener listener){
        HttpRequestCenter.postRequest(HttpConstants.CATEGORY_LIST, null, listener, CategoryData.class);
    }

    public static void requestWares(DisposeDataListener listener, int categoryId, int currentPage){
        RequestParams params = new RequestParams("curPage", Integer.toString(currentPage));
        params.put("pageSize", "10");
        params.put("categoryId", Integer.toString(categoryId));
        HttpRequestCenter.postRequest(HttpConstants.WARES_LIST, params, listener, WaresData.class);
    }

    public static void requestWareListData(DisposeDataListener listener, int campaignId, int currentPage, int orderBy){
        RequestParams params = new RequestParams("curPage", Integer.toString(currentPage));
        params.put("pageSize", "10");
        params.put("orderBy", Integer.toString(orderBy));
        params.put("campaignId", Integer.toString(campaignId));
        HttpRequestCenter.postRequest(HttpConstants.WARE_LIST_PAGER, params, listener, HotPagerData.class);
    }
}
