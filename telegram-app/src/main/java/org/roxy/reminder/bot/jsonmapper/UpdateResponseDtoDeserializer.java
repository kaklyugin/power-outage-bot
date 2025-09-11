package org.roxy.reminder.bot.jsonmapper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.roxy.reminder.bot.tgclient.dto.updates.UpdateResponseDto;
import org.roxy.reminder.bot.tgclient.dto.updates.UpdateType;

import java.io.IOException;
import java.util.stream.StreamSupport;


public class UpdateResponseDtoDeserializer extends StdDeserializer<UpdateResponseDto> {
    //FIXME
    public UpdateResponseDtoDeserializer() {
        this(null);
    }

    public UpdateResponseDtoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public UpdateResponseDto deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        UpdateResponseDto resultUpdateResponseDto = new UpdateResponseDto();
        JsonNode node = jp.getCodec().readTree(jp);
        resultUpdateResponseDto.setUpdateId(node.get("update_id").asLong());

        if (node.has("message") && !node.at("/message").has("entities")) {

            resultUpdateResponseDto.setUpdateType(UpdateType.TEXT);
            resultUpdateResponseDto.setFromId(node.at("/message/from/id").asLong());
            resultUpdateResponseDto.setChatId(node.at("/message/chat/id").asLong());
            resultUpdateResponseDto.setDate(node.at("/message/date").asLong());
            resultUpdateResponseDto.setUserResponse(node.at("/message/text").asText());

        } else if (node.has("message") && node.at("/message").has("entities")) {

            JsonNode entity = StreamSupport
                    .stream(node.at("/message/entities").spliterator(), false)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Entities are empty"));
            if (entity.get("type").asText().equals("bot_command")) {
                resultUpdateResponseDto.setUpdateType(UpdateType.COMMAND);
                resultUpdateResponseDto.setFromId(node.at("/message/from/id").asLong());
                resultUpdateResponseDto.setChatId(node.at("/message/chat/id").asLong());
                resultUpdateResponseDto.setDate(node.at("/message/date").asLong());
                resultUpdateResponseDto.setUserResponse(node.at("/message/text").asText());

            }
        } else if (node.has("callback_query")) {

            resultUpdateResponseDto.setUpdateType(UpdateType.CALLBACK);
            resultUpdateResponseDto.setFromId(node.at("/callback_query/message/from/id").asLong());
            resultUpdateResponseDto.setChatId(node.at("/callback_query/message/chat/id").asLong());
            resultUpdateResponseDto.setDate(node.at("/callback_query/message/date").asLong());
            resultUpdateResponseDto.setUserResponse(node.at("/callback_query/data").asText());
            resultUpdateResponseDto.setSourceMessageId(node.at("/callback_query/message/message_id").asLong());
            resultUpdateResponseDto.setCallbackQueryId(node.at("/callback_query/id").asText());

        } else
            throw new JsonParseException("Cannot define update type");
        return resultUpdateResponseDto;
    }
}