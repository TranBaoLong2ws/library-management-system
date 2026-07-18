package com.tranbaolong2ws.dao;

import com.tranbaolong2ws.entitys.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestParam;

public interface MessageRepository extends CrudRepository<Message, Long> {

    Page<Message> findByUserEmail(@RequestParam("user_email") String userEmail, Pageable pageable);

    Page<Message> findByClosed(@RequestParam("closed")  boolean closed, Pageable pageable);

}
