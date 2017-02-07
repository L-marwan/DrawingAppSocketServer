import java.util.*;
import com.google.gson.Gson;

/**
 * Created by L.marouane on 26/12/2016.
 */
public class Utils {

    public static String messageToJason(Message msg){

        return new Gson().toJson(msg);
    }

    public static Message jsonToMessage (String json){
        return new Gson().fromJson(json,Message.class);
    }
}
