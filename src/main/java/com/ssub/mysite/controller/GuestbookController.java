package com.ssub.mysite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ssub.mysite.service.GuestbookService;
import com.ssub.mysite.vo.GuestbookVo;

@Controller
@RequestMapping( "/guestbook" )
public class GuestbookController {
	
	@Autowired
	private GuestbookService guestbookService;
	
	@RequestMapping( "" )
	public String list(Model model) {
		List<GuestbookVo> list = guestbookService.getMessageList();
		model.addAttribute("list", list);
		return "guestbook/list";
	}
	
	@RequestMapping( "/ajax" )
	public String ajax() {
		return "guestbook/index-ajax";
	}
	
	@RequestMapping(
		value="/delete/{no}", 
		method=RequestMethod.GET)
	public String delete(@PathVariable("no") Long no, Model model) {
		model.addAttribute( "no", no );
		return "guestbook/delete";
	}

	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(@ModelAttribute GuestbookVo vo) {
		guestbookService.deleteMessage( vo );
		return "redirect:/guestbook";
	}
	
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public String insert(
		@ModelAttribute GuestbookVo guestbookVo) {
		guestbookService.insertMessage(guestbookVo);
		return "redirect:/guestbook";
	}
}
