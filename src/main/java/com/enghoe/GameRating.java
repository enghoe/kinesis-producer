package com.enghoe;


import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by user on 6/25/2017.
 */
public class GameRating implements Serializable {

    int deviceId;
    int rating;
    String gameTitle;
    //String timeStamp;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

/*
    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
*/

}
