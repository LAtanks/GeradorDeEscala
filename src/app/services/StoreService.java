package app.services;

import app.Start;
import app.models.Store;

import java.lang.reflect.Field;
import java.util.Scanner;

public class StoreService {
    private Scanner scanner = new Scanner(System.in);
    public Store registeredStore;
    private Start start;

    public StoreService(Start start) {
        this.start = start;
        registeredStore = start.config.loadStore();
    }

    public void commands(){
        System.out.println("Escolha algumas das opções: ");
        System.out.println("==========================");
        System.out.println("1 - Registrar nova loja");
        System.out.println("2 - Atualizar alguma informação");
        System.out.println("3 - Rever informações");
        System.out.println("4 - Voltar para tela inicial");
        System.out.println("==========================");

        int option = scanner.nextInt();

        switch (option){
            case 1:
                register();
                break;
            case 2:
                updateInformations();
                break;
            case 3:
                showActualStore();
                break;
            case 4:
                start.home();
        }
    }

    private void register(){
        System.out.println("Iniciando serviço de loja. . .");
        System.out.println("==========================");

        System.out.println("Coloque o numero da loja: (Ex: 125, 91, 265, 20)");
        int storeNumber = scanner.nextInt();

        System.out.println("Endereço da loja: ");
        Scanner sc = new Scanner(System.in).useDelimiter("(\\b|\\B)");
        String address = sc.next();

        System.out.println("Quantidades de checkouts(caixas) operados na loja: ");
        int checkouts = scanner.nextInt();

        System.out.println("Tem auto atendimentos(selfs)? (S ou N)");
        String hasSelfCheckout = scanner.next();

        int selfCheckouts = 0;
        if(hasSelfCheckout.contains("S")){
            System.out.println("Quantidade de selfs: ");
            selfCheckouts = scanner.nextInt();
        }

        System.out.println("Horas de trabalho na loja");
        float workingHours = scanner.nextFloat();

        this.registeredStore = new Store(
                storeNumber,
                address,
                (byte) checkouts,
                hasSelfCheckout.contains("S") ? true : false,
                (byte) selfCheckouts,
                workingHours
        );

        start.config.saveStoreToFile(this.registeredStore);

        System.out.println("Deseja rever as informações? (S ou N)");
        String reverAsInfors = scanner.next();

        if(reverAsInfors.equalsIgnoreCase("s")){
            System.out.println(this.registeredStore.toString());
            System.out.println("==========================");
            System.out.println("Deseja corrigi-las? (S ou N)");
            String corrigirLoja = scanner.next();



            if(corrigirLoja.contains("S")){
                commands();
            }else{

                start.home();
            }
        }else{
            commands();
        }
    }

    public void updateInformations(){
        if(this.registeredStore != null){
            System.out.println("Qual informação deseja atualizar? ");
            String f = scanner.next();

            for(Field field : this.registeredStore.getClass().getFields()){
                if(field.getName().contains(f)){
                    System.out.println(field.getName());
                }
            }
        }else{
            System.out.println("Nenhum loja registrada");
        }
    }

    public void showActualStore(){
        System.out.println(this.registeredStore.toString());
        System.out.println("==========================");
        commands();
    }
}
