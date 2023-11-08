package com.example.CRM.Integration.Telegram;

import com.example.CRM.Integration.ChatBot;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramConfig telegramConfig;
    private final APIConfig apiConfig;
    private final ChatBot chatBot;



    @Override
    public String getBotUsername() {
        return telegramConfig.getBotName();
    }

    @Override
    public String getBotToken(){
        return telegramConfig.getTokenAB();
    }
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if (update.getMessage().getText().equals("/adminstart")){
            sendMessage(telegramConfig.getIlyaChatId(), "Бот активирован");
        }else if (update.getMessage().getText().contains("/createChatBot")) {
            String[] massive = update.getMessage().getText().split(" ");
            if (massive.length < 3) {
                sendMessage(update.getMessage().getChatId(), "Команда: /createChatBot [name(Одним словом)] [description]");
            } else {
                String name = massive[1];
                String description = "";
                for (int i = 2; i < massive.length; i++){
                    description += massive[i] + " ";
                }

                sendMessage(apiConfig.getMy_ID_Telegram(), "Бот создается");
                chatBot.botCreate(name);
                chatBot.botUpdate(name, description, chatBot.getBotId());
                sendMessage(update.getMessage().getChatId(), "Бот создан. \n\n" +
                        "Название: " + name + "\n" +
                        "Описание: " + description + "\n" +
                        "ID бота: " + chatBot.getBotId());
            }
        }

    }

    public void sendMessage(long chatId, String textToSend) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        execute(message);

    }

}
