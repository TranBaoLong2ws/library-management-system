package com.tranbaolong2ws.service;

import com.tranbaolong2ws.dao.BookRepository;
import com.tranbaolong2ws.dao.ReviewRepository;
import com.tranbaolong2ws.entitys.Review;
import com.tranbaolong2ws.requesmodel.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;


@Service
@Transactional
public class ReviewService {

    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(BookRepository bookRepository, ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void postReview(String userEmail, ReviewRequest request) throws  Exception {

        System.out.println("Rating: " + request.getRating());
        System.out.println("BookId: " + request.getBookId());
        System.out.println("Description: " + request.getReviewDescription());

      Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail,request.getBookId());

       if (validateReview != null) {
            throw new Exception("Review already created");
       }

       Review review = new Review();
       review.setBookId(request.getBookId());
       review.setRating(request.getRating());
       review.setUserEmail(userEmail);

//       if  (request.getDescription().isPresent()) {
//          review.setReviewDescription(request.getDescription().map(
//                  Object::toString
//          ).orElse(null));
//       }

        if (request.getReviewDescription() != null && !request.getReviewDescription().isBlank()) {
            review.setReviewDescription(request.getReviewDescription());
        }

        review.setDate(Date.valueOf(LocalDate.now()));

       reviewRepository.save(review);
    }

    public Boolean userReviewListened(String userEmail, Long bookId){

        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail,bookId);

        if (validateReview != null) {
            return true;
        } else  {
            return false;
        }

    }
}
