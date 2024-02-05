package com.wittypuppy.backend.messenger.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class MessengerMainDTO {
    private Long messengerCode;
    private String messengerOption;
    private String messengerMiniAlarmOption;
    private String messengerTheme;
    private List<ChatroomMessengerMainDTO> chatroomList;

    public MessengerMainDTO setMessengerCode(Long messengerCode) {
        this.messengerCode = messengerCode;
        return this;
    }

    public MessengerMainDTO setMessengerOption(String messengerOption) {
        this.messengerOption = messengerOption;
        return this;
    }

    public MessengerMainDTO setMessengerMiniAlarmOption(String messengerMiniAlarmOption) {
        this.messengerMiniAlarmOption = messengerMiniAlarmOption;
        return this;
    }

    public MessengerMainDTO setMessengerTheme(String messengerTheme) {
        this.messengerTheme = messengerTheme;
        return this;
    }

    public MessengerMainDTO setChatroomList(List<ChatroomMessengerMainDTO> chatroomList) {
        this.chatroomList = chatroomList;
        return this;
    }

    public MessengerMainDTO builder() {
        return new MessengerMainDTO(messengerCode, messengerOption, messengerMiniAlarmOption, messengerTheme, chatroomList);
    }
}
