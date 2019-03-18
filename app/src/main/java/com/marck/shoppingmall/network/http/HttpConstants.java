package com.marck.shoppingmall.network.http;

public class HttpConstants {

    private static final String ROOT_URL = "http://api.marck.com";

    public static final String BASE_URL = "http://112.124.22.238:8081/course_api";

    /**
     * {@link com.marck.shoppingmall.fragment.impl.HomeFragment}
     * 首页商品数据 url
     */
    public static String HOME_PAGER_URL = ROOT_URL + "/banner/query";

    /**
     * {@link com.marck.shoppingmall.fragment.impl.HotFragment}
     * 热卖页商品数据 url
     */
    public static String HOT_PAGER_URL = BASE_URL + "/wares/hot";

    /**
     * {@link com.marck.shoppingmall.fragment.impl.CategoryFragment}
     * 分类页标签数据 url
     */
    public static final String CATEGORY_LIST = ROOT_URL + "/category/list";

    /**
     * {@link com.marck.shoppingmall.fragment.impl.CategoryFragment}
     * 分类页商品详情数据 url
     */
    public static final String WARES_LIST = BASE_URL + "/wares/list";

    /**
     * {@link com.marck.shoppingmall.activity.impl.WareListActivity} 商品列表数据 url
     */
    public static final String WARE_LIST_PAGER = BASE_URL +  "/wares/campaign/list";

    /**
     * {@link com.marck.shoppingmall.activity.impl.WareDetailActivity} 商品详情数据 url，
     * 为一个 HTML5 页面
     */
    public static final String WARE_DETAIL = BASE_URL + "/wares/detail.html";

}
