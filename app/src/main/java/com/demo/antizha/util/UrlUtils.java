package com.demo.antizha.util;;

import android.text.TextUtils;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;


/* compiled from: UrlUtils.java */
/* renamed from: util.v1.c */
/* loaded from: classes3.dex */
public class UrlUtils {
    public static Parameters separateParam(String str) {
        Parameters aVar;
        if (TextUtils.isEmpty(str) || str.indexOf('?') <= -1) {
            aVar = null;
        } else {
            String substring = str.substring(str.indexOf('?') + 1);
            int indexOf = substring.indexOf('#');
            aVar = indexOf > -1 ? string2Param(substring.substring(0, indexOf)) : string2Param(substring);
        }
        return aVar == null ? new Parameters() : aVar;
    }

    public static Parameters string2Param(String str) {
        Parameters aVar = new Parameters();
        try {
            String[] split = str.split("&");
            if (split != null && split.length > 0) {
                for (String str2 : split) {
                    String[] split2 = str2.split("=", 2);
                    if (split2 != null && split2.length == 2) {
                        aVar.a(URLDecoder.decode(split2[0], java.nio.charset.StandardCharsets.UTF_8.toString()),
                                URLDecoder.decode(split2[1], java.nio.charset.StandardCharsets.UTF_8.toString()));
                    }
                }
            }
        } catch (Exception e2) {
            e2.getMessage();
        }
        return aVar;
    }

    public static String removeParam(String str, String str2) throws IOException {
        URL url = new URL(str);
        if (str2.isEmpty()) {
            return str;
        }
        if (url.getQuery().isEmpty()) {
            return str;
        }
        if (!str.contains("&" + str2)) {
            if (!str.contains("?" + str2)) {
                return str;
            }
        }
        return str.replace(str2, "");
    }
}
