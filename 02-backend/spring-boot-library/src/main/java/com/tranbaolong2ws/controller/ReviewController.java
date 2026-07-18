package com.tranbaolong2ws.controller;


import com.tranbaolong2ws.requesmodel.ReviewRequest;
import com.tranbaolong2ws.service.ReviewService;
import com.tranbaolong2ws.utils.ExtractJWT;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/secure/user/book")
    public Boolean reviewBookByUser(@RequestHeader(value = "Authorization") String token,
                                    @RequestParam Long bookId) throws Exception {

        String username = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");

        if (username == null) {
            throw new Exception("User email is missing");
        }

        return reviewService.userReviewListened(username, bookId);
    }

    @PostMapping("/secure")
    public void postReview(@RequestHeader(value = "Authorization") String token,
                           @RequestBody ReviewRequest review) throws  Exception {

        String username = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");

        if (username == null) {
            throw new Exception("User email is missing");
        }

        reviewService.postReview(username, review);
    }




}
