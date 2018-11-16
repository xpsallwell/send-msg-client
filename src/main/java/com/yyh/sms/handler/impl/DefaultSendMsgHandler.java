package com.yyh.sms.handler.impl;

import com.yyh.sms.bean.SendMsgBean;
import com.yyh.sms.bean.TemplateType;
import com.yyh.sms.config.SendMsgConfig;
import com.yyh.sms.exception.SendMsgException;

import java.util.Map;

/**
 * Created by xiongps on 2017/12/22.
 */
public class DefaultSendMsgHandler extends AbstractSendMsgHandler {

    private String content;

    public DefaultSendMsgHandler(){}

    public DefaultSendMsgHandler(String content){
        this.content = content;
    }

    public String getSendContent(TemplateType templateType, String templateCode,
                                 Map<String, Object> params, SendMsgConfig sendMsgConfig,SendMsgBean sendMsgBean) throws SendMsgException{
        return super.handlerContent(this.content,templateType,params,sendMsgConfig);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
