package com.optguard.controller;

import com.optguard.dto.ApiDtos.DashboardSummaryResponse;
import com.optguard.service.CurrentUserService;
import com.optguard.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final CurrentUserService currentUserService;
    private final DashboardService dashboardService;

    public DashboardController(CurrentUserService currentUserService, DashboardService dashboardService) {
        this.currentUserService = currentUserService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public DashboardSummaryResponse summary() {
        return dashboardService.summary(currentUserService.getCurrentUser());
    }
}
