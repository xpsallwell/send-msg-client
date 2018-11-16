package com.yyh.sms.bean;

import java.util.Map;
import java.util.Properties;

/**
 * Created by xiongps on 2017/12/22.
 */
public class SendMsgBean {

    private String sender;

    private String receiver;

    private String content;

    private String templateCode;

    private Map<String,Object> params;

    private TemplateType templateType;

    //用于传其它扩展参数
    private Map<String,Object> extendMap;

    public SendMsgBean(){}
    public SendMsgBean(String sender,String receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }
    public SendMsgBean(String sender,String receiver,String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, Object> getExtendMap() {
        return extendMap;
    }

    public void setExtendMap(Map<String, Object> extendMap) {
        this.extendMap = extendMap;
    }
}
