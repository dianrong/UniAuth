package com.dianrong.common.uniauth.common.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class UniauthResourceControl extends ResourceBundle.Control {

  private static String BASE_PATH = "META-INF/resources/";

  @Override
  public ResourceBundle newBundle(final String baseName, final Locale locale, String format,
      final ClassLoader loader, final boolean reload)
      throws IllegalAccessException, InstantiationException, IOException {
    if (format.equals("java.properties") || format.equals("java.class")) {

      InputStream stream;
      try {
        stream = AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {
          @Override
          public InputStream run() throws IOException {
            InputStream is = null;
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            if (reload) {
              URL resource = loader.getResource(BASE_PATH + resourceName);
              if (resource != null) {
                URLConnection connection = resource.openConnection();
                if (connection != null) {
                  connection.setUseCaches(false);
                  is = connection.getInputStream();
                }
              }
            } else {
              is = loader.getResourceAsStream(BASE_PATH + resourceName);
            }
            return is;
          }
        });
        if (stream != null) {
          try {
            return new PropertyResourceBundle(stream);
          } finally {
            stream.close();
          }
        } else {
          return null;
        }
      } catch (PrivilegedActionException ex) {
        throw (IOException) ex.getException();
      }
    } else {
      return super.newBundle(baseName, locale, format, loader, reload);
    }

  }

}
