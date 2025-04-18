package com.vtit.project.thuctap.service.impl;

import com.vtit.project.thuctap.constant.enums.ENoticationType;
import com.vtit.project.thuctap.constant.enums.EStatusBorrow;
import com.vtit.project.thuctap.entity.Borrow;
import com.vtit.project.thuctap.repository.BorrowRepository;
import com.vtit.project.thuctap.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    private final BorrowRepository borrowRepository;
    private final ModelMapper modelMapper;
    private final JavaMailSender mailSender;

    @NonFinal
    @Value("${spring.mail.properties.mail.from}")
    protected String from;

    public void sendNotifications() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoDaysFromNow = now.plusDays(2);

        // <Email, <Type email, body>>
        Map<String, Map<String, StringBuilder>> userNotifications = new HashMap<>();
        List<Borrow> borrows = borrowRepository.findBorrowsDueSoon(now, twoDaysFromNow);

        for (Borrow borrow : borrows) {
            boolean isOverdue = borrow.getExpireAt().before(Timestamp.valueOf(now));
            boolean isDueSoon = borrow.getExpireAt().before(Timestamp.valueOf(twoDaysFromNow)) && !isOverdue;

            if (!isDueSoon && !isOverdue) {
                continue;
            }

            String email = borrow.getUserId().getEmail();
            String notificationType = isOverdue
                    ? ENoticationType.OVERDUE.toString()
                    : ENoticationType.DUE_SOON.toString();

            // update value bởi key đươc chỉ định
            userNotifications.computeIfAbsent(email, k -> new HashMap<>())
                    .computeIfAbsent(notificationType, k -> new StringBuilder())
                    .append("- ")
                    .append(borrow.getBorrowItemList().stream()
                            .filter(item -> item.getStatus() == EStatusBorrow.BORROWED)
                            .map(item -> item.getBookId().getTitle() + " (Số lượng: " + item.getQuantity() + ")")
                            .collect(Collectors.joining("\n- ")))
                    .append("\nHết hạn vào: ").append(borrow.getExpireAt().toLocalDateTime()).append("\n\n");
        }

        // Send notifications for each user
        for (Map.Entry<String, Map<String, StringBuilder>> userEntry : userNotifications.entrySet()) {
            String email = userEntry.getKey();
            String fullname = borrows.stream()
                    .filter(b -> b.getUserId().getEmail().equals(email))
                    .findFirst()
                    .map(b -> b.getUserId().getFullname())
                    .orElse("User");

            List<Borrow> userBorrows = borrows.stream()
                    .filter(b -> b.getUserId().getEmail().equals(email))
                    .toList();
            // Check if there are any BORROWED items
            boolean hasBorrowedItems = userBorrows.stream()
                    .flatMap(b -> b.getBorrowItemList().stream())
                    .anyMatch(item -> item.getStatus() == EStatusBorrow.BORROWED);

            if (!hasBorrowedItems) {
                logger.info("No BORROWED items for user {}, skipping email", email);
                continue;
            }

            Map<String, StringBuilder> notifications = userEntry.getValue();

            // Email content
            StringBuilder emailBody = new StringBuilder("Dear ").append(fullname).append(",\n\n");

            if (notifications.containsKey(ENoticationType.OVERDUE.toString() )) {
                emailBody.append("Các cuốn sách bạn mượn đã hết hạn vào:\n\n")
                        .append(borrows.stream().filter(
                                b -> b.getUserId().getEmail().equals(email)
                        ))
                        .append(notifications.get(ENoticationType.OVERDUE.toString()))
                        .append(" Hãy trả lại thư viện ngay khi có thể để tránh việc quá hạn.\n\n");
            }
            if (notifications.containsKey(ENoticationType.DUE_SOON.toString() )) {
                emailBody.append("Các cuốn sách mà bạn mượn sẽ hết hạn vào ngày:\n\n")
                        .append(notifications.get(ENoticationType.DUE_SOON.toString() ))
                        .append(" Hãy trả lại thư viện đúng hạn.\n\n");
            }
            emailBody.append("\nCảm ơn,\nThư viện.");
            emailBody.append("\nBest Regards,\n\n.");

            // Send email
            sendEmail(email, notifications.containsKey("OVERDUE") ? "Nhắc nhở về sách quá hạn và sắp đến hạn": "Nhắc nhở về sách đến hạn", emailBody.toString());
        }
    }

    @Retryable(value = MailException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void sendEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            logger.info("Email sent to {}", toEmail);
        } catch (MailException e) {
            logger.error("Failed to send email to {}: {}", toEmail, e.getMessage());
            throw e;
        }
    }

}
