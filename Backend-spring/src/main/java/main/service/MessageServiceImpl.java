package main.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import main.model.MessagePageList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import main.domain.Message;
import main.exceptions.MessageException;
import main.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

	private final MessageRepository messageRepo;

	@Override
	public Message sendMessage(Message msg)  {
	   return messageRepo.save(msg);
	}

	@Override
	public void deleteMessage(UUID msgId) throws MessageException {
		if(!messageRepo.existsById(msgId))
			throw new MessageException("Message with id " + msgId  + " doesnt exists!");
		
		 messageRepo.deleteById(msgId);

	}

	@Override
	public void setReaded(UUID msgId) throws MessageException {
		Message msg = findById(msgId);

		if(!msg.isReaded()){
			msg.setReaded(true);
		    messageRepo.save(msg);
		}
	}

	@Override
	public void setUnRead(UUID msgId) throws MessageException {
		Message msg = findById(msgId);

		if(msg.isReaded()){
			msg.setReaded(false);
			messageRepo.save(msg);
		}
	}

	@Override
	public Message findById(UUID msgId) throws MessageException {
		return  messageRepo.findById(msgId).orElseThrow(() ->  new MessageException("Message with id" + msgId + " doesnt exists"));
	}

	@Override
	public MessagePageList findMessages(int page, String filterBy) {
		Boolean filterValue = null;

		if(filterBy.equals("readed")) {
			filterValue = true;
		}else if (filterBy.equals("unread")) {
			filterValue = false;
		}
		PageRequest pageable = PageRequest.of(page > 0 ? (page - 1) : 0, 15);
		Page<Message> messagePage = messageRepo.findAll(filterValue, pageable);

		return new MessagePageList(messagePage.getContent(), pageable, messagePage.getTotalElements());
	}
}
