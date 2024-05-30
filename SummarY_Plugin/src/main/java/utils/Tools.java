package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.ui.Messages;
import entity.R;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

import static utils.GlobalSetting.*;

public class Tools {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void saveLocalProperties(String key, String value) {
        PropertiesComponent.getInstance().setValue(key, value);
    }

    public static String getLocalProperties(String key) {
        return PropertiesComponent.getInstance().getValue(key);
    }

    public static String object2Json(Object obj) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(obj);
    }

    public static <T> T json2Obj(String str, Class<T> clazz) throws JsonProcessingException {
        if (StringUtils.isEmpty(str) || clazz == null){
            return null;
        }
        return clazz.equals(String.class) ? (T) str : OBJECT_MAPPER.readValue(str,clazz);
    }

    public static void showMessage(R r, JPanel panel) {
        if (r.getCode()==SUCCESS_CODE) {
            String msg = "Success";
            if (r.getMsg() != null) {
                msg = r.getMsg();
            }
            JOptionPane.showMessageDialog(panel, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
        } else if (r.getCode()==FAIL_CODE) {
            String msg = "Fail";
            if (r.getMsg() != null) {
                msg = r.getMsg();
            }
            JOptionPane.showMessageDialog(panel, msg, "Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static R httpPostHelper(String msg, String subUrl) throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost hPost = new HttpPost(SERVICE_LOCATION+subUrl);
        hPost.setHeader("Content-Type", "application/json; charset=UTF-8");
        hPost.setHeader("Accept", "*/*");
        hPost.setHeader("ActivationCode", getLocalProperties(ACTIVATE_CODE));
        StringEntity strEntity = new StringEntity(msg, "UTF-8");
        hPost.setEntity(strEntity);

        // 获取响应
        HttpResponse response = httpclient.execute(hPost);
        response.setHeader("Content-type", "application/json; charset=UTF-8");

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            httpclient.getConnectionManager().shutdown();
            return null;
        }

        String r = EntityUtils.toString(response.getEntity(), "UTF-8");
        httpclient.getConnectionManager().shutdown();
        return OBJECT_MAPPER.readValue(r, R.class);

    }
}
