package com.onlineshop.hishop.service.impl;

import com.onlineshop.hishop.pojo.TbOrder;
import com.onlineshop.hishop.utils.SendMailThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
@PropertySource("classpath:conf/admin.yml")
public class EmailUtil {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${EMAIL_USERNAME}")
    private String sender;
    @Value("${ADMIN_EMAIL}")
    private String ADMIN_EMAIL;
    @Value("${imgPath}")
    private String imgPath;

    public boolean SendDeliverEmail(TbOrder order) {
        MimeMessage message=javaMailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
//            String serverpath1= ResourceUtils.getURL("classpath:conf/").getPath().replace("%20"," ").replace('/', '\\');
//            String path=serverpath1.substring(1);//从路径字符串中取出工程路径
//            System.out.println(path);
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(sender);
            helper.setTo(ADMIN_EMAIL);
            helper.setSubject("HiShop商城发货提醒通知");
//            String imgPath= "http://hishop.front.com:88/user/img/logo.png";
//            String imgId = "logo";
            //添加图片
//            FileSystemResource file = new FileSystemResource(new File(logoimgPath));
            String content = "\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;background-color: #ebedf0;font-family:&#39;Alright Sans LP&#39;, &#39;Avenir Next&#39;, &#39;Helvetica Neue&#39;, Helvetica, Arial, &#39;PingFang SC&#39;, &#39;Source Han Sans SC&#39;, &#39;Hiragino Sans GB&#39;, &#39;Microsoft YaHei&#39;, &#39;WenQuanYi MicroHei&#39;, sans-serif;\">\n" +
                    "  <tr>\n" +
                    "    <td>\n" +
                    "      <table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"640\">\n" +
                    "        <tr>\n" +
                    "          <td style=\"height:20px;\"></td>\n" +
                    "        </tr>\n" +
                    "        \n" +
                    "        <tr>\n" +
                    "          <td height=\"10\"></td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td>\n" +
                    "            <table cellpadding=\"0\" cellspacing=\"0\" width=\"640\">\n" +
                    "              <tr style=\"line-height: 40px;\">\n" +
                    "                <td width='80' style=\"padding-left: 290px;\">\n" +
                    "                  <a href=\"#\">\n" +
                    "                     <img src=\""+imgPath+"\" width=\"54\"  />\n" +
                    "                  </a>\n" +
                    "                </td>\n" +
                    "              </tr>\n" +
                    "            </table>\n" +
                    "          </td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td style=\"background-color: #fff;border-radius:6px;padding:40px 40px 0;\">\n" +
                    "            <table>\n" +
                    "              <tr height=\"40\">\n" +
                    "                <td style=\"padding-left:25px;padding-right:25px;font-size:18px;font-family:&#39;微软雅黑&#39;,&#39;黑体&#39;,arial;\">\n" +
                    "                  尊敬的HiShop商城管理员您好,\n" +
                    "                </td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"15\">\n" +
                    "                <td></td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"30\">\n" +
                    "                <td style=\"padding-left:55px;padding-right:55px;font-family:&#39;微软雅黑&#39;,&#39;黑体&#39;,arial;font-size:14px;\">\n" +
                    "                  您有一笔订单号为"+order.getOrderId()+"的订单已超过3天未发货，请尽快发货。\n" +
                    "                </td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"10\">\n" +
                    "                <td></td>\n" +
                    "              </tr>\n" +
                    "        <tr>\n" +
                    "          <td style=\"height:40px;\"></td>\n" +
                    "        </tr>\n" +
                    "      </table>\n" +
                    "    </td>\n" +
                    "  </tr>\n" +
                    "</table>";
            helper.setText(content,true);
//            helper.addInline(logoimgId,file);
            SendMailThread.sendEmail(javaMailSender,message);
//            javaMailSender.send(message);
            return true;
        }catch (Exception e){
            System.out.println(e.toString());
            return false;
        }

    }

}
