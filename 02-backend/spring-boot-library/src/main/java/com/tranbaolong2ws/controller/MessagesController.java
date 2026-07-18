package com.tranbaolong2ws.controller;

import com.tranbaolong2ws.entitys.Message;
import com.tranbaolong2ws.requesmodel.QuestionModel;
import com.tranbaolong2ws.service.MessagesService;
import com.tranbaolong2ws.service.ReviewService;
import com.tranbaolong2ws.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/messages")
public class MessagesController {

   private MessagesService messagesService;


   @Autowired
   public MessagesController(MessagesService messagesService) {
       this.messagesService = messagesService;
   }

   @PostMapping("/secure/add/message")
    public void postMessage(@AuthenticationPrincipal Jwt jwt,
                            @RequestBody Message messageResquest) {
       String userEmail = jwt.getClaim("https://luv2code-react-library.com/email");
        messagesService.postMessage(messageResquest,userEmail);
    }

    @PutMapping("/secure/admin/message")
    public void putMessage(@AuthenticationPrincipal Jwt jwt,
                           @RequestBody QuestionModel questionModel) throws Exception{
        String userEmail = jwt.getClaim("https://luv2code-react-library.com/email");
        List<String> roles = jwt.getClaimAsStringList("https://luv2code-react-library.com/roles");
        String admin = roles != null && !roles.isEmpty() ? roles.get(0) : null;
        if (!admin.equals("admin") || admin == null) {
            throw new Exception("Admin or userType not found");
        }

        messagesService.putMessage(questionModel,userEmail);
    }
}
