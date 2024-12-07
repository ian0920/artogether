package com.artogether.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailManager {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegisterSuccessMail(String name, String receiverMail, Integer memberId) {

        //註冊成功信件標題
        String subject = "Congratulations! Your registration on Artogether was successful.";

        try{
            MimeMessage mimeMailMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, true);
            helper.setTo(receiverMail);
            helper.setSubject(subject);
            helper.setText(registerSuccessContentTransformer(name, memberId), true);
            mailSender.send(mimeMailMessage);
            System.out.println("信件寄出");
        }catch (MessagingException e){
            e.printStackTrace();
            throw new RuntimeException("寄信失敗");
        }



    }



    //註冊成功信件內容
    private String registerSuccessContentTransformer(String receiverName, Integer memberId) {

        String formated = String.format("<html>\n" +
                "                <head>\n" +
                "                    <style>\n" +
                "                        body {\n" +
                "                            font-family: Arial, sans-serif;\n" +
                "                        }\n" +
                "                        .email-container {\n" +
                "                            max-width: 600px;\n" +
                "                            margin: auto;\n" +
                "                            border: 1px solid #ddd;\n" +
                "                            padding: 20px;\n" +
                "                            border-radius: 8px;\n" +
                "                        }\n" +
                "                        .header {\n" +
                "                            text-align: center;\n" +
                "                            background-color: #f4f4f4;\n" +
                "                            padding: 10px 0;\n" +
                "                        }\n" +
                "                        .content {\n" +
                "                            margin-top: 20px;\n" +
                "                        }\n" +
                "                        .footer {\n" +
                "                            margin-top: 20px;\n" +
                "                            text-align: center;\n" +
                "                            font-size: 12px;\n" +
                "                            color: #888;\n" +
                "                        }\n" +
                "                    </style>\n" +
                "                </head>\n" +
                "                <body>\n" +
                "                    <div class=\"email-container\">\n" +
                "                        <div class=\"header\">\n" +
                "                            <h1>Welcome to Artogether!</h1>\n" +
                "                        </div>\n" +
                "                        <div class=\"content\">\n" +
                "                            <p>Dear <strong>[%s]</strong>,</p>\n" +
                "                            <p>Congratulations! Your registration was successful.</p>\n" +
                "                            <p>Thank you for joining us. Feel free to explore and get started with our services.</p>\n" +
                "                            <p>請盡快前往驗證連結，以完成會員帳號啟用： <a href=\"http://localhost:8080/memberVerifier?memberId=%d\">啟用帳號</a>.</p>\n" +
                "                        </div>\n" +
                "                        <div class=\"footer\">\n" +
                "                            <p>&copy; 2024 Artogether. All rights reserved.</p>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </body>\n" +
                "                </html>", receiverName, memberId);

        return formated;


    }

}
