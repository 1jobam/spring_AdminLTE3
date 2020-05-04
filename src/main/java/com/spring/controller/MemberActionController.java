package com.spring.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.dto.MemberVO;
import com.spring.request.SearchCriteria;
import com.spring.service.MemberService;

@Controller
@RequestMapping("/member/*")
public class MemberActionController {
	
	@Autowired
	private MemberService memberService;
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}
	
	@RequestMapping("list.do")
	public void list(SearchCriteria cri, Model model) throws Exception{
		Map<String, Object> dataMap = memberService.getMemberList(cri);
//		model.addAttribute("pageMaker", (PageMaker)dataMap.get("pageMaker"));
//		model.addAttribute("memberList", (List<MemberVO>)dataMap.get("memberList"));
		model.addAllAttributes(dataMap);
	}
	
	@RequestMapping("detail.do")
	public void detail(String id, HttpServletRequest request) throws Exception {
		MemberVO member = memberService.getMember(id);
//		, Model model
//		model.addAttribute(member);
		request.setAttribute("member", member);
	}

}
