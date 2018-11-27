package com.qm.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MvcController {
	/**
	 * thymeleaf demo
	 * @return
	 */
	@GetMapping("/demo7")
	public String demo7() {
		return "/index";
	}
}
