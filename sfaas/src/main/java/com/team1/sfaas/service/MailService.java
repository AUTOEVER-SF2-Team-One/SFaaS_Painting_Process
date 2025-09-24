package com.team1.sfaas.service;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;
    // email → 인증번호
    private static final ConcurrentHashMap<String, String> codeStore = new ConcurrentHashMap<>();

    public void sendMail(String to) {
        try {
            Random random = new Random();
            int number = 100000 + random.nextInt(900000); // 100000~999999
            String randomNumber = String.valueOf(number);

            // 인증번호 저장
            codeStore.put(to, randomNumber);

            // MimeMessage 생성 (HTML 지원)
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String subject = "모니터링 시스템 인증 번호";
            
            // HTML 이메일 본문
            String htmlBody = "<div style=\"font-family: Arial, sans-serif; max-width: 500px; margin: 0 auto; padding: 20px; background-color: #f9f9f9;\">" +
                "    <div style=\"background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); text-align: center;\">" +
                "        <h2 style=\"color: #333; margin-bottom: 20px;\">🔐 이메일 인증</h2>" +
                "        <p style=\"color: #666; font-size: 16px; margin-bottom: 25px;\">아래 인증번호를 입력해주세요</p>" +
                "        <div style=\"background: linear-gradient(45deg, #4CAF50, #45a049); color: white; padding: 20px; border-radius: 8px; margin: 20px 0; font-size: 24px; font-weight: bold; letter-spacing: 2px;\">" +
                randomNumber +
                "        </div>" +
                "        <p style=\"color: #999; font-size: 14px; margin-top: 25px;\">⏰ 이 인증번호는 10분간 유효합니다</p>" +
                "    </div>" +
                "</div>";

            helper.setFrom("wlwhs043@naver.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML 모드

            System.out.println("here2");
            mailSender.send(mimeMessage); // MimeMessage 전송
            
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("이메일 전송 실패: " + e.getMessage());
        }
    }

    // public void sendMail(String to) {
    //     SimpleMailMessage message = new SimpleMailMessage();
    //     Random random = new Random();
    //     int number = 100000 + random.nextInt(900000); // 100000~999999
    //     String randomNumber = String.valueOf(number);

    //      // 인증번호 저장
    //     codeStore.put(to, randomNumber);

    //     // 2️⃣ URL 인코딩된 제목과 내용
    //     String subject = "모니터링 시스템 인증 번호";
    //     // HTML 이메일 본문 템플릿
    //     String body = "<div style=\"font-family: Arial, sans-serif; max-width: 500px; margin: 0 auto; padding: 20px; background-color: #f9f9f9;\">" +
    //         "    <div style=\"background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); text-align: center;\">" +
    //         "        <h2 style=\"color: #333; margin-bottom: 20px;\">🔐 이메일 인증</h2>" +
    //         "        <p style=\"color: #666; font-size: 16px; margin-bottom: 25px;\">아래 인증번호를 입력해주세요</p>" +
    //         "        <div style=\"background: linear-gradient(45deg, #4CAF50, #45a049); color: white; padding: 20px; border-radius: 8px; margin: 20px 0; font-size: 24px; font-weight: bold; letter-spacing: 2px;\">" +
    //         randomNumber +
    //         "        </div>" +
    //         "        <p style=\"color: #999; font-size: 14px; margin-top: 25px;\">⏰ 이 인증번호는 10분간 유효합니다</p>" +
    //         "    </div>" +
    //         "</div>";
                
    //     message.setFrom("wlwhs043@naver.com");
    //     message.setTo(to);
    //     message.setSubject(subject);
    //     message.setText(body);
    //     System.out.println("here2");
    //     mailSender.send(message);
    // }

    // 인증번호 확인
    public boolean verifyCode(String email, String code) {
        String stored = codeStore.get(email);
        System.out.println("email"+email); //현재 인증 이메일
        System.out.println("stored"+stored);  //이메일에 대한 인증번호 값
        System.out.println("code:"+code); // 실제 인증번호
        if(stored != null && stored.equals(code)) {
            codeStore.remove(email); // 인증 완료 후 제거
            return true;
        }
        return false;
    }
}
