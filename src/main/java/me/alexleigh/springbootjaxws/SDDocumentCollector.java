package me.alexleigh.springbootjaxws;

import com.sun.xml.ws.api.server.SDDocumentSource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SDDocumentCollector {

    public static Map<URL, Object> collectDocs(String dirPath, ClassLoader cl) {
        Map<URL, Object> docs = new HashMap<>();
        URL url = cl.getResource(dirPath);
        if (url != null) {
            if ("file".equals(url.getProtocol())) {
                File file;
                try {
                    file = new File(url.toURI());
                } catch (URISyntaxException e) {
                    file = new File(url.getPath());
                }
                doCollectDocs(file, docs);
            }
        }
        return docs;
    }

    private static void doCollectDocs(File dir, Map<URL, Object> docs) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    doCollectDocs(file, docs);
                } else {
                    String extension = getExtension(file);
                    if ("wsdl".equals(extension) || "xsd".equals(extension)) {
                        try {
                            URL url = file.toURI().toURL();
                            docs.put(url, SDDocumentSource.create(url));
                        } catch (MalformedURLException e) {
                            // do nothing
                        }
                    }
                }
            }
        }
    }

    private static String getExtension(File file) {
        String name = file.getName();
        int index = name.lastIndexOf(".");
        if (index > 0) {
            return name.substring(index + 1);
        }
        return "";
    }
}
