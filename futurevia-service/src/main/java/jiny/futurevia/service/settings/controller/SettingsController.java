package jiny.futurevia.service.settings.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jiny.futurevia.service.account.domain.entity.Account;
import jiny.futurevia.service.account.support.CurrentUser;

@Controller
public class SettingsController {

    @GetMapping("/settings/profile")
    public String profileUpdateForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(Profile.from(account));
        return "settings/profile";
    }
}