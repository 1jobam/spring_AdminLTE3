package com.spring.controller;

import java.sql.SQLException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.exception.InvalidPasswordException;
import com.spring.exception.NotFoundIDException;
import com.spring.service.MemberService;

@Controller
@RequestMapping("/commons/*")
public class CommonsActionController {
	
	private MemberService memberService;
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

	@RequestMapping(value="loginForm.do", method=RequestMethod.GET)
	public String loginForm() {
		String url = "commons/loginForm";
		return url;
	}
	
	@RequestMapping(value="login.do", method=RequestMethod.POST)
	public String login(String id, String pwd) {
		String url = "redirect:/member/list.do";
		
		try {
			memberService.login(id, pwd);
		} catch (SQLException | NotFoundIDException | InvalidPasswordException e) {
			e.printStackTrace();
		}
		
		return url;
		
	}
}
