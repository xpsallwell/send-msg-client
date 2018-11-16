package com.yyh.sms;

import com.yyh.sms.bean.TemplateType;
import com.yyh.sms.exception.SendMsgException;
import com.yyh.sms.handler.SendMsgHandler;

import java.util.Map;

/**
 * 发送消息客户端
 * Created by xiongps on 2017/12/22.
 */
public interface SendMsgService {

    /**
     * 发送信息
     * @param sender 发送者
     * @param receiver 接收者
     * @param content 发送内容
     * @return String
     */
    String sendMsg(String sender,String receiver,String content) throws SendMsgException;

    /**
     * 根据配置的模板编码 获取内容发送
     * @param sender
     * @param receiver
     * @param templateCode
     * @param params
     * @return String
     */
    String sendMsg(String sender,String receiver,String templateCode,Map<String,Object> params) throws SendMsgException;

    /**
     * 根据模板内容，及模板类型 获取内容发送
     * @param receiver 接收者
     * @param templateContent
     * @param templateType
     * @param params
     * @return String
     */
    String sendMsg(String sender, String receiver, String templateContent,
                 TemplateType templateType, Map<String,Object> params) throws SendMsgException;

    void sendMsg(String sender, String receiver, SendMsgHandler handler) throws SendMsgException;

    void sendMsg(String sender, String receiver,String templateCode,Map<String,Object> params, SendMsgHandler handler) throws SendMsgException;

    void sendMsg(String sender, String receiver,String templateCode,Map<String,Object> params,TemplateType templateType, SendMsgHandler handler) throws SendMsgException;

}
