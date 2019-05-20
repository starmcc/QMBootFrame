package com.qm.frame.qmsecurity.utils;

import com.qm.frame.qmsecurity.config.QmSecurityConstants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化工具
 * @author 浅梦
 */
public class QmSerializeTools {

    /**
     * 序列化
     * @param obj
     * @return
     * @throws Exception
     */
    public static String serializeToString(Object obj) throws Exception {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(obj);
        //此处只能是ISO-8859-1,但是不会影响中文使用
        return byteOut.toString(QmSecurityConstants.SERIALIZE_CHARSET);
    }

    /**
     * 反序列化
     * @param str
     * @return
     * @throws Exception
     */
    public static Object deserializeToObject(String str) throws Exception {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(str.getBytes(QmSecurityConstants.SERIALIZE_CHARSET));
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        return objIn.readObject();
    }
}