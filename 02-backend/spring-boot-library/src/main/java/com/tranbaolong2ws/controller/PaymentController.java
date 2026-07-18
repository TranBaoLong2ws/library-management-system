package com.tranbaolong2ws.controller;


import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.tranbaolong2ws.entitys.Payment;
import com.tranbaolong2ws.requesmodel.PaymentInfoRequest;
import com.tranbaolong2ws.service.PaymentService;
import com.tranbaolong2ws.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/payment/secure")
public class PaymentController {

    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/fees")
    public ResponseEntity<Payment> getFees(
            @RequestHeader("Authorization") String token) throws Exception {

        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");

        if (userEmail == null) {
            throw new Exception("User email is missing");
        }

        Payment payment = paymentService.getPaymentByUserEmail(userEmail);

        return ResponseEntity.ok(payment);
    }

    @PostMapping("/payment-intent")
    public ResponseEntity<String> createPayment(@RequestBody PaymentInfoRequest payment)
            throws StripeException {

        PaymentIntent paymentIntent = paymentService.createPaymentIntent(payment);

        String pamentStr = paymentIntent.toJson();

        return new ResponseEntity<>(pamentStr, HttpStatus.OK);

    }


    @PutMapping("/payment-complete")
    public ResponseEntity<String> stripePaymentComplete(@RequestHeader(value="Authorization") String token)
            throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        if (userEmail == null) {
            throw new Exception("User email is missing");
        }
        return paymentService.stripePayment(userEmail);
    }

}
