package com.ok.english.entity;


public class MessageBuilder {
    private static Message message = new Message();
    private static MessageBuilder builder = new MessageBuilder();

    public static MessageBuilder addCode(MessageCode code) {
        builder.message.setCode(code);
        return builder;
    }

    public static MessageBuilder addInfo(String info) {
        builder.message.setInfo(info);
        return builder;
    }

    public static MessageBuilder addData(Object data) {
        builder.message.setData(data);
        return builder;
    }

    public static Message build() {
        Message builtMsg = builder.message;
        message = new Message();
        return builtMsg;
    }

    public static Message buildSimpleMessage(MessageCode code, String info) {
        return new Message(code, info, new String[]{});
    }
}