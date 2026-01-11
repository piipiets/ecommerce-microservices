package com.microservices.notificationservice.service;

import com.microservices.notificationservice.mapper.TdOrderItemsMapper;
import com.microservices.notificationservice.mapper.TdOrderMapper;
import com.microservices.notificationservice.model.dto.OrderItems;
import com.microservices.notificationservice.model.dto.OrderNotificationDto;
import com.microservices.notificationservice.model.dto.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final TdOrderMapper tdOrderMapper;
    private final TdOrderItemsMapper tdOrderItemsMapper;
    private final TemplateEngine templateEngine;

    public void send(String to, String subject, String html) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Send email failed", e);
        }
    }

    public void sendEmail(OrderNotificationDto message) {
        try {
            Order order = tdOrderMapper.getByOrderNumber(message.getOrderNumber());
            if (order != null) {
                List<OrderItems> orderItems = tdOrderItemsMapper.getItemsByOrderNumber(message.getOrderNumber());
                order.setOrderItems(orderItems);

                String html = buildHtml(order);
                log.info("TO : " + message.getEmail());
                send(
                        message.getEmail(),
                        "Order Summary At Ecommerce",
                        html
                );
            }
        } catch (Exception e) {
            log.error("Failed send email", e);
            throw e;
        }
    }

    public String buildHtml(Order dto) {
        Context ctx = new Context();
        ctx.setVariable("orderNumber", dto.getOrderNumber());
        ctx.setVariable("items", dto.getOrderItems());

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItems item : dto.getOrderItems()) {
            BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);
        }

        ctx.setVariable("total", total);

        return templateEngine.process("order-summary", ctx);
    }
}
