package com.tranbaolong2ws.requesmodel;

import lombok.Data;

import java.util.Optional;

@Data
public class ReviewRequest {
   private double  rating;

   private Long bookId;

   private String reviewDescription;

//   private Optional<String> description;




}
