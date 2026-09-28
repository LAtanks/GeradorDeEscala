package app.util;

import app.Start;
import app.models.Collaborator;
import app.models.enums.Horary;
import app.models.enums.Type;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class Itinerary {

    private Scanner scanner = new Scanner(System.in);
    private Start start;

    List<Collaborator> colaboradoresNoSelf = new ArrayList<>();
    List<Collaborator> colaboradoresDeAtestado = new ArrayList<>();
    List<Collaborator> colaboradoresParaFicaremEmPe = new ArrayList<>();
    List<Collaborator> colaboradoresAtrasado = new ArrayList<>();

    List<Collaborator> colaboradoresSortidos = new ArrayList<>();
    List<Collaborator> colaboradoresEmPe = new ArrayList<>();
    List<Collaborator> colaboradoresDeFolga = new ArrayList<>();

    Map<Byte, Collaborator> checkoutsMorning = new HashMap<>();
    Map<Byte, Collaborator> checkoutsAfternoon = new HashMap<>();

    LocalDate nextDay = LocalDate.now().plusDays(1);
    String nextDayTratado = nextDay.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("pt", "BR")).split("-")[0];


    public Itinerary(Start start) {
        this.start = start;
    }

    public void generate(List<Collaborator> collaborators){
        Collections.shuffle(colaboradoresSortidos);

        colaboradoresSortidos = start.collaboratorService.collaborators.stream().toList();

        //start.collaboratorService.collaborators.stream().filter(e -> e.getDaysOffInMonth().values().contains(LocalDate.now().getDayOfMonth())).collect(Collectors.toList())

        colaboradoresSortidos.forEach(colaborador -> {
            for (Set<LocalDate> localDates : colaborador.getDaysOffInMonth().values()) {
                if(!localDates.contains(nextDay)){
                    colaboradoresDeFolga.add(colaborador);
                }
            }
        });


        byte checkoutMorning = 0;
        byte checkoutAfternoon = 0;
        for(int i = 0; i < colaboradoresSortidos.size(); i++){
            Collaborator collaborator = colaboradoresSortidos.get(i);

            if(colaboradoresParaFicaremEmPe.contains(collaborator) || colaboradoresNoSelf.contains(collaborator) ||colaboradoresDeFolga.contains(collaborator) && !collaborator.getCollaboratorType().equals(Type.OPERATOR)){
                continue;
            }

            if(collaborator.horary.equals(Horary.MORNING) && checkoutsMorning.size() < start.storeService.registeredStore.getCheckouts()){
                checkoutsMorning.put(checkoutMorning++, collaborator);
            }
            else if(collaborator.horary.equals(Horary.AFTERNOON) && checkoutsAfternoon.size() < start.storeService.registeredStore.getCheckouts()){
                checkoutsAfternoon.put(checkoutAfternoon++, collaborator);
            }
            else{
                colaboradoresEmPe.add(collaborator);
            }
        }
        colaboradoresEmPe.addAll(colaboradoresParaFicaremEmPe);

        printItinerary();
    }

    public void commands(){
        while(true){
            System.out.println("========================");
            System.out.println("0 - Quem vai ficar no self?");
            System.out.println("1 - Algum Colaborador de atestado?");
            System.out.println("2 - colocar alguem em pé?");
            System.out.println("3 - Vai chegar atrasado?");
            System.out.println("4 - Gerar Escala");
            System.out.println("5 - Cancelar");

            int option = scanner.nextInt();

            switch (option){
                case 0 -> {
                    System.out.println("Qual nome do colaborador?");
                    String nomeColaborador = scanner.next();
                    Collaborator colaborador = this.start.collaboratorService.findByName(nomeColaborador);
                    colaboradoresNoSelf.add(colaborador);
                }
                case 1 ->{
                    System.out.println("Qual nome do colaborador?");
                    String nomeColaborador = scanner.next();
                    Collaborator colaborador = this.start.collaboratorService.findByName(nomeColaborador);
                    colaboradoresDeAtestado.add(colaborador);
                }case 2 -> {
                    System.out.println("Qual nome do colaborador?");
                    String nomeColaborador = scanner.next();
                    Collaborator colaborador = this.start.collaboratorService.findByName(nomeColaborador);
                    colaboradoresParaFicaremEmPe.add(colaborador);
                }
                case 3 -> {
                    System.out.println("Qual nome do colaborador?");
                    String nomeColaborador = scanner.next();
                    Collaborator colaborador = this.start.collaboratorService.findByName(nomeColaborador);
                    colaboradoresAtrasado.add(colaborador);
                }
                case 4 -> {
                    generate(start.collaboratorService.collaborators.stream().toList());
                }
                case 5 -> {
                    start.home();
                }
                default -> System.out.println("Opção invalida");
            }
        }
    }

    private void printItinerary(){
        System.out.println("========================");
        System.out.printf("Dia: %s  Data: %s\n", nextDayTratado.toUpperCase(),  nextDay);

        if(!nextDayTratado.equalsIgnoreCase("domingo")){
            System.out.println("7:20");
            for(int i = 0; i< start.storeService.registeredStore.getCheckouts(); i++){
                System.out.printf("Checkout %d: %s\n", i+1, checkoutsMorning.get((byte) i) != null ? checkoutsMorning.get((byte) i).getName() : "");
            }
            System.out.println("========================");
            System.out.println("12:00");
            for(int i = 0; i< start.storeService.registeredStore.getCheckouts(); i++){
                System.out.printf("Checkout %d: %s\n", i+1, checkoutsAfternoon.get((byte) i) != null ? checkoutsAfternoon.get((byte) i).getName() : "");
            }
            System.out.println("========================");
        }else{
            Map<Byte, Collaborator> checkoutsOfSunday = new HashMap<>();
            checkoutsOfSunday.putAll(checkoutsMorning);
            checkoutsOfSunday.putAll(checkoutsAfternoon);
            System.out.println("========================");
            for(int i = 0; i< start.storeService.registeredStore.getCheckouts(); i++){
                System.out.printf("Checkout %d: %s\n", i+1, checkoutsOfSunday.get((byte) i) != null ? checkoutsOfSunday.get((byte) i).getName() : "");
            }
            System.out.println("========================");
        }


        System.out.println("Self: \n" + colaboradoresNoSelf.stream().filter(e -> !e.getCollaboratorType().equals(Type.FRONT_LINE)).map(Collaborator::getName).collect(Collectors.joining(", \n")));
        System.out.println("========================");
        System.out.println("Em pé: \n" + colaboradoresEmPe.stream().filter(e -> !e.getCollaboratorType().equals(Type.FRONT_LINE)).map(Collaborator::getName).collect(Collectors.joining(", \n")));
        System.out.println("========================");
        System.out.println("Folga: \n" + colaboradoresDeFolga.stream().filter(e -> !e.getCollaboratorType().equals(Type.FRONT_LINE)).map(Collaborator::getName).collect(Collectors.joining(", \n")));
        System.out.println("========================");
        System.out.println("Linha de frente: \n" + colaboradoresSortidos.stream().filter(e -> e.getCollaboratorType().equals(Type.FRONT_LINE)).map(Collaborator::getName).collect(Collectors.joining(", \n")));
        System.out.println("========================");
        System.out.println("Atestado: \n" + colaboradoresDeAtestado.stream().filter(e -> !e.getCollaboratorType().equals(Type.FRONT_LINE)).map(Collaborator::getName).collect(Collectors.joining(", \n")));

    }
}
