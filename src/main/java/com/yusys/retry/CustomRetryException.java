package com.yusys.retry;

import javax.crypto.MacSpi;

/**
 * Created by huyang on 2019/10/12.
 */
public class CustomRetryException extends  Exception {

    public CustomRetryException() {
        super();
    }

    public CustomRetryException(String msg){
        super(msg);
    }
}
