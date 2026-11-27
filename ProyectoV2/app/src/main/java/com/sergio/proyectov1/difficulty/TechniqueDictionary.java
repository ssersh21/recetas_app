package com.sergio.proyectov1.difficulty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class TechniqueDictionary {

    public static final int BASICA = 1;
    public static final int INTERMEDIA = 2;
    public static final int AVANZADA = 3;

    public static final class Technique {
        public final String nombre;
        public final int nivel;
        public final List<String> palabrasClave;

        Technique(String nombre, int nivel, String... palabrasClave) {
            this.nombre = nombre;
            this.nivel = nivel;
            this.palabrasClave =
                    Collections.unmodifiableList(Arrays.asList(palabrasClave));
        }
    }

    private static final List<Technique> TECHNIQUES = build();

    private TechniqueDictionary() {
    }

    public static List<Technique> getAll() {
        return TECHNIQUES;
    }

    private static List<Technique> build() {
        List<Technique> t = new ArrayList<>();

        // ---------------- BÁSICAS (nivel 1) ----------------
        t.add(new Technique("Cortar y picar", BASICA,
                "cort", "pica!", "picar", "picad", "pique", "piquen", "trocea", "troce",
                "rebana", "rebanad", "cubit", "rodaja"));
        t.add(new Technique("Mezclar", BASICA,
                "mezcl", "revuel", "revolv", "integr", "incorpor"));
        t.add(new Technique("Hervir", BASICA,
                "herv", "hierv", "hirv"));
        t.add(new Technique("Cocer", BASICA,
                "cocer", "cuece", "cuezan", "cocin", "coccion"));
        t.add(new Technique("Freír", BASICA,
                "freir", "freid", "frita", "frito", "fritur", "frie"));
        t.add(new Technique("Saltear", BASICA,
                "saltea", "saltee", "salteado"));
        t.add(new Technique("Hornear", BASICA,
                "horne", "horno", "precalient"));
        t.add(new Technique("Asar a la plancha o parrilla", BASICA,
                "asar", "asad", "parrilla", "plancha"));
        t.add(new Technique("Batir", BASICA,
                "bati", "bate"));
        t.add(new Technique("Licuar o triturar", BASICA,
                "licu", "tritur", "proces", "moler", "muela"));
        t.add(new Technique("Marinar o adobar", BASICA,
                "marin", "adob"));
        t.add(new Technique("Sazonar", BASICA,
                "sazon", "condimenta", "salpiment"));
        t.add(new Technique("Rallar", BASICA,
                "ralla", "rallad"));
        t.add(new Technique("Dorar", BASICA,
                "dorar", "dorad", "doren", "dora!"));
        t.add(new Technique("Escurrir o colar", BASICA,
                "escurr", "colar", "colad", "cuele", "cuela"));
        t.add(new Technique("Gratinar", BASICA,
                "gratin"));

        // ---------------- INTERMEDIAS (nivel 2) ----------------
        t.add(new Technique("Sofreír", INTERMEDIA,
                "sofr", "sofrit"));
        t.add(new Technique("Guisar o estofar", INTERMEDIA,
                "guis", "estof"));
        t.add(new Technique("Brasear", INTERMEDIA,
                "brase", "braseado"));
        t.add(new Technique("Reducir una salsa", INTERMEDIA,
                "reducir", "reduzc", "reduce", "reducc"));
        t.add(new Technique("Blanquear o escaldar", INTERMEDIA,
                "blanquea", "blanquee", "escald"));
        t.add(new Technique("Escalfar o pochar", INTERMEDIA,
                "escalf", "pochar", "pochad", "poche"));
        t.add(new Technique("Amasar", INTERMEDIA,
                "amas", "amasado"));
        t.add(new Technique("Leudar o fermentar", INTERMEDIA,
                "leud", "fermen", "levad"));
        t.add(new Technique("Montar claras o crema", INTERMEDIA,
                "montar", "montad", "punto de nieve", "chantilly", "claras a punto"));
        t.add(new Technique("Espesar o ligar", INTERMEDIA,
                "espes", "ligar", "ligue", "roux"));
        t.add(new Technique("Desglasar", INTERMEDIA,
                "desglas"));
        t.add(new Technique("Empanizar o rebozar", INTERMEDIA,
                "empaniz", "rebozar", "rebozad", "capea", "empanar", "empanad"));
        t.add(new Technique("Rellenar y enrollar", INTERMEDIA,
                "rellen", "enroll", "enrolla"));
        t.add(new Technique("Caramelizar", INTERMEDIA,
                "carameli"));
        t.add(new Technique("Cocer al vapor", INTERMEDIA,
                "vapor", "vaporera"));
        t.add(new Technique("Baño María", INTERMEDIA,
                "bano maria", "bano de maria"));
        t.add(new Technique("Filetear o deshuesar", INTERMEDIA,
                "fileteo", "filetea", "filetee", "deshues"));
        t.add(new Technique("Sellar", INTERMEDIA,
                "sellar", "selle", "sellado"));

        // ---------------- AVANZADAS (nivel 3) ----------------
        t.add(new Technique("Emulsionar", AVANZADA,
                "emulsi", "mayonesa", "holandesa", "bearnesa"));
        t.add(new Technique("Flamear", AVANZADA,
                "flambe", "flame", "prender fuego"));
        t.add(new Technique("Templar chocolate", AVANZADA,
                "atemper", "templar el chocolate", "templado de chocolate"));
        t.add(new Technique("Laminar masa (hojaldre)", AVANZADA,
                "hojaldr", "laminad", "laminar", "dobleces"));
        t.add(new Technique("Cocción al vacío (sous vide)", AVANZADA,
                "sous vide", "sous-vide", "al vacio", "termocirculador"));
        t.add(new Technique("Confitar", AVANZADA,
                "confit"));
        t.add(new Technique("Punto de azúcar", AVANZADA,
                "punto de caramelo", "punto de hebra", "punto de bola",
                "termometro de azucar", "azucar al punto"));
        t.add(new Technique("Suflé o merengue italiano", AVANZADA,
                "sufle", "merengue italiano"));
        t.add(new Technique("Clarificar", AVANZADA,
                "clarific", "consome"));
        t.add(new Technique("Desosar o trinchar ave entera", AVANZADA,
                "desos", "trinch", "abrir en mariposa"));
        t.add(new Technique("Esferificar o gelificar", AVANZADA,
                "esferific", "gelific", "agar!", "alginato"));
        t.add(new Technique("Ahumar o curar", AVANZADA,
                "ahum", "curar", "curado", "embutir"));

        return Collections.unmodifiableList(t);
    }
}