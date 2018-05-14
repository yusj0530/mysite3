package com.ssub.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssub.mysite.dao.GuestbookDao;
import com.ssub.mysite.vo.GuestbookVo;

@Service
public class GuestbookService {
	@Autowired
	private GuestbookDao guestbookDao;

//	public List<GuestbookVo> getMessageList(Long no){
//		List<GuestbookVo> list = guestbookDao.getList(no);
//		return list;
//	}
	
	public List<GuestbookVo> getMessageList(){
		List<GuestbookVo> list = guestbookDao.getList();
		return list;
	}
	
	public boolean insertMessage( GuestbookVo guestbookVo ) {
		int count = guestbookDao.insert(guestbookVo);
		return count == 1;
	}
	
	public boolean deleteMessage( GuestbookVo guestbookVo ) {
		int count = guestbookDao.delete(guestbookVo);
		return count == 1;
	}
}
