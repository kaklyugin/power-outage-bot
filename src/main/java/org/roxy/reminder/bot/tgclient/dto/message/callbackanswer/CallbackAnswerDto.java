package org.roxy.reminder.bot.tgclient.dto.message.callbackanswer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CallbackAnswerDto {

    @JsonProperty("callback_query_id")
    private String  callbackQueryId;
    private String  text;
    @JsonProperty("show_alert")
    private boolean  showAlert;

}