package com.yyh.sms;

import com.yyh.sms.config.SMSConfig;
import com.yyh.sms.handler.impl.DefaultSendMsgHandler;
import com.yyh.sms.impl.SendSMSServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by xiongps on 2017/12/22.
 */
public class SMSSendTest {

    private SendMsgService sendMsgService;
    @Before
    public void init(){
        /**
        System.out.println("开始测试了");
        InputStream is =SMSSendTest.class.getClassLoader().getResourceAsStream("send-sms.properties");
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SMSConfig smsConfig = new SMSConfig();
        smsConfig.setProperties(properties);
        System.out.println(smsConfig.getAccount());
        sendMsgService = new SendSMSServiceImpl(smsConfig, new DefaultSendMsgHandler());
        */
    }

    @Test
    public void testSmsSend(){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("verifyCode","9990");
        params.put("verifyCode1","9ee91");
        params.put("verifyCode2","9ee922");
        final String content = "您报名的验证码为$verifyCode$，请尽快填写完成报名|#|verifyCode2|#|。如需帮助请致电客|verifyCode1|服专线400-006-5151";
        // sendMsgService.sendMsg(null,"18651671163","t02",params,new DefaultSendMsgHandler(content));

    }
}
