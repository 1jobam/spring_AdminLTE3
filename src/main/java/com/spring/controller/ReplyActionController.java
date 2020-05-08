package com.spring.controller;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.dto.ReplyVO;
import com.spring.request.DeleteReplyRequest;
import com.spring.request.ModifyReplyRequest;
import com.spring.request.PageMaker;
import com.spring.request.RegistReplyRequest;
import com.spring.request.SearchCriteria;
import com.spring.service.ReplyService;

@Controller
@RequestMapping("/replies/*")
public class ReplyActionController {
	
	@Autowired
	private ReplyService replyService;
	public void setReplyService(ReplyService replyService) {
		this.replyService = replyService;
	}
	
	@RequestMapping("list.do")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> list(int bno, SearchCriteria cri) throws Exception{
		ResponseEntity<Map<String,Object>> entity = null;
		
		try {
//			if(true) throw new SQLException();
			Map<String, Object> dataMap = replyService.getReplyList(bno, cri);
			entity = new ResponseEntity<Map<String,Object>>(dataMap,HttpStatus.OK);	
		}catch(SQLException e) {
			e.printStackTrace();
			entity = new ResponseEntity<Map<String,Object>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return entity;
	}
	
	@RequestMapping("regist.do")
	@ResponseBody
	public ResponseEntity<Integer> regist(@RequestBody RegistReplyRequest registReq) throws Exception{
		
//		String result = "SUCCESS";
		
		ResponseEntity<Integer> entity = null;
		
		ReplyVO reply = registReq.toReplyVO();
		
		try {
			replyService.registReply(reply);
			
//			if(true)throw new SQLException();
			
			Map<String, Object> dataMap = replyService.getReplyList(registReq.getBno(), new SearchCriteria());
			PageMaker pageMaker = (PageMaker)dataMap.get("pageMaker");
			int realEndPage = pageMaker.getRealEndPage();
			
			dataMap.put("realEndPage", pageMaker.getRealEndPage());
			entity = new ResponseEntity<Integer>(realEndPage,HttpStatus.OK);
//			result = "SUCCESS," + realEndPage;
		} catch (SQLException e) {
			e.printStackTrace();
			entity = new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
//			result = "FAIL,1";
		}
		
		return entity;
	}
	
	@RequestMapping("modify.do")
	@ResponseBody
	public ResponseEntity<String>modify(@RequestBody ModifyReplyRequest modifyReq) throws Exception{
		ResponseEntity<String> entity = null;
		
		ReplyVO reply = modifyReq.toReplyVO();
		
		try {
			replyService.modifyReply(reply);
			entity = new ResponseEntity<String>(HttpStatus.OK);
		}catch(SQLException e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return entity;
	}
	
	@RequestMapping("remove.do")
	@ResponseBody
	public ResponseEntity<Integer> remove(@RequestBody DeleteReplyRequest deleteReq) throws Exception {
		ResponseEntity<Integer> entity = null;
		
		try {
			replyService.removeReply(deleteReq.getRno());
			
			Map<String, Object> dataMap = replyService.getReplyList(deleteReq.getBno(), new SearchCriteria());
			
			PageMaker pageMaker = (PageMaker)dataMap.get("pageMaker");
			int page = deleteReq.getPage();
			int realEndPage = pageMaker.getRealEndPage();
			
			if(page>realEndPage) {
				page = realEndPage;
			}
			entity = new ResponseEntity<Integer>(page, HttpStatus.OK);
		}catch(SQLException e) {
			e.printStackTrace();
			entity = new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return entity;
	}

}
