package com.example.CRM.Integration.Telegram;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
public class APIConfig {

    private Long my_ID_Telegram = 998920048L;
    private Long chatBotID = 1181247366L;

}
