package com.jasonfunderburker.couchpotato.controller;

import com.jasonfunderburker.couchpotato.entities.ScheduleSettings;
import com.jasonfunderburker.couchpotato.entities.TorrentItem;
import com.jasonfunderburker.couchpotato.entities.UserDO;
import com.jasonfunderburker.couchpotato.repositories.SingleUserRepository;
import com.jasonfunderburker.couchpotato.service.torrents.TorrentsItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/itemList")
public class ItemListController {
    private static final Logger logger = LoggerFactory.getLogger(ItemListController.class);

    private final TorrentsItemService itemService;
    private final TaskScheduler scheduler;
    private final SingleUserRepository userRepo;

    private ScheduleSettings scheduleSettings = new ScheduleSettings();
    private ScheduledFuture scheduledFuture;

    @Autowired
    public ItemListController(TorrentsItemService itemService, TaskScheduler scheduler, SingleUserRepository userRepo) {
        this.itemService = itemService;
        this.scheduler = scheduler;
        this.userRepo = userRepo;
        scheduleCheckIfNeed();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
	public List<TorrentItem> getItemList() {
	    return itemService.getItemsList();
	}

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void addItemToList(@RequestBody TorrentItem torrentItem) {
        logger.debug("addItemToList called");
        itemService.addItemToList(torrentItem);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteItemFromList(@PathVariable("id")long id) {
        logger.debug("deleteItemFromList called");
        itemService.deleteItemFromList(id);
    }

    @RequestMapping(value = "/{id}/check", method = RequestMethod.POST)
    public void checkItemById(@PathVariable("id")long id) {
        logger.debug("Check id={} now", id);
        itemService.checkItem(id);
    }

    @RequestMapping(value = "/checkAll", method = RequestMethod.POST)
    public void itemListStartCheck() {
        logger.debug("Start check now");
        itemService.checkAllItems();
    }

    @RequestMapping(value = "/scheduleCheck", method = RequestMethod.POST)
    public void itemListScheduleCheck(@RequestBody ScheduleSettings scheduleSettings, Principal principal) {
        scheduleCheck(scheduleSettings);
        UserDO userDO = userRepo.findByUsername(principal.getName());
        userDO.setSettings(scheduleSettings);
        userRepo.saveAndFlush(userDO);
    }

    @RequestMapping(value = "/scheduleCheck", method = RequestMethod.GET)
    public ScheduleSettings getScheduleSettings() {
        return scheduleSettings;
    }

    private void scheduleCheck(ScheduleSettings scheduleSettings) {
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

    private void scheduleCheckIfNeed() {
        userRepo.findFirstByOrderById()
                .map(UserDO::getSettings)
                .filter(settings -> nonNull(settings.getScheduleTime()))
                .ifPresent(this::scheduleCheck);
    }
}