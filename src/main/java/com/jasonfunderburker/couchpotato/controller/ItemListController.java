package com.jasonfunderburker.couchpotato.controller;

import com.jasonfunderburker.couchpotato.domain.ScheduleSettings;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.service.torrents.TorrentsItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Controller
@RequestMapping("/")
public class ItemListController {
    private static final Logger logger = LoggerFactory.getLogger(ItemListController.class);

    private final TorrentsItemService itemService;
    private final TaskScheduler scheduler;

    private ScheduleSettings scheduleSettings = new ScheduleSettings();
    private ScheduledFuture scheduledFuture;

    @Autowired
    public ItemListController(TorrentsItemService itemService, TaskScheduler scheduler) {
        this.itemService = itemService;
        this.scheduler = scheduler;
    }

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
        model.addAttribute("scheduleSettings", scheduleSettings);
        model.addAttribute("checkStartDate", itemService.getCheckStartDate());
        model.addAttribute("checkEndDate", itemService.getCheckEndDate());
        if (!model.containsAttribute("generatedRssUrl")) model.addAttribute("generatedRssUrl","");
        logger.trace("modelMap: {}",model);
		return "itemList";
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

    @RequestMapping(value = "/itemList/{id}/check", method = RequestMethod.POST)
    public String checkItemById(@PathVariable("id")long id, ModelMap model) {
        logger.debug("Check id={} now", id);
        itemService.checkItem(id);
        return "redirect:/itemList";
    }

    @RequestMapping(value = "/itemList/checkAll", method = RequestMethod.POST)
    public String itemListStartCheck(ModelMap model) {
        logger.debug("Start check now");
        itemService.checkAllItems();
        return "redirect:/itemList";
    }

    @RequestMapping(value = "/itemList/scheduleCheck", method = RequestMethod.POST)
    public String itemListScheduleCheck(ScheduleSettings scheduleSettings) {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
        logger.debug("Schedule check on time: {} every day", scheduleSettings.getScheduleTime());
        this.scheduleSettings = scheduleSettings;
        String[] timeParts = scheduleSettings.getScheduleTime().split(":");
        String hours = timeParts[0];
        String minutes = timeParts[1];
        scheduledFuture = scheduler.schedule(itemService::checkAllItems, new CronTrigger("0 "+minutes+" "+hours+" * * *"));
        return "redirect:/itemList";
    }
}