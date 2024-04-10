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
    private Sheets sheetsService;
    private final String APPLICATION_NAME = "Google Sheets Example";
    private final String SPREADSHEET_ID = "1pLgKaAENH1mYW3fAKpcGq8GSCt0V8epyuVdbBg2caqk";
    private Professor[] professors;

    public SheetsAndJava() {
        this.professors = new Professor[31];
    }

    public Professor[] getProfessors() {
        return professors;
    }

    public void setProfessors(Professor[] professors) {
        this.professors = professors;
    }

    private Credential authorize() throws IOException, GeneralSecurityException {
        InputStream inputStream = SheetsAndJava.class.getResourceAsStream("/credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new InputStreamReader(Objects.requireNonNull(inputStream)));

        List<String> scopes = List.of(SheetsScopes.SPREADSHEETS);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), clientSecrets, scopes).setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens"))).setAccessType("offline").build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), credential).setApplicationName(APPLICATION_NAME).build();
    }

    public void start() throws GeneralSecurityException, IOException {
        for (int i = 0; i < this.professors.length; i++)
            this.professors[i] = new Professor();

        this.getNames();
        this.getHours("matrizITI!F4:AJ81", "ITI");
        this.getHours("matrizIM!F4:AJ81", "IM");
        this.getHours("matrizITM!F4:AJ81", "ITM");
        this.getHours("matrizISA!F4:AJ81", "ISA");
        this.getHours("matrizLAyGE!F4:AJ81", "LAyGE");
        this.getHours("matrizComercio!F4:AJ81", "Comercio");

        int totalHours = 0;
        for (Professor professor : this.professors) {
            int tmpTotalHours = (professor.getITIHours() + professor.getIMHours() + professor.getITMHours() + professor.getISAHours() + professor.getLAyGEHours() + professor.getComercioHours());
            System.out.printf("Name: %20s, ITIHours: %3d, IMHours: %3d, ITMHours: %3d, ISAHours: %3d, LAyGEHours: %3d, comercioHours: %3d, Total hours: %d\n", professor.getName(), professor.getITIHours(), professor.getIMHours(), professor.getITMHours(), professor.getISAHours(), professor.getLAyGEHours(), professor.getComercioHours(), tmpTotalHours);

            totalHours += tmpTotalHours;
        }

        System.out.println("Total of hours: " + totalHours);
    }

    public void getNames() throws GeneralSecurityException, IOException {
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
                this.professors[i].setName(name);
            }
        }
    }

    public void getHours(String range, String career) throws IOException, GeneralSecurityException {
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
                if (career.equals("ITI")) this.professors[j].setITIHours(this.professors[j].getITIHours() + itemInteger);
                if (career.equals("IM")) this.professors[j].setIMHours(this.professors[j].getIMHours() + itemInteger);
                if (career.equals("ITM")) this.professors[j].setITMHours(this.professors[j].getITMHours() + itemInteger);
                if (career.equals("ISA")) this.professors[j].setISAHours(this.professors[j].getISAHours() + itemInteger);
                if (career.equals("LAyGE")) this.professors[j].setLAyGEHours(this.professors[j].getLAyGEHours() + itemInteger);
                if (career.equals("Comercio")) this.professors[j].setComercioHours(this.professors[j].getComercioHours() + itemInteger);
            }
        }
    }
}
