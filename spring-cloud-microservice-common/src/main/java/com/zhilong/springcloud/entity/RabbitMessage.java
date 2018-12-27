package com.zhilong.springcloud.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RabbitMessage implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMessage.class);

    private List<Class<?>> paramTypes = new ArrayList<Class<?>>();
    private String exchange;

    private List<Object> params = new ArrayList<Object>();

    private String routeKey;

    public RabbitMessage(){}


    @SuppressWarnings("rawtypes")
    public RabbitMessage(String exchange,String routeKey,Object...params)
    {
        this.params = Arrays.asList(params);
        this.exchange=exchange;
        this.routeKey=routeKey;
        int len = params.length;
        for(int i=0;i<len;i++)
        this.paramTypes.add(params[i].getClass());
    }

    public static byte[] getSerialBytes(Serializable obj)
    {
        byte[] res=new byte[0];
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();
            res = baos.toByteArray();
        } catch (IOException e) {
            logger.error("",e);
        }
        return res;
    }

    public static Object getObjectFromBytes(byte[] objBytes) throws Exception {
        if (objBytes == null || objBytes.length == 0) {
            return null;
        }
        ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
        ObjectInputStream oi = new ObjectInputStream(bi);

        return oi.readObject();
    }


    public String getRouteKey() {
        return routeKey;
    }



    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }



    public List<Class<?>> getParamTypes() {
        return paramTypes;
    }

    public List<Object> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return "RabbitMessage{" +
                "paramTypes=" + paramTypes +
                ", exchange='" + exchange + '\'' +
                ", params=" + params +
                ", routeKey='" + routeKey + '\'' +
                '}';
    }
}
