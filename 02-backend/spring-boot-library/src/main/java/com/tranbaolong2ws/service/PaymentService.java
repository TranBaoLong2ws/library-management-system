package com.tranbaolong2ws.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.tranbaolong2ws.dao.PaymentRepository;
import com.tranbaolong2ws.entitys.Payment;
import com.tranbaolong2ws.requesmodel.PaymentInfoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaymentService {

    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, @Value("${stripe.key.secret}") String secretKey) {
        this.paymentRepository = paymentRepository;
        Stripe.apiKey = secretKey;
    }


    public Payment getPaymentByUserEmail(String userEmail) throws Exception {

        Payment payment = paymentRepository.findByUserEmail(userEmail);

        if (payment == null) {
            throw new Exception("Payment not found");
        }

        return payment;
    }


    public PaymentIntent createPaymentIntent(PaymentInfoRequest paymentInfoRequest) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList<>();

        paymentMethodTypes.add("card");

        Map<String, Object>  params = new HashMap<>();

        params.put("amount", paymentInfoRequest.getAmount());
        params.put("currency", paymentInfoRequest.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);

        return PaymentIntent.create(params);
    }


    public ResponseEntity<String> stripePayment(String userEmail) throws Exception {

        System.out.println("=== Payment Complete ===");
        System.out.println("User: " + userEmail);


        Payment payment = paymentRepository.findByUserEmail(userEmail);

         if (payment == null) {
             throw new Exception("Payment not found");
         }

        System.out.println("Before: " + payment.getAmount());

        payment.setAmount(00.00);
         paymentRepository.save(payment);

        System.out.println("After: " + payment.getAmount());
         return  new ResponseEntity<>(HttpStatus.OK);
    }


}
