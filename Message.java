import java.util.*;
import java.net.*;
import java.io.*;
import com.google.gson.*;

/**
 * Created by js on 22/12/16.
 */

public class Message {

    FLAG flag;
    int eventType;
    float x;
    float y;
    String message;

    public enum FLAG{
        MESSAGE, ACTION, FULLNAME, READY, START, ANSWER; //....
    }

    public Message(FLAG flag, int typeAction, float x, float y) {
        this.flag = flag;
        this.eventType = typeAction;
        this.x = x;
        this.y = y;
    }

    public Message(){}


    public Message(FLAG flag, String message) {
        this.flag = flag;

        this.message = message;
    }

    public FLAG getFlag() {
        return flag;
    }

    public void setFlag(FLAG flag) {
        this.flag = flag;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int typeAction) {
        this.eventType = typeAction;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}