package com.jasonfunderberker.couchpotato;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HelloController {
	@RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hi, I'm couch potato, so i wrote this app cause i want to lay on my soft comfy coach and doing nothing when new episode of my favorite show is coming");
		return "hello";
	}
}