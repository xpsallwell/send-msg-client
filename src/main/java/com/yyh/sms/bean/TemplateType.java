package com.yyh.sms.bean;

/**
 * Created by xiongps on 2017/12/22.
 */
public enum TemplateType {
    DEFAULT,//普通文件key占位，例如：测试验证码：$verifyCode$，这是验证码！！中的$verifyCode$
    MESSAGE_FORMAT,//使用java.text.MessageFormat进行格式化，{0},{1}....等等。注意是从0开始
    FREEMARKER;//freemarker来解析，暂时不支持
}
