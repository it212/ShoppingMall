package com.marck.shoppingmall.network.okhttp.request;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vision
 * @function 封装所有的参数请求到 HashMap 中
 */
public class RequestParams {

    public ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<String, String>();
    public ConcurrentHashMap<String, Object> fileParams = new ConcurrentHashMap<String, Object>();

    /**
     * Constructs a new empty {@code RequestParams} instance
     */
    public RequestParams(){
        this((Map<String, String>) null);
    }

    /**
     * Constructs a new instance containing the key/value string
     * params for the specified map.
     *
     * @param source the source kry/value string map to add.
     */
    public RequestParams(Map<String, String> source) {
        if (source != null) {
            for (Map.Entry<String, String> entry : source.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Constructs a new RequestParams instance and populate it with a single
     * initial key/value string params.
     *
     * @param key   the key name for the initial param.
     * @param value the value string for the initial param.
     */
    public RequestParams(final String key, final String value){
        this(new HashMap<String, String>(){
            {
                put(key, value);
            }
        });
    }

    /**
     * add a key/value string pair to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value string for the new param.
     */
    public void put(String key, String value){
        if(key != null && value != null){
            urlParams.put(key, value);
        }
    }

    public void put(String key, Object value) throws FileNotFoundException{
        if(key != null){
            fileParams.put(key, value);
        }
    }

    public boolean hasParams(){
        return urlParams.size() > 0 || fileParams.size() > 0;
    }
}
