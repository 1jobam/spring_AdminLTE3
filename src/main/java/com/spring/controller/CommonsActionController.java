package com.spring.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.dto.MemberVO;
import com.spring.exception.InvalidPasswordException;
import com.spring.exception.NotFoundIDException;
import com.spring.service.MemberService;

@Controller
@RequestMapping("/commons/*")
public class CommonsActionController {
	
	@Autowired
	private MemberService memberService;
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

	//@RequestMapping(value= {"/","/commons/loginForm.do"})
	@RequestMapping("loginForm.do")
	public String loginForm() {
		String url = "commons/loginForm";
		return url;
	}
	
	@RequestMapping(value="login.do", method=RequestMethod.POST)
	public String login(String id, String pwd, Model model, HttpSession session) {
		String url = "redirect:/member/list.do";
		
		//로그인 실패시 추가한 attribute를 삭제.
		session.removeAttribute("msg");
		String message = null;
		
			try {
				memberService.login(id, pwd);
				
				MemberVO loginUser = memberService.getMember(id);
				if(loginUser.getEnabled() == 0) {
					message = "사용중지된 아이디로 이용이 제한됩니다.";
					url = "redirect:/commons/loginForm.do";
				}else { //사용가능
					session.setAttribute("loginUser", loginUser);
					session.setMaxInactiveInterval(60*6);					
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				message="서버 장애로 인해 접속이 불가합니다.";
				url = "error/500_error";
			} catch (NotFoundIDException | InvalidPasswordException e) {
				url = "redirect:/commons/loginForm.do";
				message="아이디 또는 비밀번호가 존재하지 않습니다";
			}
			
			session.setAttribute("msg", message);
			session.setAttribute("id", id);
			
		return url;
	}
	
	@RequestMapping("logout.do")
	public String logout(HttpSession session) throws Exception {
		String url = "redirect:/commons/loginForm.do";
		session.invalidate();
		return url;
	}
}
