package com.yyh.sms.config;

import java.util.Properties;

/**
 * Created by xiongps on 2017/12/22.
 */
public class SMSConfig extends SendMsgConfig{

    /**发送短信的地址*/
    private String smsUrl;
    /**发送短信的用户名*/
    private String account;
    /**发送短信的密码*/
    private String password;

    private String specialSymbol;

    public String getSmsUrl() {
        return smsUrl;
    }

    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.smsUrl = properties.getProperty("sms-url");
        this.account =  properties.getProperty("sms-account");
        this.password =  properties.getProperty("sms-password");
        this.specialSymbol = properties.getProperty("sms-special-symbol");
    }

    public String getSpecialSymbol() {
        return specialSymbol;
    }

    public void setSpecialSymbol(String specialSymbol) {
        this.specialSymbol = specialSymbol;
    }
}
