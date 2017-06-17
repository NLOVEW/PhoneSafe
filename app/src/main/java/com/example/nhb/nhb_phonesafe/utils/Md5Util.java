package com.example.nhb.nhb_phonesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by NHB on 2017/6/16.
 */

public class Md5Util {

    private static String value="";

    public static String Md5Convert(String key){
        try {
            MessageDigest digest=MessageDigest.getInstance("MD5");
            byte[] bt=digest.digest(key.getBytes());
            for(byte b:bt){
                int temp=b&255;
                if(temp>0&&temp<16){
                    value=value+0+Integer.toHexString(temp);
                }else{
                    value=value+Integer.toHexString(temp);
                }
            }
            return value;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return value;
    }
}
