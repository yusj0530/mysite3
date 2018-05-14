package com.ssub.mysite.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ssub.mysite.vo.GuestbookVo;

@Repository
public class GuestbookDao {
	@Autowired
	private SqlSession sqlSession;
	
	public int delete( GuestbookVo vo ) {
		int count = sqlSession.delete("guestbook.delete", vo);
		return count;		
	}
	
	public int insert(GuestbookVo vo) {
		int count = sqlSession.insert( 
				"guestbook.insert", vo);
		System.out.println( "no--->" + vo.getNo() );
		return count;
	}

	public List<GuestbookVo> getList() {
		List<GuestbookVo> list = sqlSession.selectList("guestbook.getList");
		return list;
	}
	
	public List<GuestbookVo> getList(Long no) {
		List<GuestbookVo> list = sqlSession.selectList("guestbook.getList2", no);
		return list;
	}	
}
