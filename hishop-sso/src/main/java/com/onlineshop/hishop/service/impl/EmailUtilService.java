package com.onlineshop.hishop.service.impl;


import com.onlineshop.hishop.utils.SendMailThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@PropertySource("classpath:conf/sso.yml")
public class EmailUtilService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${EMAIL_USERNAME}")
    private String sender;

    @Value("${ServerPath}")
    private String ServerPath;

    @Value("${ActiveReqUrl}")
    private String ActiveReqUrl;

    @Value("${imgPath}")
    private String imgPath;

    @Value("${CODE_PRX}")
    private String CODE_PEX;

    public boolean SendRegisterEmail(String recipient, String username) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
//            String serverpath1= ResourceUtils.getURL("classpath:conf/").getPath().replace("%20"," ").replace('/', '\\');
//            String path=serverpath1.substring(1);//从路径字符串中取出工程路径
//            System.out.println(path);
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(recipient);
            helper.setSubject("HiShop商城用户激活通知");
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
                    "                     <img src=\"" + imgPath + "\" width=\"54\"  />\n" +
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
                    "                  尊敬的" + recipient + "您好,\n" +
                    "                </td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"15\">\n" +
                    "                <td></td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"30\">\n" +
                    "                <td style=\"padding-left:55px;padding-right:55px;font-family:&#39;微软雅黑&#39;,&#39;黑体&#39;,arial;font-size:14px;\">\n" +
                    "                  感谢您选择HiShop商城。\n" +
                    "                </td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"30\">\n" +
                    "                <td style=\"padding-left:55px;padding-right:55px;font-family:&#39;微软雅黑&#39;,&#39;黑体&#39;,arial;font-size:14px;\">\n" +
                    "                  请点击以下链接进行邮箱验证，以便开始使用您的HiShop账户：\n" +
                    "                </td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"60\">\n" +
                    "                <td style=\"padding-left:55px;padding-right:55px;font-family:&#39;微软雅黑&#39;,&#39;黑体&#39;,arial;font-size:14px;\">\n" +
                    "                  <a href=\"" + ServerPath + ActiveReqUrl + "?idName=" + username + "\" target=\"_blank\" style=\"display: inline-block;color:#fff;line-height: 40px;background-color: #1989fa;border-radius: 5px;text-align: center;text-decoration: none;font-size: 14px;padding: 1px 30px;\">\n" +
                    "                    马上验证邮箱\n" +
                    "                  </a>\n" +
                    "                </td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"10\">\n" +
                    "                <td></td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"20\">\n" +
                    "                <td style=\"padding-left:55px;padding-right:55px;font-family:&#39;微软雅黑&#39;,&#39;黑体&#39;,arial;font-size:12px;\">\n" +
                    "                  如果您无法点击以上链接，请复制以下网址到浏览器里直接打开：\n" +
                    "                </td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"30\">\n" +
                    "                <td style=\"padding-left:55px;padding-right:65px;font-family:&#39;微软雅黑&#39;,&#39;黑体&#39;,arial;line-height:18px;\">\n" +
                    "                  <a style=\"color:#0c94de;font-size:12px;\" href=\"" + ServerPath + ActiveReqUrl + "?idName=" + username + "\">\n" +
                    "                    " + ServerPath + ActiveReqUrl + "?idName=" + username + "\n" +
                    "                  </a>\n" +
                    "                </td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"20\">\n" +
                    "                <td style=\"padding-left:55px;padding-right:55px;font-family:&#39;微软雅黑&#39;,&#39;黑体&#39;,arial;font-size:12px;\">\n" +
                    "                  如果您并未申请HiShop账户，可能是其他用户误输入了您的邮箱地址。请忽略此邮件。\n" +
                    "                </td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"20\">\n" +
                    "                <td></td>\n" +
                    "              </tr>\n" +
                    "            </table>\n" +
                    "          </td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td style=\"height:40px;\"></td>\n" +
                    "        </tr>\n" +
                    "      </table>\n" +
                    "    </td>\n" +
                    "  </tr>\n" +
                    "</table>";
            helper.setText(content, true);
//            helper.addInline(logoimgId,file);
            SendMailThread.sendEmail(javaMailSender, message);
//            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }

    }

    public boolean SendCode(Long uid, String email) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
//            helper.setCc(sender);
            helper.setTo(email);
            helper.setSubject("HiShop商城验证码");
            String code = getCode();
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
                    "                     <img src=\"" + imgPath + "\" width=\"54\"  />\n" +
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
                    "                  尊敬的" + email + "您好,\n" +
                    "                </td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"15\">\n" +
                    "                <td></td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"30\">\n" +
                    "                <td style=\"padding-left:55px;padding-right:55px;font-family:&#39;微软雅黑&#39;,&#39;黑体&#39;,arial;font-size:14px;\">\n" +
                    "                  感谢您选择HiShop商城。\n" +
                    "                </td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"30\">\n" +
                    "                <td style=\"padding-left:55px;padding-right:55px;font-family:&#39;微软雅黑&#39;,&#39;黑体&#39;,arial;font-size:14px;\">\n" +
                    "                  本次操作的验证码为：<span style=\"color:#0000FF\">" + code + "</span>,有效时间一分钟。\n" +
                    "                </td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"10\">\n" +
                    "                <td></td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"20\">\n" +
                    "                <td style=\"padding-left:55px;padding-right:55px;font-family:&#39;微软雅黑&#39;,&#39;黑体&#39;,arial;font-size:12px;\">\n" +
                    "                  如果您未操作，请忽略此邮件。\n" +
                    "                </td>\n" +
                    "              </tr>\n" +
                    "              <tr height=\"20\">\n" +
                    "                <td></td>\n" +
                    "              </tr>\n" +
                    "            </table>\n" +
                    "          </td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td style=\"height:40px;\"></td>\n" +
                    "        </tr>\n" +
                    "      </table>\n" +
                    "    </td>\n" +
                    "  </tr>\n" +
                    "</table>";
            helper.setText(content, true);
            SendMailThread.sendEmail(javaMailSender, message);

//            javaMailSender.send(message);
            redisTemplate.opsForValue().set(CODE_PEX+":" + uid, code, 1, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    // 生成6位数验证码
    public static String getCode() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }


}
