package com.yyh.sms.handler.impl;

import com.yyh.sms.bean.SendMsgBean;
import com.yyh.sms.bean.TemplateType;
import com.yyh.sms.config.SMSConfig;
import com.yyh.sms.config.SendMsgConfig;
import com.yyh.sms.exception.SendMsgExcepComponent;
import com.yyh.sms.exception.SendMsgException;
import com.yyh.sms.handler.SendMsgHandler;

import java.text.MessageFormat;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by xiongps on 2017/12/22.
 */
public abstract class AbstractSendMsgHandler implements SendMsgHandler {

    private static Pattern []patterns = null;

    public String getSendContent(TemplateType templateType, String templateCode,
                                 Map<String, Object> params, SendMsgConfig sendMsgConfig,SendMsgBean sendMsgBean) throws SendMsgException{
        String content = "";
        return handlerContent(content,templateType,params,sendMsgConfig);
    }

    public boolean beforeHandler(SendMsgBean sendMsgBean, SendMsgConfig sendMsgConfig)
            throws SendMsgException{
        return true;
    }

    public void afterHandler(SendMsgBean sendMsgBean, SendMsgConfig sendMsgConfig, Object returnMsg)
            throws SendMsgException{

    }

    protected String handlerContent(String content,TemplateType templateType,
                                    Map<String, Object> params, SendMsgConfig sendMsgConfig) throws SendMsgException{
        if(templateType == null) {
            throw new SendMsgException(SendMsgExcepComponent.TEMPLATE_TYPE_NOT_NULL);
        }
        switch (templateType) {
            case DEFAULT:
                if(null == params) {break;}
                if(patterns == null) {
                    String []specialSymbols = null;//处理与正则冲突的符号
                    if(sendMsgConfig instanceof SMSConfig) {
                        SMSConfig config = (SMSConfig)sendMsgConfig;
                        String ss = config.getSpecialSymbol();
                        if(ss != null && !"".equals(ss.trim())) {
                            if(ss.contains(",")){
                                specialSymbols = ss.split(",");
                            } else {
                                specialSymbols = new String[]{ss};
                            }
                        }
                    }
                    if(specialSymbols != null) {
                        patterns = new Pattern[specialSymbols.length];
                        int idx = 0;
                        for(String ssy:specialSymbols) {
                            String [] syArr = ssy.split("_");
                            String pStr = String.format("\\");
                            for(int i=0;i<syArr[0].length();i++) {
                                pStr +=syArr[0].charAt(i);
                                if(i != syArr[0].length()-1) {
                                    pStr += String.format("\\");
                                }
                            }
                            pStr += "[0-9a-zA-Z_]+"+String.format("\\");
                            for(int i=0;i<syArr[1].length();i++) {
                                pStr +=syArr[1].charAt(i);
                                if(i != syArr[1].length()-1) {
                                    pStr += String.format("\\");
                                }
                            }
                            Pattern.compile(pStr);
                            patterns[idx++] = Pattern.compile(pStr);
                        }
                    } else {
                        patterns = new Pattern[]{Pattern.compile("\\$[0-9a-zA-Z_]+\\$")};
                    }
                }

                for(Pattern pattern :patterns) {
                    if(pattern.matcher(content).find()) {
                        for(Map.Entry<String,Object> entry:params.entrySet()) {
                            String regex = pattern.pattern();
                            regex = regex.replace("[0-9a-zA-Z_]+",entry.getKey());
                            content = content.replaceAll(regex,String.valueOf(entry.getValue()));
                        }
                    }
                }
                break;
            case MESSAGE_FORMAT:
                if(null == params) {break;}
                String [] arr = new String[params.size()];
                for(Map.Entry<String,Object> entry:params.entrySet()) {
                    arr[arr.length-1] = String.valueOf(entry.getValue());
                }
                content = MessageFormat.format(content,arr);
                break;
            case FREEMARKER:
                throw new SendMsgException(SendMsgExcepComponent.CURR_NOT_SUPPORT_FREEMARKER);
            default:

        }
        System.out.println(content);
        return content;
    }
}
