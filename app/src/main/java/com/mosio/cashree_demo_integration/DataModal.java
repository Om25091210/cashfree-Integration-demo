package com.mosio.cashree_demo_integration;

public class DataModal {
    String customer_id,customer_email,customer_phone,order_note;
    int order_amount;
    String order_token;
    private ResponseData responseData;

    public DataModal(String customer_id, String customer_email, String customer_phone, String order_note, int order_amount) {
        this.customer_id = customer_id;
        this.customer_email = customer_email;
        this.customer_phone = customer_phone;
        this.order_note = order_note;
        this.order_amount = order_amount;
    }

    public String getOrder_token() {
        return order_token;
    }

    public void setOrder_token(String order_token) {
        this.order_token = order_token;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getOrder_note() {
        return order_note;
    }

    public void setOrder_note(String order_note) {
        this.order_note = order_note;
    }

    public int getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(int order_amount) {
        this.order_amount = order_amount;
    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public class ResponseData {

        private String message;
        public Data data;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    public class Data {

        private int application_id;

        public int getApplication_id() {
            return application_id;
        }
        public void setApplication_id(int application_id) {
            this.application_id = application_id;
        }
    }
}
