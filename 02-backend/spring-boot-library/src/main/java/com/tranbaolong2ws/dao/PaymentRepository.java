package com.tranbaolong2ws.dao;

import com.tranbaolong2ws.entitys.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
   Payment findByUserEmail(String email);
}
