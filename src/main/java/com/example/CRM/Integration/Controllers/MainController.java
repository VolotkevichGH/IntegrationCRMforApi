package com.example.CRM.Integration.Controllers;

import com.example.CRM.Integration.CRMBot;
import com.example.CRM.Integration.Telegram.APIConfig;
import com.example.CRM.Integration.Telegram.TelegramBot;
import com.example.CRM.Integration.Telegram.TelegramConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CRMBot CRMBot;
    private final TelegramBot telegramBot;
    private final APIConfig apiConfig;
    private final TelegramConfig telegramConfig;

    @GetMapping("/")
    public String home (Model model){
        return "index";
    }

    @SneakyThrows
    @PostMapping("/post")
    public String home2(Model model, @RequestParam String sphere, @RequestParam String Input, @RequestParam String desired_service) {
        String name = sphere;
        String tg = Input;
        if (!CRMBot.hasOrg(tg)){
            CRMBot.orgCreate(name,tg);
        }
        String orgId = CRMBot.getOrgIdByName(tg);

        if (!CRMBot.hasContact(tg)){
            CRMBot.contactCreate(tg, orgId);
        }

        String contId = CRMBot.getContIdByTg(tg);
        String message = "Поступила новая заявка: " +
                "\n\nСфера бизнеса: " + sphere +  "\n" +
                "Описание заказа: " + desired_service + "\n" +
                "Телеграм: " + Input + "\n" +
                "\n" +
                "https://automaticbusiness.site/\n" +
                "-----";
        CRMBot.dealCreate(name,orgId,contId, desired_service);
        telegramBot.sendMessage(telegramConfig.getIlyaChatId(), message);
        return "redirect:/";
    }



}
