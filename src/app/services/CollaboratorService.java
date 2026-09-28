package app.services;

import app.Start;
import app.models.Collaborator;
import app.models.enums.Horary;
import app.models.enums.Type;
import csv.CollaboratorCsvReader;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;

public class CollaboratorService {
    private Scanner scanner = new Scanner(System.in);
    public Set<Collaborator> collaborators;

    private Start start;

    public CollaboratorService(Start start) {
        this.start = start;
        collaborators = start.config.loadCollaborators();
    }

    public void commands(){
        System.out.println("Escolha algumas das opções: ");
        System.out.println("==========================");
        System.out.println("1 - Registrar um novo colaborador");
        System.out.println("2 - Mostrar a lista de colaboradores");
        System.out.println("3 - Remover alguma colaborador");
        System.out.println("4 - Procurar colaborador pelo o nome");
        System.out.println("5 - Carregar Tabela de colaboradores");
        System.out.println("6 - Voltar para tela inicial");
        System.out.println("==========================");

        int option = new Scanner(System.in).nextInt();

        switch (option){
            case 1:
                register();
                break;
            case 2:
                showAllCollaborators();
                break;
            case 3:
                System.out.println("==========================");
                System.out.println("Qual seu nome?");
                String toDeleteUser = scanner.next();
                removeCollaboratorByName(toDeleteUser);
                break;
            case 4:
                System.out.println("==========================");
                System.out.println("Qual seu nome?");
                String toFindUser = scanner.next();
                Collaborator collaborator = findByName(toFindUser);
                try {
                    System.out.println(collaborator.toString());
                } catch (Exception e) {
                    System.err.println("Colaborador não encontrado");
                }
                break;
            case 5:
                loadingCollaboratorsTable();
                break;
            case 6:
                start.home();
                break;
            default:
                System.out.println("Opção invalida");
                commands();
        }
    }

    private void loadingCollaboratorsTable() {
        List<Collaborator> collaborators = null;
        try {
            collaborators = CollaboratorCsvReader.getInstance().reader();
            collaborators.addAll(collaborators);
            for (Collaborator collaborator : collaborators) {
                System.out.println(collaborator.toString());
            }

            this.collaborators.clear();
            this.collaborators.addAll(collaborators);
            start.config.save();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            commands();
        }
    }

    private void showAllCollaborators() {
        if(!this.collaborators.isEmpty()){
            System.out.println("Todos colaboradores registrados na loja:");
            for(Collaborator collaborator : this.collaborators){
                if(collaborator.getDaysOffInMonth() == null) continue;
                System.out.println(collaborator.toString());
            }
            System.out.println("==========================");
            commands();
        }else{
            System.out.println("Não existe nenhum colaborador cadastrado");
            System.out.println("==========================");
            commands();
        }
    }

    public Collaborator findByName(String name) {
        for(Collaborator collaborator : collaborators){
            String collaboratorName = collaborator.getName().toLowerCase();

            if(collaboratorName.equalsIgnoreCase(name.toLowerCase()) || collaboratorName.contains(name.toLowerCase()) || collaboratorName.startsWith(name.toLowerCase())){
                return collaborator;
            }
        }

        return null;
    }

    public void removeCollaboratorByName(String name){
        Collaborator collaborator = findByName(name);

        if(collaborator != null){
            this.collaborators.remove(collaborator);
            System.out.println("Colaborador removido com sucessor");
            System.out.println("==========================");
            commands();
        }else{
            System.out.println("Colaborador não encontrado ou não registrado");
            System.out.println("==========================");
            commands();
        }
    }

    public void register(){
        scanner = new Scanner(System.in);

        System.out.println("==========================");

        System.out.printf("Nome: \n");
        String fullName = scanner.next();

        System.out.printf("Horario: (Manha ou Tarde) \n");
        String horary = scanner.next();


        System.out.printf("Folgas no mês: %d (Ex: 31,02,05,2) \n", LocalDate.now().getYear());
        String daysOffInMonth = scanner.next();
        String[] daysOffSplited = daysOffInMonth.split(",");
        Set<LocalDate> daysOffSet = new HashSet<>();

        for(int i = 0; i < daysOffSplited.length; i++){
            LocalDate localDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), Integer.parseInt(daysOffSplited[i]));
            daysOffSet.add(localDate);
        }


        String month = Year.now().toString();

        System.out.printf("Tipo de colaborador: \n");
        System.out.printf("1 - Linha de Frente \n");
        System.out.printf("2 - Operador \n");

        int collaboratorType = scanner.nextInt();

        Collaborator collaborator = new Collaborator(
                fullName,
                Horary.getByName(horary),
                Map.of(month, daysOffSet),
                collaboratorType == 1 ? Type.FRONT_LINE : Type.OPERATOR
        );

        if(findByName(collaborator.getName()) != null){
            System.out.println("Colaborador já registrado");
            register();
        }

        if(collaborators==null){
            collaborators = new HashSet<>();
        }


        System.out.println("Deseja rever as informações? (S ou N)");
        String reverAsInfors = scanner.next();

        if(reverAsInfors.equalsIgnoreCase("s")){
            System.out.println(collaborator.toString());
            System.out.println("==========================");
            System.out.println("Deseja corrigi-las? (S ou N)");
            String corrigirLoja = scanner.next();

            if(corrigirLoja.contains("S")){
                register();
            }else{
                try {
                    collaborators.add(collaborator);
                    start.config.save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                commands();
            }
        }else{
            if(collaborators==null){
                collaborators = new HashSet<Collaborator>();
            }

            try {
                collaborators.add(collaborator);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            start.config.save();

            commands();
        }
    }



    private LocalDate handleDateFromString(String date){
        String[] splits = date.split("/");

        int day = Integer.parseInt(splits[0]);
        int mounth = Integer.parseInt(splits[1]);
        int year = Integer.parseInt(splits[2]);

        LocalDate dateFormated = LocalDate.of(year, Month.of(mounth), day);

        return dateFormated;
    }
}
