package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

public class SheetsAndJava {
    private static Sheets sheetsService;
    private static final String APPLICATION_NAME = "Google Sheets Example";
    private static final String SPREADSHEET_ID = "1pLgKaAENH1mYW3fAKpcGq8GSCt0V8epyuVdbBg2caqk";

    private static Credential authorize() throws IOException, GeneralSecurityException {
        InputStream inputStream = SheetsAndJava.class.getResourceAsStream("/credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new InputStreamReader(Objects.requireNonNull(inputStream)));

        List<String> scopes = List.of(SheetsScopes.SPREADSHEETS);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), clientSecrets, scopes).setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens"))).setAccessType("offline").build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), credential).setApplicationName(APPLICATION_NAME).build();
    }

    public static void start() throws GeneralSecurityException, IOException {
        Professor[] professors = new Professor[31];
        for (int i = 0; i < professors.length; i++)
            professors[i] = new Professor();

        SheetsAndJava.getNames(professors);
        SheetsAndJava.getHours(professors, "matrizITI!F4:AJ81", "ITI");
        SheetsAndJava.getHours(professors, "matrizIM!F4:AJ81", "IM");
        SheetsAndJava.getHours(professors, "matrizITM!F4:AJ81", "ITM");
        SheetsAndJava.getHours(professors, "matrizISA!F4:AJ81", "ISA");
        SheetsAndJava.getHours(professors, "matrizLAyGE!F4:AJ81", "LAyGE");
        SheetsAndJava.getHours(professors, "matrizComercio!F4:AJ81", "Comercio");

        int totalHours = 0;
        for (Professor professor : professors) {
            int tmpTotalHours = (professor.getITIHours() + professor.getIMHours() + professor.getITMHours() + professor.getISAHours() + professor.getLAyGEHours() + professor.getComercioHours());
            System.out.printf("Name: %20s, ITIHours: %3d, IMHours: %3d, ITMHours: %3d, ISAHours: %3d, LAyGEHours: %3d, comercioHours: %3d, Total hours: %d\n", professor.getName(), professor.getITIHours(), professor.getIMHours(), professor.getITMHours(), professor.getISAHours(), professor.getLAyGEHours(), professor.getComercioHours(), tmpTotalHours);

            totalHours += tmpTotalHours;
        }

        System.out.println("Total of hours: " + totalHours);
    }

    public static void getNames(Professor[] professors) throws GeneralSecurityException, IOException {
        sheetsService = getSheetsService();
        String namesRange = "matrizITI!F2:AJ2";
        ValueRange namesResponse = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, namesRange).execute();

        List<List<Object>> names = namesResponse.getValues();

        if (names == null || names.isEmpty()) {
            System.out.println("No data found");
            return;
        }

        for (List<Object> row : names) {
            for (int i = 0; i < row.size(); i++) {
                String name = row.get(i).toString();
                professors[i].setName(name);
            }
        }
    }

    public static void getHours(Professor[] professors, String range, String career) throws IOException, GeneralSecurityException {
        sheetsService = getSheetsService();
        ValueRange response = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, range).execute();

        List<List<Object>> values = response.getValues();

        if ((values == null || values.isEmpty())) {
            System.out.println("No data found");
            return;
        }

        for (List<Object> row : values) {
            for (int j = 0; j < row.size(); j++) {
                Object item = row.get(j);
                int itemInteger = ExceptionHandling.toInt(item);
                if (career.equals("ITI")) professors[j].setITIHours(professors[j].getITIHours() + itemInteger);
                if (career.equals("IM")) professors[j].setIMHours(professors[j].getIMHours() + itemInteger);
                if (career.equals("ITM")) professors[j].setITMHours(professors[j].getITMHours() + itemInteger);
                if (career.equals("ISA")) professors[j].setISAHours(professors[j].getISAHours() + itemInteger);
                if (career.equals("LAyGE")) professors[j].setLAyGEHours(professors[j].getLAyGEHours() + itemInteger);
                if (career.equals("Comercio")) professors[j].setComercioHours(professors[j].getComercioHours() + itemInteger);
            }
        }
    }
}
