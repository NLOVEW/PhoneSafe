package com.example.nhb.nhb_phonesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by NHB on 2017/5/17.
 */

public class StreamUtil {
    public static String getInfo(InputStream is) {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        int len=0;
        byte[] buffer=new byte[1024];
        try {
            while((len=is.read(buffer))!=-1){
                baos.write(buffer,0,len);
            }
            return baos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
