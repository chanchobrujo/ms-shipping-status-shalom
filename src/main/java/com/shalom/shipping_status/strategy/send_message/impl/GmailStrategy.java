package com.shalom.shipping_status.strategy.send_message.impl;

import com.shalom.shipping_status.document.ShipStatusDocument;
import com.shalom.shipping_status.error.exception.BusinessException;
import com.shalom.shipping_status.mapper.NotificationMapper;
import com.shalom.shipping_status.strategy.send_message.SendMessageStrategy;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Slf4j
@Service
@RequiredArgsConstructor
public class GmailStrategy implements SendMessageStrategy {
    private final JavaMailSender sender;
    private final NotificationMapper notificationMapper;

    @Override
    public Mono<Void> send(Tuple2<ShipStatusDocument, String> tuple) {
        var notification = this.notificationMapper.mapper(tuple);
        return Mono.fromRunnable(() -> {
            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(notification.getEmail());
                helper.setSubject(notification.getSubject());
                helper.setText(notification.getText(), false);
                this.sender.send(message);
            } catch (MessagingException | MailException e) {
                log.error(e.getMessage());
                throw new BusinessException("Error sending email");
            }
        });
    }
}
