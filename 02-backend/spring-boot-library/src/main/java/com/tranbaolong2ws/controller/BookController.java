package com.tranbaolong2ws.controller;


import com.tranbaolong2ws.entitys.Book;
import com.tranbaolong2ws.repsonsemodel.ShelfCurrentLoansResponse;
import com.tranbaolong2ws.service.BookService;
import com.tranbaolong2ws.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("secure/currentloans")
    public List<ShelfCurrentLoansResponse> currentLoans(@RequestHeader(value = "Authorization") String token) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        return bookService.currentLoans(userEmail);
    }

    @GetMapping("secure/currentloans/count")
    public int currentLoansCount(@RequestHeader(value = "Authorization") String token) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        return bookService.currentLoansCount(userEmail);
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public Boolean checkoutBookByUser (@RequestHeader(value = "Authorization") String token,
                                       @RequestParam Long bookId) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        return bookService.checkoutBookByUser(userEmail, bookId);
    }

    @PutMapping("/secure/checkout")
    public Book checkoutBook(@RequestHeader(value = "Authorization") String token
                             ,@RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        return  bookService.checkouBook(userEmail,bookId);
    }

    @PutMapping("/secure/return")
    public void returnBook (@RequestHeader(value = "Authorization") String token,
                            @RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
         bookService.returnBook(userEmail,bookId);
    }

    @PutMapping("/secure/renew/loan")
    public void renewBook (@RequestHeader(value = "Authorization") String token,
                           @RequestParam Long bookId) throws  Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        bookService.renewBook(userEmail,bookId);
    }
}
