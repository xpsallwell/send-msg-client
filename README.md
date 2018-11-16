
# send-msg-client
对创瑞短信平台接口对接客户端，针对接口编程的方式使得其适应不同的场景。可以直接引入到自己的工程，实现对应的接口即可完成工作。方便快捷！

 使用示例：
 1.在配置文件中配置相关的参数
  #创瑞短信平台接口
sms-url=http://web.cr6868.com/asmx/smsservice.aspx?
sms-account=username
sms-password=password
#通配符号，可支持多种符号，默认是$_$，例：您正在修改支付密码，验证码：$verifyCode$，请尽快完成验证。如非本人操作，请及时联系客户服务。
sms-special-symbol=$_$,|#|_|#|,|_|

2.在spring配置文件中引入
   <!-- 短信发送 -->
    <bean id="smsSendService" class="com.yyh.sms.impl.SendSMSServiceImpl">
        <property name="smsConfig" >
            <bean class="com.yyh.sms.config.SMSConfig">
                <property name="smsUrl" value="${sms-url}"></property>
                <property name="account" value="${sms-account}"></property>
                <property name="password" value="${sms-password}"></property>
                <property name="specialSymbol" value="${sms-special-symbol}"></property>
            </bean>
        </property>
        <property name="handler" ref="smsSendHandlerServiceImpl"></property>
    </bean>
    
 3.上面的handler是
 package com.yaoyaohao.health.user.service.impl;

import com.yaoyaohao.health.user.dao.SmsTemplateDao;
import com.yaoyaohao.health.user.entity.SmsBusi;
import com.yaoyaohao.health.user.entity.SmsTemplate;
import com.yyh.sms.bean.SendMsgBean;
import com.yyh.sms.bean.TemplateType;
import com.yyh.sms.config.SendMsgConfig;
import com.yyh.sms.exception.SendMsgException;
import com.yyh.sms.handler.impl.AbstractSendMsgHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiongps on 2017/12/22.
 */
@Service
public class SmsSendHandlerServiceImpl extends AbstractSendMsgHandler {

    @Autowired
    private SmsTemplateDao smsTemplateDao;

    @Override
    public String getSendContent(TemplateType templateType, String templateCode, Map<String, Object> params,
                                 SendMsgConfig sendMsgConfig, SendMsgBean sendMsgBean)
            throws SendMsgException {

        SmsTemplate smsTemplate = smsTemplateDao.selectSmsTemplateByCode(templateCode);
        if(null == smsTemplate) {
            throw new SendMsgException("B0001","模板不存在");
        }
        String smsContent = super.handlerContent(smsTemplate.getTemplateContents(),templateType, params, sendMsgConfig);
        Map<String,Object> extendMap = new HashMap<>();
        extendMap.put("smsTemplate",smsTemplate);
        extendMap.put("smsContent",smsContent);
        sendMsgBean.setExtendMap(extendMap);
        return smsContent;
    }

    @Override
    public boolean beforeHandler(SendMsgBean sendMsgBean, SendMsgConfig sendMsgConfig) throws SendMsgException {
        Map<String,Object> extendMap = sendMsgBean.getExtendMap();
        SmsTemplate smsTemplate = null;
        if(extendMap.containsKey("smsTemplate")) {
            smsTemplate = (SmsTemplate)extendMap.get("smsTemplate");
        }
        //校验是否在黑名单中
        Boolean isExists = smsTemplateDao.selectIsInBlackListByPhoneNo(sendMsgBean.getReceiver());
        if(isExists){
            throw new SendMsgException("B0002","接收人是黑名单客户");
        }

        return false;//为true继续发送
    }


    @Override
    public void afterHandler(SendMsgBean sendMsgBean, SendMsgConfig sendMsgConfig, Object returnMsg)
            throws SendMsgException {

        //发送完成后处理
        Map<String,Object> extendMap = sendMsgBean.getExtendMap();

        //1.插入业务结果表
        SmsBusi messageInfo = new SmsBusi();
        messageInfo.setChanId("00");
        Calendar nowDate = Calendar.getInstance();
        int month = nowDate.get(Calendar.MONTH) + 1;
        int day = nowDate.get(Calendar.DAY_OF_MONTH);
        messageInfo.setCreateTime(nowDate.getTime());
        messageInfo.setDay(Short.parseShort(String.valueOf(day)));
        messageInfo.setMonth(Short.parseShort(String.valueOf(month)));
        messageInfo.setDealState("15");
        messageInfo.setSmsTypeCode("00");
        messageInfo.setInModeCode("0"); //接口接入
        messageInfo.setNoticeContent(String.valueOf(extendMap.get("smsContent")));
        messageInfo.setNoticeContentType("0"); //现在只能0   根据模板发送
        messageInfo.setPriorityLevel(Short.parseShort("1000"));
        messageInfo.setSendNumber("111");
        messageInfo.setRecvObject(sendMsgBean.getReceiver());
        messageInfo.setTryTimes(Short.parseShort("0"));
        messageInfo.setSmsNoticeId(BigDecimal.valueOf(System.currentTimeMillis()));
        messageInfo.setPartitionId(Short.valueOf("1"));
        messageInfo.setSmsNetTag("0"); // 回执暂时没用
        messageInfo.setSmsCode(sendMsgBean.getTemplateCode());
        messageInfo.setReferedCount(0);
        messageInfo.setForceReferCount(1);
        messageInfo.setReferTime(new Date());
        messageInfo.setSmsKindCode("01");
        if(returnMsg != null) {
            //0,2017122215592306537359081,0,1,0,提交成功
            if(returnMsg instanceof String) {
                if(String.valueOf(returnMsg).contains(",")) {

                }
                messageInfo.setRspCode(String.valueOf(returnMsg).split(",")[0]);
            }
            messageInfo.setRspDesc(String.valueOf(returnMsg));
        }
        smsTemplateDao.insertSmsBusi(messageInfo);

    }




}

  
 4.编写统一的一个工具类
 package com.yaoyaohao.health.user.util;

