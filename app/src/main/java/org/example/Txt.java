package org.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.Buffer;
import java.io.InputStreamReader;
import java.util.Objects;

public class Txt {
    public static String[] getIDs () {
        String[] ids = new String[6];

        InputStream txtStream =  Txt.class.getResourceAsStream("/links.txt");

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(txtStream)));
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null && index < ids.length) {
                ids[index++] = line.trim();
            }
            reader.close();
        } catch (Exception ignored) {}

        for (String id : ids) {
            System.out.println(id);
        }

        return ids;
    }
}
