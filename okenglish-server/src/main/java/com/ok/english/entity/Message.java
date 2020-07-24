package com.ok.english.entity;
public class Message {
        MessageCode    code;
        String info;
        Object data;
        public Message(){

        }
        public Message(MessageCode code,String info,Object data){
            this.code=code;
            this.info=info;
            this.data=data;
        }
        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

    public MessageCode getCode() {
        return code;
    }

    public void setCode(MessageCode code) {
        this.code = code;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