import com.yaoyaohao.framework.redis.ShardedJedisClient;
import com.yaoyaohao.framework.redis.impl.ShardedJedisClientImpl;
import com.yaoyaohao.health.common.enums.Constants;
import com.yaoyaohao.health.common.exception.DefaultExcepComponent;
import com.yaoyaohao.health.common.exception.ServiceBusinessException;
import com.yaoyaohao.health.common.springmvc.SpringContextHolder;
import com.yyh.sms.SendMsgService;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiongps on 2017/12/22.
 */
public abstract class SMSSendMsgUtil {

    private static int DEFAULT_VERIFICATION_CODE_LENGTH = 6;


    private static SendMsgService sendMsgService =
            (SendMsgService) SpringContextHolder.getBean("smsSendService");

    private static ShardedJedisClient redisClient = SpringContextHolder.getBean(ShardedJedisClientImpl.class);

    public static void sendSMS(String receiver, String tempCode, Map<String,Object> params){
        sendMsgService.sendMsg(null,receiver,tempCode,params);
    }

    /**
     * 发送验证码，默认长度6位，默认过期时间5分钟
     * @param receiver
     * @param tempCode
     */
    public static void sendVerificationCode(String receiver, String tempCode){
        sendVerificationCode(receiver,tempCode,new HashMap<String, Object>());//5分钟
    }

    /**
     * 发送验证码，默认长度6位，默认过期时间5分钟
     * @param receiver
     * @param tempCode
     */
    public static void sendVerificationCode(String receiver, String tempCode, Map<String,Object> extendMap){
        sendVerificationCode(receiver,tempCode,extendMap,DEFAULT_VERIFICATION_CODE_LENGTH,2*60);//2分钟
    }

    public static void sendVerificationCode(String receiver, String tempCode, Map<String,Object> params, int vcLen, int expireSeconds){
        //生成验证码
        String key = MessageFormat.format(Constants.VERIFICATION_CODE_REDIS_KEY,receiver),vc = redisClient.get(key);
        if(vc==null) {
            vc = createRandomVcode(vcLen);
        }
        redisClient.set(key,vc,expireSeconds);
        params.put("verifyCode",vc);
        sendMsgService.sendMsg(null,receiver,tempCode,params);
    }

    public static boolean checkVerificationCode(String receiver, String verCode)
        throws ServiceBusinessException {
        if(StringUtils.isEmpty(verCode)) {
            return false;
        }
        //获取验证码
        String vc =redisClient.get(MessageFormat.format(Constants.VERIFICATION_CODE_REDIS_KEY,receiver));
        if(StringUtils.isEmpty(vc)) {
            throw new ServiceBusinessException(DefaultExcepComponent.VERIFICATION_CODE_EXPIRE);
        }
        return verCode.equals(vc);
    }

    private static String createRandomVcode(int num) {
        //验证码生成
        String vcode = "";
        for (int i = 0; i < num; i++) {
            int temp = (int) (Math.random() * 9);
            while (temp == 0 || temp == 1) {
                temp = (int) (Math.random() * 9);
            }
            vcode = vcode + temp;
        }
        return vcode;
    }
}

5.在代码中使用
 @Override
    public DataResponse<IData> sendVerifyCode(HashMapDataRequest params) {
        DataResponse<IData> response = new DataResponse<>();
        response.getHeader().setToSysId("surface");
        String phoneNo = params.getString("phoneNo");
        boolean isCheckUserExists = params.getBoolean("isCheckUserExists");
        try {
            if(isCheckUserExists) {
                UserInfo userInfo = userInfoDao.selectByLoginAccount(phoneNo);
                if(userInfo == null) {
                    throw new ServiceBusinessException(UserExcepComponent.NOT_EXISTS_USER_INFO);
                }
            }
            SMSSendMsgUtil.sendVerificationCode(phoneNo,"ZC0001");//ZC0001为配置的模板编号且唯一
        }catch (ServiceBusinessException se) {
            response.setExceptionComp(se);
        }
        return response;
    }
    
    
