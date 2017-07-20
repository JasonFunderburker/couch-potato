package com.jasonfunderburker.couchpotato.controller;

import com.jasonfunderburker.couchpotato.entities.ScheduleSettings;
import com.jasonfunderburker.couchpotato.entities.TorrentItem;
import com.jasonfunderburker.couchpotato.service.torrents.TorrentsItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

@RestController
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
/*
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "redirect:/itemList";
    } */

	@RequestMapping(value = "/itemList", method = RequestMethod.GET)
	public List<TorrentItem> getItemList() {
	    return itemService.getItemsList();
	}

    @RequestMapping(value = "/itemList", method = RequestMethod.POST)
    public void addItemToList(TorrentItem torrentItem) {
        logger.debug("addItemToList called");
        itemService.addItemToList(torrentItem);
    }

    @RequestMapping(value = "/itemList/{id}/remove", method = RequestMethod.DELETE)
    public void deleteItemFromList(@PathVariable("id")long id) {
        logger.debug("deleteItemFromList called");
        itemService.deleteItemFromList(id);
    }

    @RequestMapping(value = "/itemList/{id}/check", method = RequestMethod.POST)
    public void checkItemById(@PathVariable("id")long id) {
        logger.debug("Check id={} now", id);
        itemService.checkItem(id);
    }

    @RequestMapping(value = "/itemList/checkAll", method = RequestMethod.POST)
    public void itemListStartCheck() {
        logger.debug("Start check now");
        itemService.checkAllItems();
    }

    @RequestMapping(value = "/itemList/scheduleCheck", method = RequestMethod.POST)
    public void itemListScheduleCheck(ScheduleSettings scheduleSettings) {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
        logger.debug("Schedule check on time: {} every day", scheduleSettings.getScheduleTime());
        this.scheduleSettings = scheduleSettings;
        String[] timeParts = scheduleSettings.getScheduleTime().split(":");
        String hours = timeParts[0];
        String minutes = timeParts[1];
        scheduledFuture = scheduler.schedule(itemService::checkAllItems, new CronTrigger("0 "+minutes+" "+hours+" * * *"));
    }
}