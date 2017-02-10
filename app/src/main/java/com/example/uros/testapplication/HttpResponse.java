package com.example.uros.testapplication;

/**
 * Created by Uros on 2/10/2017.
 */
public class HttpResponse {
    private boolean sucess;
    private String message;

    public HttpResponse() {
    }

    public HttpResponse(String message, boolean sucess) {
        this.message = message;
        this.sucess = sucess;
    }

    public boolean isSucess() {
        return sucess;
    }

    public void setSucess(boolean sucess) {
        this.sucess = sucess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
