package com.example.CRM.Integration.Telegram;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TelegramConfig {

    private String token = "TOKEN";
    private String botName = "ИИ Ассистент | Automatic business";
    private Long ilyaChatId = 123123L;
    private String tokenAB = "TOKEN";


}
