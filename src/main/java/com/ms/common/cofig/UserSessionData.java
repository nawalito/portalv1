/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.common.cofig;
import java.io.Serializable;
import java.util.HashMap;
/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 01/febrero/2014
 * 
 */
public final class UserSessionData implements Serializable{
    private int userId;
    private String userIdCod;
    private String userName;
    
    public UserSessionData(HashMap<String, String> data) {
        this.setUserId(Integer.parseInt(data.get("id")));
        this.setUserIdCod(data.get("encod_id"));
        this.setUserName(data.get("username"));
    }
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserIdCod() {
        return userIdCod;
    }

    public void setUserIdCod(String userIdCod) {
        this.userIdCod = userIdCod;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
