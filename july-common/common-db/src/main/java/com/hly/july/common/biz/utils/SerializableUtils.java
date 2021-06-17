package com.hly.july.common.biz.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @ClassName SerializableUtils
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/16 16:14
 * @Version 1.0.0
 **/
public class SerializableUtils {
    public static byte[] obj2byte(Object obj) throws Exception {
        byte[] ret = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(obj);
        out.close();
        ret = baos.toByteArray();
        baos.close();
        return ret;
    }

    public static Object byte2obj(byte[] bytes) throws Exception {
        Object ret = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bais);
        ret = in.readObject();
        in.close();
        return ret;
    }


//    public String OutputStream(List<Object> results) throws Exception {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream( byteArrayOutputStream);
//        objectOutputStream.writeObject(results);
//        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
//        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
//
//        objectOutputStream.close();
//
//        byteArrayOutputStream.close();
//
//        return serStr;
//
//    }
//
//
//    public List> InputStream(String serStr) throws Exception {
//        String redStr = java.net.URLDecoder.decode(serStr, "UTF-8");
//
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
//
//                redStr.getBytes("ISO-8859-1"));
//
//        ObjectInputStream objectInputStream = new ObjectInputStream(
//
//                byteArrayInputStream);
//
//        List> results = (List>) objectInputStream.readObject();
//
//        objectInputStream.close();
//
//        byteArrayInputStream.close();
//
//        return results;
//
//    }


}
