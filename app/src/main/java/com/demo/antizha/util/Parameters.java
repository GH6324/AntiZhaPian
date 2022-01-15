package com.demo.antizha.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/* compiled from: Parameters.java */
/* renamed from: util.v1.a */
/* loaded from: classes3.dex */
public class Parameters {

    /* renamed from: a  reason: collision with root package name */
    private final Map<String, Map<Integer, Object>> linkedHashMap = new LinkedHashMap();

    /* renamed from: b  reason: collision with root package name */
    private int f14583b = -1;

    /* renamed from: c  reason: collision with root package name */
    private int f14584c = 0;

    /* renamed from: d  reason: collision with root package name */
    private int f14585d = 0;

    public void a(String str, Object obj) throws IllegalStateException {
        if (str != null) {
            if (a(obj)) {
                this.f14584c++;
                int i2 = this.f14583b;
                if (i2 <= -1 || this.f14584c <= i2) {
                    Map<Integer, Object> map = this.linkedHashMap.get(str);
                    if (map == null) {
                        map = new LinkedHashMap<>(1);
                        this.linkedHashMap.put(str, map);
                    }
                    int i3 = this.f14585d;
                    this.f14585d = i3 + 1;
                    Integer valueOf = Integer.valueOf(i3);
                    if (obj == null) {
                        obj = "";
                    }
                    map.put(valueOf, obj);
                    return;
                }
                throw new IllegalStateException("parameters.maxCountFail: " + this.f14583b);
            }
            throw new IllegalArgumentException("Please use value which is primitive type like: String,Integer,Long and so on. But not Collection !");
        }
    }

    /* renamed from: b */
    public String value(String str) {
        Map<Integer, Object> map = this.linkedHashMap.get(str);
        if (map == null || map.size() == 0) {
            return "";
        }
        String obj = map.values().iterator().next().toString();
        if (obj == null || "null".equals(obj)) {
            return "";
        }
        return obj;
    }

    public boolean isEmpty() {
        return this.linkedHashMap.isEmpty();
    }

    public Set<String> b() {
        return this.linkedHashMap.keySet();
    }

    private boolean a(Object obj) {
        return obj == null || (obj instanceof String) || (obj instanceof Integer) || (obj instanceof Long) || (obj instanceof Boolean) || (obj instanceof Float) || (obj instanceof Double) || (obj instanceof Character) || (obj instanceof Byte) || (obj instanceof Short);
    }

    public String[] a(String str) {
        Map<Integer, Object> map = this.linkedHashMap.get(str);
        if (map == null) {
            return null;
        }
        return (String[]) map.values().toArray(new String[map.size()]);
    }

    public int a() {
        return this.f14583b;
    }

    public void a(int i2) {
        this.f14583b = i2;
    }
}