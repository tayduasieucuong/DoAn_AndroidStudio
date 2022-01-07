package org.meicode.doan_android.chat;

import java.util.Date;

public class ChatList {
    Date messageDate;
    String content, nickname, idUser;

    public ChatList(Date messageDate, String content, String nickname, String idUser) {
        this.messageDate = messageDate;
        this.content = content;
        this.nickname = nickname;
        this.idUser = idUser;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
