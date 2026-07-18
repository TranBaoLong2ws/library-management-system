package com.tranbaolong2ws.dao;

import com.tranbaolong2ws.entitys.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


public interface HistoryRepository extends JpaRepository<History,Long> {
    Page<History> findByUserEmail(String userEmail, Pageable pageable);
}
