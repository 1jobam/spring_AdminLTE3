package com.spring.controller;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.dto.BoardVO;
import com.spring.request.SearchCriteria;
import com.spring.service.BoardService;

@Controller
@RequestMapping("/board/*")
public class BoardActionController {
	
	@Autowired
	private BoardService boardService;
	public void setBoardService(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@RequestMapping("list.do")
	public String list(SearchCriteria cri, Model model) throws Exception{
		String url = "board/list";
		
		Map<String, Object> dataMap = boardService.getBoardList(cri);
		model.addAllAttributes(dataMap);
		
		return url;
	}
	
	@RequestMapping(value="registForm.do", method=RequestMethod.GET)
	public String registForm() throws Exception{
		String url = "board/registBoard";
		return url;
	}
	
	@RequestMapping(value="regist.do", method=RequestMethod.POST)
	public String regist(BoardVO board) {
		String url = "board/regist_success";
		
		try {
			boardService.write(board);
		} catch (SQLException e) {
			e.printStackTrace();
			url = "board/regist_fail";
		}
		
		return url;
	}
	
	@RequestMapping(value="detail.do", method=RequestMethod.GET)
	public String detail(int bno, Model model) throws Exception {
		String url = "board/detailBoard";
		BoardVO board = boardService.getBoard(bno);
		model.addAttribute("board", board);
		return url;
	}
	
	@RequestMapping(value="modifyForm.do", method=RequestMethod.POST)
	public String modifyForm(int bno, Model model) throws Exception {
		String url = "board/modifyBoard";
		BoardVO board = boardService.getBoardForModify(bno);
		model.addAttribute("board", board);
		return url;
	}
	
	@RequestMapping(value="modify.do", method=RequestMethod.POST)
	public String modify(BoardVO board) {
		String url = "board/modify_success";
		
		try {
			boardService.modify(board);
		} catch (SQLException e) {
			e.printStackTrace();
			url = "board/modify_fail";
		}
		
		return url;
	}
	
	@RequestMapping("remove.do")
	public String remove(int bno) {
		String url = "board/remove_success";
		
		try {
			boardService.remove(bno);
		} catch (SQLException e) {
			e.printStackTrace();
			url = "board/remove_fail";
		}
		
		return url;
	}
}
