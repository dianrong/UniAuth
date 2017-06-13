package gradle;

import static org.apache.http.client.fluent.Request.Get;
import static org.apache.http.client.fluent.Request.Put;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;
import com.google.common.io.CharStreams;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

public class TomcatDeploy {

  public static String deploy(boolean update, String appname, String filepath, String username,
      String password, String host) throws IOException {
    return doHttpRequest(
        Put("http://" + host + "/manager/text/deploy?path=/" + appname + "&update=" + update)
            .bodyFile(new File(filepath), ContentType.DEFAULT_BINARY),
        username, password);
  }


  public static String undeploy(String appname, String username, String password, String host)
      throws IOException {
    return doHttpRequest(Get("http://" + host + "/manager/text/undeploy?path=/" + appname),
        username, password);
  }

  public static void main(String... args) throws IOException {
    switch (args[0]) {
      case "deploy":
        deploy(false, args[1], args[2], args[3], args[4], args[5]);
        break;
      case "undeploy":
        undeploy(args[1], args[2], args[3], args[4]);
        break;
      default:
        deploy(true, args[1], args[2], args[3], args[4], args[5]);
    }
  }

  private static String doHttpRequest(Request base, String username, String password)
      throws IOException {
    return CharStreams.toString(new InputStreamReader(base
        .addHeader("Authorization",
            "Basic " + BaseEncoding.base64().encode((username + ":" + password).getBytes()))
        .execute()
        .returnContent()
        .asStream(), Charsets.UTF_8));
  }
}
