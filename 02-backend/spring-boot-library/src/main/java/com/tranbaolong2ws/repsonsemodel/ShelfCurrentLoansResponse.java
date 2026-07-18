package com.tranbaolong2ws.repsonsemodel;

import com.tranbaolong2ws.entitys.Book;
import lombok.Data;

@Data
public class ShelfCurrentLoansResponse {

    private Book book;

    private int daysLeft;

    public ShelfCurrentLoansResponse(Book book, int daysLeft) {
        this.book = book;
        this.daysLeft = daysLeft;
    }
}
