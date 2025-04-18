package com.vtit.project.thuctap.controller;

import com.vtit.project.thuctap.dto.response.ApiResponse;
import com.vtit.project.thuctap.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/mail")
public class MailController {

    private final MailService mailService;
    @PostMapping("/send")
    public ApiResponse<String> triggerNotifications() {
        mailService.sendNotifications();
        return ApiResponse.<String>builder()
                .result("Notification send successfully")
                .build();
    }
}
