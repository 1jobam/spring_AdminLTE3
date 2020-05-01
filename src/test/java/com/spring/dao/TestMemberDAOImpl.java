package com.spring.dao;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dto.MemberVO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/spring/context/root-context.xml")
@TransactionConfiguration(transactionManager="transactionManager")
@Transactional
public class TestMemberDAOImpl {

	@Autowired
	private MemberDAO memberDAO;
	
	@Test
	public void testSelectMember()throws SQLException{
		String id = "mimi";
		MemberVO member = memberDAO.selectMemberById(id);
			
		Assert.assertEquals(id, member.getId());
	}
	
	@Test
	public void testInsertMember() throws SQLException{
		MemberVO member = new MemberVO();
		member.setId("kaka");
		member.setName("kaka");
		member.setPwd("kaka");
		member.setEmail("kaka@kaka.com");
		member.setPhone("010-1234-1234");
		member.setPicture("noImage.jpg");
		
		memberDAO.insertMember(member);
		
		MemberVO result = memberDAO.selectMemberById("kaka");
		
		Assert.assertEquals(member.getId(), result.getId());
	}
	
}
