package csv;

import app.models.Collaborator;
import app.models.enums.Horary;
import app.models.enums.Type;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class CollaboratorCsvReader {

    private static final CollaboratorCsvReader INSTANCE = new CollaboratorCsvReader();
    public static CollaboratorCsvReader getInstance() {
        return INSTANCE;
    }

    private FileReader  fileReader;

    public CollaboratorCsvReader() {

    }

    public List<Collaborator> reader() throws Exception {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Apenas CSV", "csv"
        );

        chooser.addChoosableFileFilter(filter);
        
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Carregar tabela de colaboradores");
        chooser.setFileSystemView(FileSystemView.getFileSystemView());

        int returnVal = chooser.showOpenDialog(null);

        if(chooser.getSelectedFile().getName().contains(".csv")) {
            System.out.println("Arquivo selecionado: " + chooser.getSelectedFile().getAbsolutePath());
        }else{
            System.err.println("Error!!");
            throw new Exception("Arquivo não aceito");
        }

        this.fileReader = new FileReader(chooser.getSelectedFile());

        List<Collaborator> collaborators = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(fileReader)) {
            csvReader.readNext();

            String[] header =  csvReader.readNext();

            String[] line;

            while ((line = csvReader.readNext()) != null) {

                if(line.length < 3){
                    continue;
                }

                int number = Integer.parseInt(line[0]);
                String name = line[1];

                boolean frontLine = "X".equals(line[2]);
                Horary horary = line[3].contains("Manha") ? Horary.MORNING : Horary.AFTERNOON;

                Set<LocalDate> daysOff = new HashSet<>();

                for(int i = 4; i < line.length; i++){

                    if("X".equalsIgnoreCase(line[i])){
                        int day = Integer.parseInt(header[i]);

                        LocalDate date = LocalDate.of(2026, 9, day);

                        daysOff.add(date);
                    }
                }

                Collaborator pessoa = new Collaborator(
                        name,
                        horary,
                        Map.of("Setembro", daysOff),
                        frontLine ? Type.FRONT_LINE : Type.OPERATOR
                );

                collaborators.add(pessoa);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return collaborators;
    }
}
