package com.lcshidai.lc.model.account;

/**
 * Created by RandyZhang on 2016/10/11.
 */
public class LicaiUcInfoData {

    /**
     * id : 23848
     * token : b510126a38f0cf69b6a982d0173330d3
     * expire_time : 1486585812
     * pwd : 528de6e662373f845ce0396c1aa99d56
     * auth : 7436e345e67f4d6a0466fc65a6228699
     */

    private String id;
    private String token;
    private String expire_time;
    private String pwd;
    private String auth;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
