package me.alexleigh.springbootjaxws;

import com.sun.xml.ws.api.server.SDDocumentSource;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
                collectDir(file, docs);
            } else if ("jar".equals(url.getProtocol())) {
                String jarUrlString;
                try {
                    jarUrlString = url.toURI().getSchemeSpecificPart();
                } catch (URISyntaxException e) {
                    jarUrlString = url.getPath();
                }
                collectJar(jarUrlString, docs);
            }
        }
        return docs;
    }

    private static void collectDir(File dir, Map<URL, Object> docs) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    collectDir(file, docs);
                } else {
                    String extension = getExtension(file.getName());
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

    private static void collectJar(String jarUrlString, Map<URL, Object> docs) {
        String jarPathUrlString = jarUrlString.substring(0, jarUrlString.lastIndexOf('!'));
        String jarPath = jarPathUrlString.substring(5);
        String dirPath = jarUrlString.substring(jarUrlString.lastIndexOf('!') + 2);
        try {
            JarFile jar = new JarFile(jarPath);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    String name = entry.getName();
                    if (name.startsWith(dirPath)) {
                        String extension = getExtension(name);
                        if ("wsdl".equals(extension) || "xsd".equals(extension)) {
                            String urlString = jarPathUrlString + "!/" + name;
                            try {
                                URL url = new URI("jar", urlString, null).toURL();
                                docs.put(url, SDDocumentSource.create(url));
                            } catch (URISyntaxException | MalformedURLException e) {
                                // do nothing
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            // do nothing
        }
    }

    private static String getExtension(String name) {
        int index = name.lastIndexOf(".");
        if (index > 0) {
            return name.substring(index + 1);
        }
        return "";
    }
}
