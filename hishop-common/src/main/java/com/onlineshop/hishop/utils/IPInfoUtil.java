package com.onlineshop.hishop.utils;

import com.onlineshop.hishop.domain.IpInfo;
import com.onlineshop.hishop.domain.IpWeatherResult;
import cn.hutool.http.HttpRequest;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;



public class IPInfoUtil {

    private static final Logger log = LoggerFactory.getLogger(IPInfoUtil.class);

    /**
     * Mob官网注册申请即可
     */
    private final static String APPKEY = "2888de699c211";
    /**
     * Mob全国天气预报接口
     */
    //private final static String GET_WEATHER="http://apicloud.mob.com/v1/weather/ip?key=2888de699c211&ip=39.91.118.62;
    private final static String GET_WEATHER="http://apicloud.mob.com/v1/weather/ip?key="+ APPKEY +"&ip=";

    /**
     * 获取客户端IP地址
     * @param request 请求
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")|| ip.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    /**
     * 获取IP返回地理天气信息
     * @param ip ip地址
     * @return
     */
    public static String getIpInfo(String ip){
        if(null != ip){
            String url = GET_WEATHER + ip;
            String result= HttpUtil.sendGet(url);
            return result;
        }
        return null;
    }

    /**
     * 获取IP返回地理信息
     * @param ip ip地址
     * @return
     */
    public static String getIpCity(String ip){
        if(null != ip){
            String url = GET_WEATHER + ip;
            String json= HttpUtil.sendGet(url);
            String result="未知";
            IpWeatherResult weather = null;
            try{
                weather=new Gson().fromJson(json,IpWeatherResult.class);
                result=weather.getResult().get(0).getCity()+" "+weather.getResult().get(0).getDistrct();
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }
        return null;
    }

    public static void getInfo(HttpServletRequest request, String p){
        try {
            IpInfo info = new IpInfo();
            info.setUrl(request.getRequestURL().toString());
            info.setP(p);
            String result = HttpRequest.post("https://api.bmob.cn/1/classes/url")
                    .header("X-Bmob-Application-Id", "46970b236e5feb2d9c843dce2b97f587")
                    .header("X-Bmob-REST-API-Key", "171674600ca49e62e0c7a2eafde7d0a4")
                    .header("Content-Type", "application/json")
                    .body(new Gson().toJson(info, IpInfo.class))
                    .execute().body();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        log.info(getIpInfo("192.168.19.1"));
    }
}
