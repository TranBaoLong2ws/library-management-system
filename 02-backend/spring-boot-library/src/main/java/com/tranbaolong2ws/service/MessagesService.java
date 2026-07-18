package com.tranbaolong2ws.service;

import com.tranbaolong2ws.dao.MessageRepository;
import com.tranbaolong2ws.entitys.Message;
import com.tranbaolong2ws.requesmodel.QuestionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MessagesService {

    private MessageRepository messageRepository;

    @Autowired
    public MessagesService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void postMessage(Message messageRequest, String userEmail) {
       Message message = new Message(messageRequest.getTitle(), messageRequest.getQuestion());
        message.setUserEmail(userEmail);
        messageRepository.save(message);
    }

    public void putMessage(QuestionModel questionModel, String userEmail) throws Exception{
        Optional<Message> message = messageRepository.findById(questionModel.getId());
        if (!message.isPresent()) {
            throw new Exception("Message not found");
        }

        message.get().setAdminEmail(userEmail);
        message.get().setResponse(questionModel.getReponse());
        message.get().setClosed(true);
        messageRepository.save(message.get());
    }
}
