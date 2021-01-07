package com.petmarkets2020.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.petmarkets2020.model.Comments;
import com.petmarkets2020.service.CommentServices;

@RestController
public class CommentController {
	@Autowired
	CommentServices commentServices;

	@GetMapping("/getComments")
	@ResponseBody
	public Comments getComments(@RequestParam String userId) throws InterruptedException, ExecutionException {
		return commentServices.getComments(userId);
		
	}
}
