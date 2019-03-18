package com.marck.shoppingmall.network.okhttp.exception;


/**
 * Created by Marck on 19/01/21
 *
 * Description：OkHttp 的异常处理封装，用于抛出异常
 */
public class OkHttpException extends Exception{

    private static final long serialVersionUID = 1L;

    /**
     * the server return code
     */
    private int ecode;

    /**
     * the server return error message
     */
    private Object emsg;

    public OkHttpException(int ecode, Object emsg){
        this.ecode = ecode;
        this.emsg = emsg;
    }

    public int getEcode() {
        return ecode;
    }

    public Object getEmsg() {
        return emsg;
    }
}
