package com.marck.shoppingmall.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.SpannableString;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;
import com.marck.shoppingmall.model.cartpager.CartData;
import com.marck.shoppingmall.model.hotpager.HotSale;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: Save to a local util class, use {@link SharedPreferences} to save
 * the data.Singleton mode for output.
 *
 * Created by Marck on 19/03/10
 */
public class CartProvider {

    private static final String TAG = CartProvider.class.getSimpleName();

    private static final String KEY_CART = "cartKey";

    private volatile static CartProvider sInstance = null;

    private SparseArray<CartData> mData = null;
    private Context mContext;

    public static CartProvider getInstance(Context context){
        if(sInstance == null){
            synchronized (CartProvider.class){
                if (sInstance == null){
                    sInstance = new CartProvider(context);
                }
            }
        }
        return sInstance;
    }

    private CartProvider(Context context){
        this.mContext = context;
        mData = new SparseArray<>(10);
        getSparseArrayData();
    }

    /**
     * 将数据储存的方法
     * @param cart
     */
    public void put(CartData cart){
        CartData temp = mData.get(cart.getId());
        if(temp != null){
            temp.setCount(temp.getCount() + 1);
        }else {
            temp = cart;
            temp.setCount(1);
        }
        mData.put(cart.getId(), temp);
        commit();
    }

    /**
     * 将数据储存的方法
     * @param hotSale
     */
    public void put(HotSale hotSale){
        CartData cart = convertData(hotSale);
        put(cart);
    }

    /**
     * 更新数据的方法
     * @param cart
     */
    public void update(CartData cart){
        mData.put(cart.getId(), cart);
        commit();
    }

    /**
     * 删除数据的方法
     * @param cart
     */
    public void delete(CartData cart){
        mData.delete(cart.getId());
        commit();
    }

    /**
     * 获取数据库中所有数据的方法
     * @return
     */
    public List<CartData> getAll(){
        return getDataFromSharedPreferences();
    }

    /**
     * 将 {@link HotSale} 转化为 {@link CartData} 类型
     * @param hotSale
     * @return
     */
    private CartData convertData(HotSale hotSale) {
        CartData cart = new CartData();
        cart.setIsChecked(true);
        cart.setId(hotSale.getId());
        cart.setImgUrl(hotSale.getImgUrl());
        cart.setName(hotSale.getName());
        cart.setPrice(hotSale.getPrice());
        return cart;
    }

    /**
     * 将内存中的数据提交给数据库
     */
    private void commit(){
        List<CartData> data = sparseToList();
        PreferencesUtils.putString(mContext, KEY_CART, JSONUtil.toJSON(data));
    }

    /**
     * 将 {@link SparseArray} 转化为 {@link List}
     * @return
     */
    private List<CartData> sparseToList(){
        int size = mData.size();
        List<CartData> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++){
            data.add(mData.valueAt(i));
        }
        return data;
    }

    /**
     * 从数据库中获取数据放置到内存中
     */
    private void getSparseArrayData(){
        List<CartData> data = getDataFromSharedPreferences();
        if(data != null && data.size() > 0){
            for (CartData cart : data){
                mData.put(cart.getId(), cart);
            }
        }
    }

    /**
     * 从本地数据库获取数据的方法
     * @return
     */
    private List<CartData> getDataFromSharedPreferences(){
        String jsonString = PreferencesUtils.getString(mContext, KEY_CART);
        List<CartData> data = null;
        if(jsonString != null){
            data = JSONUtil.fromJson(jsonString, new TypeToken<List<CartData>>(){}.getType());
        }
        return data;
    }

}
