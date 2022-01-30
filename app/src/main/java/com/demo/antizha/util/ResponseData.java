package com.demo.antizha.util;

import java.io.Serializable;

/* loaded from: classes3.dex */
public class ResponseData implements Serializable {
    private Object data;
    private String msg;
    private Integer status;

    public Object getData() {
        return this.data;
    }

    public String getMsg() {
        return this.msg;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setData(Object obj) {
        this.data = obj;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public void setStatus(Integer num) {
        this.status = num;
    }
}
