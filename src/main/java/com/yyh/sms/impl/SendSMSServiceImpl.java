package com.yyh.sms.impl;

import com.yyh.sms.SendMsgService;
import com.yyh.sms.bean.SendMsgBean;
import com.yyh.sms.bean.TemplateType;
import com.yyh.sms.config.SMSConfig;
import com.yyh.sms.exception.SendMsgExcepComponent;
import com.yyh.sms.exception.SendMsgException;
import com.yyh.sms.handler.SendMsgHandler;
import com.yyh.sms.util.HttpSendUtil;

import java.util.Map;

/**
 * Created by xiongps on 2017/12/22.
 */
public class SendSMSServiceImpl implements SendMsgService {

    private SMSConfig smsConfig;

    private SendMsgHandler handler;

    public  SendSMSServiceImpl(){}

    public  SendSMSServiceImpl(SMSConfig smsConfig,SendMsgHandler handler) {
        this.smsConfig = smsConfig;
        this.handler = handler;
    }
    public String sendMsg(String sender, String receiver, String content) {
        SendMsgBean sendMsgBean = new SendMsgBean(sender,receiver,content);
        return send(sendMsgBean,handler);
    }

    public String sendMsg(String sender, String receiver, String templateCode, Map<String, Object> params) {
        return sendMsg(sender,receiver,templateCode,TemplateType.DEFAULT,params);
    }

    public String sendMsg(String sender, String receiver, String templateCode, TemplateType templateType, Map<String, Object> params) {
        if(receiver == null || "".equals(receiver)) {
            throw new SendMsgException(SendMsgExcepComponent.SEND_RECEIVER_NOT_EMPTY);
        }
        if(templateCode == null || "".equals(templateCode)) {
            throw new SendMsgException(SendMsgExcepComponent.SEND_TEMPLATE_CODE_NOT_EMPTY);
        }
        if(templateType == null){templateType = TemplateType.DEFAULT;}
        SendMsgBean sendMsgBean = new SendMsgBean(sender,receiver);
        String content = handler.getSendContent(templateType,templateCode,params,smsConfig,sendMsgBean);
        if(content == null || "".equals(content)) {
            throw new SendMsgException(SendMsgExcepComponent.SEND_CONTENT_NOT_EMPTY);
        }
        sendMsgBean.setContent(content);
        sendMsgBean.setTemplateCode(templateCode);
        sendMsgBean.setParams(params);
        sendMsgBean.setTemplateType(templateType);
        return send(sendMsgBean,handler);
    }

    public void sendMsg(String sender, String receiver,SendMsgHandler handler) throws SendMsgException {
        sendMsg(sender,receiver,null,null,TemplateType.DEFAULT,handler);
    }

    public void sendMsg(String sender, String receiver, String templateCode,Map<String,Object> params, SendMsgHandler handler) throws SendMsgException {
        sendMsg(sender,receiver,templateCode,params,TemplateType.DEFAULT,handler);
    }

    public void sendMsg(String sender, String receiver, String templateCode,Map<String,Object> params, TemplateType templateType, SendMsgHandler handler) throws SendMsgException {
        if(receiver == null || "".equals(receiver)) {
            throw new SendMsgException(SendMsgExcepComponent.SEND_RECEIVER_NOT_EMPTY);
        }
        if(templateCode == null || "".equals(templateCode)) {
            throw new SendMsgException(SendMsgExcepComponent.SEND_TEMPLATE_CODE_NOT_EMPTY);
        }
        if(templateType == null){templateType = TemplateType.DEFAULT;}
        SendMsgBean sendMsgBean = new SendMsgBean(sender,receiver);
        String content = handler.getSendContent(templateType,templateCode,params,smsConfig,sendMsgBean);
        if(content == null || "".equals(content)) {
            throw new SendMsgException(SendMsgExcepComponent.SEND_CONTENT_NOT_EMPTY);
        }
        sendMsgBean.setContent(content);
        sendMsgBean.setTemplateCode(templateCode);
        sendMsgBean.setParams(params);
        sendMsgBean.setTemplateType(templateType);
        send(sendMsgBean,handler);
    }

    private String send(SendMsgBean sendMsgBean,SendMsgHandler handler){
        try {
            if(null == sendMsgBean.getReceiver()
                    || "".equals(sendMsgBean.getReceiver())
                    || null == sendMsgBean.getContent()
                    || "".equals(sendMsgBean.getContent())) {
                throw new SendMsgException(SendMsgExcepComponent.SEND_RECEIVER_CONTENT_NOT_EMPTY);
            }
            boolean isContinue = true;String ret = null;
            if(null != handler) {
                isContinue = handler.beforeHandler(sendMsgBean,smsConfig);
            }
            if(isContinue) {
                ret = HttpSendUtil.sendCR(smsConfig.getSmsUrl(), smsConfig.getAccount(), smsConfig.getPassword(),
                        sendMsgBean.getReceiver(), sendMsgBean.getContent(), true, "", "");
            }
            if(null != handler) {
                handler.afterHandler(sendMsgBean,smsConfig,ret==null?null:new String(ret));
            }
            return ret;
        } catch (Exception e) {
            throw new SendMsgException(SendMsgExcepComponent.SEND_SMS_ERROR,e);
        }
    }

    public SMSConfig getSmsConfig() {
        return smsConfig;
    }

    public void setSmsConfig(SMSConfig smsConfig) {
        this.smsConfig = smsConfig;
    }

    public SendMsgHandler getHandler() {
        return handler;
    }

    public void setHandler(SendMsgHandler handler) {
        this.handler = handler;
    }
}
