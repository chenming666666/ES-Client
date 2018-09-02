package com.example.client.test;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.client.demo.Demo;

@RestController	 //界面显示必备
public class TestController {

	@GetMapping("/test")
	public SearchResponse show() {
		return Demo.termQuery();
	}
}
