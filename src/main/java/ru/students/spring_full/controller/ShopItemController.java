package ru.students.spring_full.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.students.spring_full.entity.ShopItem;
import ru.students.spring_full.entity.User;
import ru.students.spring_full.enums.ShopItemStatusEnum;
import ru.students.spring_full.repository.ShopItemRepository;
import ru.students.spring_full.repository.UserRepository;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Controller
public class ShopItemController {
    private final ShopItemRepository shopItemRepository;
    private final UserRepository userRepository;

    public ShopItemController(ShopItemRepository shopItemRepository, UserRepository userRepository) {
        this.shopItemRepository = shopItemRepository;
        this.userRepository = userRepository;
    }

    @GetMapping({"/"})
    public ModelAndView show(Principal principal) {
        User owner = getOwner(principal);

        return showCurrentState(owner);
    }

    @PostMapping("/add-shop-items")
    public ModelAndView addShopItem(@RequestParam String value, Principal principal) {
        var values = Arrays.stream(value.split("[.,]")).map(s -> s.trim()).toList();
        User owner = getOwner(principal);

        var entities = new ArrayList<ShopItem>();

        for (var item : values) {
            entities.add(new ShopItem(null, item, ShopItemStatusEnum.NEW, owner, OffsetDateTime.now(), OffsetDateTime.now(), null));
        }

        shopItemRepository.saveAllAndFlush(entities);

        return showCurrentState(owner);
    }

    @PostMapping("/set-status/{id}")
    public ModelAndView setStatus(@PathVariable Long id, @RequestParam String status, Principal principal) {
        User owner = getOwner(principal);
        var optionalItem = shopItemRepository.findById(id);

        if (optionalItem.isPresent()) {
            var item = optionalItem.get();

            if (item.getOwner().getId().equals(owner.getId())) {
                item.setStatus(ShopItemStatusEnum.valueOf(status));
                shopItemRepository.saveAndFlush(item);
            } else {
                return showCurrentState(owner, "Доступ запрещен");
            }
        } else {
            return showCurrentState(owner, "Неверный запрос");
        }

        return showCurrentState(owner);
    }

    @PostMapping("/set-status")
    public ModelAndView setStatus(@RequestParam String fromStatus, @RequestParam String toStatus, Principal principal) {
        User owner = getOwner(principal);

        int affected = shopItemRepository.setStatusAll(
            owner,
            Arrays.stream(fromStatus.split(",")).map(v -> ShopItemStatusEnum.valueOf(v)).toList(),
            ShopItemStatusEnum.valueOf(toStatus), OffsetDateTime.now()
        );

        return showCurrentState(owner, "Завершены " + affected + " пунктов");
    }

    @PostMapping("/delete")
    public ModelAndView delete(@RequestParam Long id, Principal principal) {
        User owner = getOwner(principal);
        var optionalItem = shopItemRepository.findById(id);

        if (optionalItem.isPresent()) {
            var item = optionalItem.get();

            if (item.getOwner().getId().equals(owner.getId())) {
                item.setDeletedAt(OffsetDateTime.now());
                shopItemRepository.saveAndFlush(item);
            } else {
                return showCurrentState(owner, "Доступ запрещен");
            }
        } else {
            return showCurrentState(owner, "Неверный запрос");
        }

        return showCurrentState(owner);
    }

    private User getOwner(Principal principal) {
        return userRepository.findByEmail(principal.getName());
    }

    private ModelAndView showCurrentState(User owner, String ...messages) {
        var mav = new ModelAndView("show");
        OffsetDateTime startAt = OffsetDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        OffsetDateTime endAt = startAt.plusDays(1);

        mav.addObject("items", shopItemRepository.findActiveItems(
                owner,
                startAt,
                endAt
        ));

        mav.addObject("suggestions", shopItemRepository.suggestRecent(owner, OffsetDateTime.now().minusMonths(1)));
        mav.addObject("messages", messages);

        return mav;
    }
}
