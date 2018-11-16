package com.yyh.sms.handler;

import com.yyh.sms.bean.SendMsgBean;
import com.yyh.sms.bean.TemplateType;
import com.yyh.sms.config.SendMsgConfig;
import com.yyh.sms.exception.SendMsgException;

import java.util.Map;

/**
 * Created by xiongps on 2017/12/22.
 */
public interface SendMsgHandler {

    /**
     * 根据条件获取发送的信息内容
     * @param templateType
     * @param templateCode
     * @param params
     * @param sendMsgConfig
     * @return
     */
    String getSendContent(TemplateType templateType,String templateCode,
                          Map<String,Object> params, SendMsgConfig sendMsgConfig,SendMsgBean sendMsgBean) throws SendMsgException;

    /**
     * 发送前调用，如果有异常不发送
     * @param sendMsgBean
     * @param sendMsgConfig
     * @return 如果返回false则不发送
     */
    boolean beforeHandler(SendMsgBean sendMsgBean, SendMsgConfig sendMsgConfig) throws SendMsgException;

    /**
     * 发送后调用
     * @param sendMsgBean
     * @param sendMsgConfig
     * @param returnMsg
     */
    void afterHandler( SendMsgBean sendMsgBean, SendMsgConfig sendMsgConfig,Object returnMsg) throws SendMsgException;


}
