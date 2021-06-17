package com.hly.july.common.biz.utils;

import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * 与sun.mis c套件和Apache Commons Codec所提供的Base64编解码器来比较的话，Java 8提供的Base64拥有更好的效能。实际测试编码与解码速度的话，Java 8提供的Base64，
 * 要比sun.mis c套件提供的还要快至少11倍，比Apache Commons Codec提供的还要快至少3倍。
 * 注意：Base编码会使编码后的内容比原来增加1/3
 * @Author hly
 */
public class MyBase64Utils {
    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Base64.Encoder encoder = Base64.getEncoder();

    /**
     * Java util 的Base64编码
     * @param source
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String jEncodeString(String source) throws UnsupportedEncodingException {
        byte[] textByte = source.getBytes("UTF-8");
        return encoder.encodeToString(textByte);
    }

    /**
     * Java util 的Base64解码
     * @param source
     * @return
     */
    private static String jDecodeString(String source) throws UnsupportedEncodingException {
        byte[] textByte = decoder.decode(source);
        return new String(textByte, "utf-8");
    }

    /**
     * Springframework提供的Base64编码
     * @param source
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String springEncodeString(String source) throws UnsupportedEncodingException {
        byte[] textByte = source.getBytes("UTF-8");
        return Base64Utils.encodeToString(textByte);
    }

    /**
     * Springframework提供的Base64解码
     * Spring提供的org.springframework.util.Base64Utils类，先会检测JDK里是否自带java.util.Base64，
     * 如果不带，则使用的是apache提供的org.apache.commons.codec.binary.Base64。
     * 我看了，现在貌似用的java.ut
     * @param source
     * @return
     */
    public static String springDecodeString(String source){
        return new String(Base64Utils.decodeFromString(source));
    }

//    public static BlogSearchDTO blogSearchDTODecode(BlogSearchDTO blogSearchDTO) {
//        if (blogSearchDTO.getBlogTitle() != null) {
//            String decodeBlogTitle = MyBase64Utils.springDecodeString(blogSearchDTO.getBlogTitle());
//            blogSearchDTO.setBlogTitle(decodeBlogTitle);
//        }
//        return blogSearchDTO;
//    }
//
//    public static BlogSearchDTO blogSearchDTOEncode(BlogSearchDTO blogSearchDTO) throws UnsupportedEncodingException {
//        if (blogSearchDTO.getBlogTitle() != null) {
//            String encodeBlogTitle = MyBase64Utils.springEncodeString(blogSearchDTO.getBlogTitle());
//            blogSearchDTO.setBlogTitle(encodeBlogTitle);
//        }
//        return blogSearchDTO;
//    }
//





}
