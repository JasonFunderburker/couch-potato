package com.jasonfunderburker.couchpotato.controller;

import com.jasonfunderburker.couchpotato.dao.TorrentItemDao;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HelloController {
    @Autowired
    TorrentItemDao torrentItemDao;

	@RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
        TorrentItem torrentItem = torrentItemDao.findById(1L);
		model.addAttribute("message", "Hi, I'm couch potato, so i wrote this app cause i want to lay on my soft comfy coach and doing nothing when new episode of my favorite show is coming");
        model.addAttribute("id", torrentItem.getId());
        model.addAttribute("name", torrentItem.getName());
		return "hello";
	}
}