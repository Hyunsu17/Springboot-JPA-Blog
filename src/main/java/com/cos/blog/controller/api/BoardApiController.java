package com.cos.blog.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ReplySaveRequestDto;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.service.BoardService;



@RestController
public class BoardApiController {
	
	final int SUCCESS = 1;
	
	@Autowired
	private BoardService boardService;
	
	
	@Autowired
	private BCryptPasswordEncoder encode;
	
	@PostMapping("/api/board")
	public ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal) {
		boardService.글쓰기(board,principal.getUser());
		return new ResponseDto<Integer>(HttpStatus.OK.value(),SUCCESS);
	}
	
	@PostMapping("/api/board/{boardId}/reply")
	public ResponseDto<Integer> replySave(@RequestBody ReplySaveRequestDto replySaveRequestDto) {
				
		boardService.댓글쓰기(replySaveRequestDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(),SUCCESS);
	}
	
	@DeleteMapping("/api/board/{id}")
	public ResponseDto<Integer> deleteById(@PathVariable int id){
		boardService.삭제하기(id);
		return new ResponseDto<Integer>(HttpStatus.OK.value(),SUCCESS);
	}
	
	//데이터를 받을때 컨트롤러에서 dto를 만들어서 받는게 좋다.
	// dto 사용하지 않는 이유
	@PutMapping("/api/board/{id}")
	public ResponseDto<Integer> update(@PathVariable int id,@RequestBody Board board){
		boardService.글수정하기(id,board);
		return new ResponseDto<Integer>(HttpStatus.OK.value(),SUCCESS);
	}
	
	

}