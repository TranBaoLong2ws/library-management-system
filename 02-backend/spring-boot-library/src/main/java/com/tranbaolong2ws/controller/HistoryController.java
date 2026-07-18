package com.tranbaolong2ws.controller;


import com.tranbaolong2ws.entitys.History;
import com.tranbaolong2ws.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
@CrossOrigin("http://localhost:3000")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/secure")
    public Page<History> getHistory(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        String email = jwt.getClaim("email");

        Pageable pageable = PageRequest.of(page, size);

        return historyService.getHistory(email, pageable);
    }
}
