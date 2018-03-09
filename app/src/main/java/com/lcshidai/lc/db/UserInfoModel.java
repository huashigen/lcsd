package com.lcshidai.lc.db;

import java.io.Serializable;

public class UserInfoModel implements Serializable {
    public String is_paypwd_edit;// 1,
    public String uid;// 119,
    public String is_change_uname;// 0,
    public String sex;// 0,
    // ava;// {
    public String url_s100;// http;////test.yrz.cn/data/uploads/img/2014/02/17/s100_5301c55535fab.jpg,
    public String url_s50;// http;////test.yrz.cn/data/uploads/img/2014/02/17/s50_5301c55535fab.jpg,
    public String url_s300;// http;////test.yrz.cn/data/uploads/img/2014/02/17/s300_5301c55535fab.jpg,
    public String url;// http;////test.yrz.cn/data/uploads/img/2014/02/17/5301c55535fab.jpg,
    public String url_s700;// http;////test.yrz.cn/data/uploads/img/2014/02/17/s700_5301c55535fab.jpg
    // },
    public String person_id;// 321322198912165665,
    public String is_active;// 1,
    public String uname;// mao,
    public String mi_id;// 1,
    public String is_id_auth;// 1,
    public String safe_level;// 80,
    public String dept_id;// 43,
    public String identity_no;// I101,I102,I103,I104,
    public String is_email_auth;// 0,
    public String vip_group_id;// ,1,,
    public String real_name;// 毛迎兰,
    public String is_mobile_auth;// 1,
    public String before_logintime;// 1392693365,
    public String mobile;// 18994374723
    public String is_set_uname = "";
    public String is_all;//是否完成新手指引 0 未完成 1 已完成
    public String is_newbie;    //是否是新客0-不是 	1-是新客
    public String is_paypwd_mobile_set; //是否设置手机支付密码
    public String is_recharged;     //是否充值用户
    public boolean user_is_qfx;      //是否yq付用户
    public String uc_id;// 用户中新id
    public String is_binding_bank;     //是否充值用户
}
