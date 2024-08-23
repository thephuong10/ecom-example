package com.vn.ecommerce.commonservice.helpers;

import java.text.Normalizer;

public class StringHelper {

    public static String genericSlugFromString(String str){
        return  Normalizer.normalize(str, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .replaceAll("đ", "d")
                .replaceAll(" ", "-");
    }

}
