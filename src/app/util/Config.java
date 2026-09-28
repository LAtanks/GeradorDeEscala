package app.util;

import app.Start;
import app.models.Collaborator;
import app.models.Store;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;

public class Config {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

    private final File DIR = new File("bh/configs");
    private final File STORE = new File(DIR, "store.json");
    private final File COLLABORATORS = new File(DIR, "collaborators.json");

    private final Start start;

    public Config(Start start) {
        this.start = start;

        if(!DIR.exists()) DIR.mkdirs();
    }

    public Store loadStore(){
        if(!STORE.exists()){
            System.err.println("Dados não encontrado");
            return null;
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(STORE);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder builder = new StringBuilder();
            String line;

            while((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            return gson.fromJson(builder.toString(), Store.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean saveStoreToFile(Object obj) {
        try {
            if(!STORE.exists()) {
                STORE.createNewFile();
            }

            FileOutputStream outputStream = new FileOutputStream(STORE);
            outputStream.write(gson.toJson(obj).getBytes());
            outputStream.flush();
            outputStream.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public Set<Collaborator> loadCollaborators(){
        if(!COLLABORATORS.exists()){
            System.err.println("Dados não encontrado");
            return new HashSet<>();
        }

        try (FileReader reader = new FileReader(COLLABORATORS)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            JsonArray array = root.getAsJsonArray("collaborators");

            Type setType = new TypeToken<Set<Collaborator>>(){}.getType();

            return gson.fromJson(array, setType);


        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }
    public void save(){
        JsonObject root = new JsonObject();

        root.add("collaborators", gson.toJsonTree(this.start.collaboratorService.collaborators));

        try {
            DIR.mkdirs();
            try(FileWriter fw = new FileWriter(COLLABORATORS)){
                gson.toJson(root, fw);
                System.out.println("Colaboradores salvo com sucesso!");
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
