package com.yusys.skip;

/**
 * Created by huyang on 2019/10/12.
 */
public class CustomSkipException extends  Exception {

    public CustomSkipException() {
        super();
    }

    public CustomSkipException(String msg){
        super(msg);
    }
}
