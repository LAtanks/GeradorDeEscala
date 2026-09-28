package app;

import app.services.CollaboratorService;
import app.services.StoreService;
import app.util.Config;
import app.util.Itinerary;

import java.util.Scanner;

public class Start {
    public Config config = new Config(this);

    public StoreService storeService = new StoreService(this);
    public CollaboratorService collaboratorService = new CollaboratorService(this);

    public Itinerary itinerary = new Itinerary(this);
    private Scanner scanner = new Scanner(System.in);
    private boolean hasInformationFromStore = false;

    public Start(){
    }

    public void initilize() {
        System.out.println("Abrindo app. . .");
        System.out.println("versão 1.0.0v");
        System.out.println("Identificando a loja . . .");
        System.out.println("==========================");

        loadInformations();

        if(hasInformationFromStore){
            home();
        }else {
            System.out.println("Loja não cadastrada");
            System.out.println("==========================");
            storeService.commands();
        }
    }

    public void home(){
        while (true){
            System.out.println("Aba home. . .");
            System.out.println("1 - Gerar escala");
            System.out.println("2 - Colaboradores");
            System.out.println("3 - Loja");

            int option = scanner.nextInt();

            switch (option){
                case 1:
                    generateItinerary();
                    break;
                case 2:
                    collaboratorService.commands();
                    break;
                case 3:
                    storeService.commands();
                    break;
                default:
                    System.out.println("Opção invalida");
            }

            System.out.println("==========================");
        }
    }

    public void generateItinerary(){
        if(this.collaboratorService.collaborators.isEmpty()){
            System.out.println("Nenhum colaborador foi encontrado");
            System.out.println("==========================");
            home();
        }

        itinerary.commands();
    }

    private void loadInformations(){
        this.storeService.registeredStore = config.loadStore();
        this.collaboratorService.collaborators = config.loadCollaborators();

        if(this.storeService.registeredStore != null) hasInformationFromStore = true;
    }
}
