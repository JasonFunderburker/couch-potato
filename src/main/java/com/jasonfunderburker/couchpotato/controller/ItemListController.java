package com.jasonfunderburker.couchpotato.controller;

import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.service.torrents.TorrentsItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/")
public class ItemListController {
    private static final Logger logger = LoggerFactory.getLogger(ItemListController.class);
    @Autowired
    TorrentsItemService itemService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "redirect:/itemList";
    }

	@RequestMapping(value = "/itemList", method = RequestMethod.GET)
	public String showItemList(ModelMap model) {
        List<TorrentItem> torrentItemsList = itemService.getItemsList();
		model.addAttribute("message", "Hi, I'm couch potato, so i wrote this app cause i want to lay on my soft comfy coach and doing nothing when new episode of my favorite show is coming");
        model.addAttribute("itemList", torrentItemsList);
        model.addAttribute("torrentItem", new TorrentItem());
		return "hello";
	}

    @RequestMapping(value = "/itemList", method = RequestMethod.POST)
    public String addItemToList(TorrentItem torrentItem, ModelMap model) {
        logger.debug("addItemToList called");
        try {
            itemService.addItemToList(torrentItem);
            model.clear();
        }
        catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/itemList";
    }

    @RequestMapping(value = "/itemList/{id}/remove", method = RequestMethod.POST)
    public String deleteItemFromList(@PathVariable("id")long id, ModelMap model) {
        logger.debug("deleteItemFromList called");
        itemService.deleteItemFromList(id);
        return "redirect:/itemList";
    }

    @RequestMapping(value = "/itemList/check", method = RequestMethod.POST)
    public String itemListStartCheck(ModelMap model) {
        itemService.checkAllItems();
        return "redirect:/itemList";
    }
}