package com.tranbaolong2ws.service;

import com.tranbaolong2ws.dao.HistoryRepository;
import com.tranbaolong2ws.entitys.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    public Page<History> getHistory(String userEmail, Pageable pageable) {
        return historyRepository.findByUserEmail(userEmail, pageable);
    }
}
