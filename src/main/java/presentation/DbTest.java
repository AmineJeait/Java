package presentation;

import dao.*;
import metier.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DbTest {

    public static void main(String[] args) {

        /* ======================
           SERVICES
           ====================== */
        GestionUser gu = new GestionUser();
        GestionClient gc = new GestionClient();
        GestionReparation gr = new GestionReparation();
        GestionAppareil ga = new GestionAppareil();
        GestionCaisse gca = new GestionCaisse();

        /* ======================
           ADMIN
           ====================== */
        Admin admin = new Admin();
        admin.setNom("Doe");
        admin.setPrenom("John");
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setMagasin(null);

        gu.ajouter(admin);

        /* ======================
           REPARATEUR
           ====================== */
        User reparateur = new User();
        reparateur.setNom("Smith");
        reparateur.setPrenom("Adam");
        reparateur.setUsername("repair1");
        reparateur.setPassword("1234");
        reparateur.setMagasin(null);

        gu.ajouter(reparateur);

        /* ======================
           CAISSE
           ====================== */
        Caisse caisse = new Caisse();
        caisse.setUser(reparateur);
        caisse.setMontantReel(3000);
        caisse.setMontantReparation(0);

        gca.ajouter(caisse);

        /* ======================
           CLIENT
           ====================== */
        Client client = new Client();
        client.setNomComplet("Mike Brown");
        client.setTelephone("0612345678");

        gc.ajouter(client);

        /* ======================
           REPARATION
           ====================== */
        Reparation reparation = new Reparation();
        reparation.setState("EN_COURS");
        reparation.setDescription("Remplacement écran");
        reparation.setPrix(900);
        reparation.setDateDepot(LocalDate.now());
        reparation.setUser(reparateur);
        reparation.setClient(client);

        gr.ajouter(reparation);

        /* ======================
           APPAREILS
           ====================== */
        Appareil a1 = new Appareil();
        a1.setMarque("Samsung");
        a1.setModele("Galaxy S21");
        a1.setDescription("Écran fissuré");
        a1.setIemi("111222333444555");
        a1.setReparation(reparation);

        Appareil a2 = new Appareil();
        a2.setMarque("Apple");
        a2.setModele("iPhone 12");
        a2.setDescription("Batterie faible");
        a2.setIemi("999888777666555");
        a2.setReparation(reparation);

        ga.ajouter(a1);
        ga.ajouter(a2);

        /* ======================
           LINK APPAREILS -> REPARATION
           ====================== */
        List<Appareil> appareils = new ArrayList<>();
        appareils.add(a1);
        appareils.add(a2);
        reparation.setAppareils(appareils);

        gr.modifier(reparation);

        /* ======================
           DISPLAY
           ====================== */
        System.out.println("==== USERS ====");
        gu.lister().forEach(System.out::println);

        System.out.println("==== CLIENTS ====");
        gc.lister().forEach(System.out::println);

        System.out.println("==== REPARATIONS ====");
        gr.lister().forEach(System.out::println);

        System.out.println("==== APPAREILS ====");
        ga.lister().forEach(System.out::println);

        System.out.println("✅ Database seeded successfully");
    }
}
