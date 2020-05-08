package com.spring.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.dto.MemberVO;
import com.spring.request.SearchCriteria;
import com.spring.service.MemberService;
import com.spring.utils.GetUploadPath;

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
		model.addAllAttributes(dataMap);
	}
	
	@RequestMapping("detail.do")
	public void detail(String id, Model model) throws Exception {
		MemberVO member = memberService.getMember(id);
		model.addAttribute("member", member);
	}
	
	@RequestMapping(value="registForm.do", method=RequestMethod.GET)
	public String registForm() throws Exception {
		String url = "member/regist";
		return url;
	}
	
	@RequestMapping(value="regist.do", method=RequestMethod.POST)
	public String regist(MemberVO member) {
		String url = "member/regist_success";
		try {
			memberService.regist(member);
		} catch (SQLException e) {
			e.printStackTrace();
			url = "member/regist_fail";
		}
		return url;
	}
	
	@RequestMapping(value="modifyForm.do", method=RequestMethod.GET)
	public String modifyForm(String id, Model model) throws Exception {
		String url = "member/modify";
		MemberVO member = memberService.getMember(id);
		model.addAttribute("member", member);
		return url;
	}
	
	@RequestMapping(value="remove.do", method=RequestMethod.GET)
	public String remove(String id) {
		String url = "member/remove_success";
		try {
			memberService.remove(id);
		} catch (SQLException e) {
			e.printStackTrace();
			url = "member/remove_fail";
		}
		return url;
	}
	
	@RequestMapping(value="disabled.do", method=RequestMethod.GET)
	public String disabled(String id) {
		String url = "member/disabled_success";
		try {
			memberService.disabledMember(id);
		} catch (SQLException e) {
			e.printStackTrace();
			url = "member/disabled_fail";
		}
		return url;
	}
	
	@RequestMapping(value="enabled.do", method=RequestMethod.GET)
	public String enabled(String id) {
		String url = "member/enabled_success";
		try {
			memberService.enabledMember(id);
		} catch (SQLException e) {
			e.printStackTrace();
			url = "member/enabled_fail";
		}
		return url;
	}
	
	@RequestMapping("checkPassword.do")
	public void checkPassword(String pwd, HttpSession session, HttpServletResponse response) throws Exception {
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		
		String result = null;
		
		if(pwd.equals(loginUser.getPwd())) {
			result = "SUCCESS";
		}		
		response.getWriter().print(result);
	}
	
	@RequestMapping("picture/get.do")
	public void getPicture(HttpServletRequest request, HttpServletResponse response) throws Exception {
		sendOkFile(request, response);
	}
	
	private void sendOkFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String fileName = request.getParameter("picture");
		String savedPath = GetUploadPath.getUploadPath("member.picture.upload");

		String filePath = savedPath + fileName;

		sendFile(request,response, filePath);
	}

	private void sendFile(HttpServletRequest request, HttpServletResponse response, String filePath) throws ServletException, IOException {

		// 보낼 파일 설정.
		File downloadFile = new File(filePath);
		FileInputStream inStream = new FileInputStream(downloadFile);

		ServletContext context = request.getSession().getServletContext();

		// 파일 포맷으로 MIME를 결정한다.
		String mimeType = context.getMimeType(filePath);
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		System.out.println("MIME type : " + mimeType);

		// response 수정.
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());

		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		response.setHeader(headerKey, headerValue);

		// 파일 내보내기
		OutputStream outStream = response.getOutputStream();
		byte[] buffer = new byte[4096];
		int bytesRead = -1;

		while ((bytesRead = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		if (inStream != null)
			inStream.close();
		if (outStream != null)
			outStream.close();

	}

}
