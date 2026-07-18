package com.tranbaolong2ws.service;


import com.tranbaolong2ws.dao.BookRepository;
import com.tranbaolong2ws.dao.CheckoutRepository;
import com.tranbaolong2ws.dao.HistoryRepository;
import com.tranbaolong2ws.dao.PaymentRepository;
import com.tranbaolong2ws.entitys.Book;
import com.tranbaolong2ws.entitys.Checkout;
import com.tranbaolong2ws.entitys.History;
import com.tranbaolong2ws.entitys.Payment;
import com.tranbaolong2ws.repsonsemodel.ShelfCurrentLoansResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class BookService {

    private BookRepository bookRepository;

    private CheckoutRepository checkoutRepository;

    private HistoryRepository historyRepository;

    private PaymentRepository paymentRepository;

    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository,
                       HistoryRepository historyRepository, PaymentRepository paymentRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
        this.historyRepository = historyRepository;
        this.paymentRepository = paymentRepository;
    }

    public Book checkouBook (String userEmail, Long bookId) throws Exception {

        Optional<Book> book = bookRepository.findById(bookId);

        Checkout  validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

         if(!book.isPresent() ||  validateCheckout!=null ||  book.get().getCopiesAvailable() <= 0){
             throw new Exception("Book doesn't exist or already checked out by user. ");
         }

         List<Checkout> currentBooksCheckedOut = checkoutRepository.findBooksByUserEmail(userEmail);

         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        boolean bookNeedsReturned = false;

        for (Checkout checkout: currentBooksCheckedOut) {
            Date d1 = sdf.parse(checkout.getReturnDate());
            Date d2 = sdf.parse(LocalDate.now().toString());

            TimeUnit time = TimeUnit.DAYS;

            double differenceInTime = time.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);

            if (differenceInTime < 0) {
                bookNeedsReturned = true;
                break;
            }
        }

        Payment userPayment = paymentRepository.findByUserEmail(userEmail);


        if ((userPayment != null && userPayment.getAmount() > 0) || (userPayment != null && bookNeedsReturned)) {
            throw new Exception("Outstanding fees");
        }

        if (userPayment == null) {
            Payment payment = new Payment();
            payment.setAmount(00.00);
            payment.setUserEmail(userEmail);
            paymentRepository.save(payment);
        }

         book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
         bookRepository.save(book.get());

         Checkout checkout = new Checkout(
                 userEmail,
                 LocalDate.now().toString(),
                 LocalDate.now().plusDays(7).toString(),
                 book.get().getId()
         );

         checkoutRepository.save(checkout);


        return book.get();
    }

    public Boolean checkoutBookByUser (String userEmail, Long bookId) {
        Checkout checkout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if( checkout != null ){
            return true;
        } else {
            return false;
        }
    }

    public int currentLoansCount(String userEmail) {
        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }

    public List<ShelfCurrentLoansResponse> currentLoans (String userEmail) throws Exception {

        List<ShelfCurrentLoansResponse> shelfCurrentLoans = new ArrayList<>();

        List<Checkout> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);

        List<Long> bookIdList = new ArrayList<>();

        for (Checkout i : checkoutList){
            bookIdList.add(i.getBookId());
        }

        List<Book> bookList = bookRepository.findBooksByBookIds(bookIdList);

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Book book : bookList){
            Optional<Checkout> checkout = checkoutList .stream()
                    .filter(x -> x.getBookId() == book.getId()).findFirst();

            if (checkout.isPresent()) {
                Date d1 = sdFormat.parse(checkout.get().getReturnDate());
                Date d2 = sdFormat.parse(LocalDate.now().toString());

                TimeUnit time = TimeUnit.DAYS;

                long difference_In_time = time.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);
                shelfCurrentLoans.add(new ShelfCurrentLoansResponse(book,(int)difference_In_time));
            }
        }
        return  shelfCurrentLoans;
    }

    public void returnBook(String userEmail, Long bookId) throws Exception {

        Optional<Book> book = bookRepository.findById(bookId);

        Checkout checkout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (!book.isPresent() || checkout == null) {
            throw new Exception("Book doesn't exist or checked out by user. ");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);

        bookRepository.save(book.get());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date d1 = sdf.parse(checkout.getReturnDate());
        Date d2 = sdf.parse(LocalDate.now().toString());

        TimeUnit time = TimeUnit.DAYS;

        double differenceInTime = time.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);

        if (differenceInTime < 0) {
            Payment payment = paymentRepository.findByUserEmail(userEmail);

            payment.setAmount(payment.getAmount() + (differenceInTime * -1));
            paymentRepository.save(payment);
        }

        checkoutRepository.deleteById(checkout.getId());

        History history = new History(
                userEmail,
                checkout.getCheckoutDate(),
                LocalDate.now().toString(),
                book.get().getTitle(),
                book.get().getAuthor(),
                book.get().getDescription(),
                book.get().getImg()
        );

        historyRepository.save(history);
    }

    public void renewBook(String userEmail, Long bookId) throws Exception {

        Checkout checkout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (checkout == null) {
            throw new Exception("Book doesn't exist or checked out by user. ");
        }

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date d1 = sdFormat.parse(checkout.getReturnDate());
        Date d2 = sdFormat.parse(LocalDate.now().toString());

        if (d1.compareTo(d2) > 0 || d1.compareTo(d2) == 0) {
            checkout.setReturnDate(LocalDate.now().plusDays(7).toString());
            checkoutRepository.save(checkout);
        }
    }
}
