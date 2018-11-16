package com.yyh.sms.config;

import java.util.Properties;

/**
 * Created by xiongps on 2017/12/22.
 */
public abstract class SendMsgConfig {

    private Properties properties;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
