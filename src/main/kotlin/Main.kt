import dresseur.Entraineur
import item.Badge
import item.MonsterKube
import monde.Zone
import monstre.EspeceMonstre
import monstre.IndividuMonstre

/**
 * Change la couleur du message donné selon le nom de la couleur spécifié.
 * Cette fonction utilise les codes d'échappement ANSI pour appliquer une couleur à la sortie console. Si un nom de couleur
 * non reconnu ou une chaîne vide est fourni, aucune couleur n'est appliquée.
 *
 * @param message Le message auquel la couleur sera appliquée.
 * @param couleur Le nom de la couleur à appliquer (ex: "rouge", "vert", "bleu"). Par défaut c'est une chaîne vide, ce qui n'applique aucune couleur.
 * @return Le message coloré sous forme de chaîne, ou le même message si aucune couleur n'est appliquée.
 */

fun changeCouleur(message: String, couleur:String=""): String {
    val reset = "\u001B[0m"
    val codeCouleur = when (couleur.lowercase()) {
        "rouge" -> "\u001B[31m"
        "vert" -> "\u001B[32m"
        "jaune" -> "\u001B[33m"
        "bleu" -> "\u001B[34m"
        "magenta" -> "\u001B[35m"
        "cyan" -> "\u001B[36m"
        "blanc" -> "\u001B[37m"
        "marron" -> "\u001B[38;5;94m"
        else -> "" // pas de couleur si non reconnu
    }
    return "$codeCouleur$message$reset"
}

/** variables des dresseurs */

var joueur = Entraineur(1, "Sacha", 100)
var rival = Entraineur(2, "Regis", 100)



/** variables des monstres */

var Springleaf = EspeceMonstre(
    1,
    "Springleaf",
    "Graine",
    9,
    11,
    10,
    12,
    14,
    60,
    6.5,
    9.0,
    8.0,
    7.0,
    10.0,
    34.0,
    "Petit monstre espiègle rond comme une graine, adore le soleil.\n",
    "Sa feuille sur la tête indique son humeur.\n",
    "Curieux, amical, timide.\n"
)
var Flamkip = EspeceMonstre(
    4,
    "Flamkip",
    "Animal",
    12,
    8,
    13,
    16,
    7,
    50,
    10.0,
    5.5,
    9.5,
    9.5,
    6.5,
    22.0,
    "Petit animal entouré de flammes, déteste le froid.\n",
    "Sa flamme change d’intensité selon son énergie.\n",
    "Impulsif, joueur, loyal.\n"
)
var Aquamy= EspeceMonstre(
    7,
    "Aquamy",
    "Meteo",
    10,
    11,
    9,
    14,
    14,
    55,
    9.0,
    10.0,
    7.5,
    12.0,
    12.0,
    27.0,
    "Créature vaporeuse semblable à un nuage, produit des gouttes pures.\n",
    "Fait baisser la température en s’endormant.\n",
    "Calme, rêveur, mystérieux\n"
)



/** variables des zones */

var zone1 = Zone(
    1,
    "route 1",
    800,
//    mutableListOf(Springleaf),
//    zone2,
//    null
    )

var zone2 = Zone (
    2,
    "route 2",
    1500,
//    mutableListOf(Flamkip),
//    null,
//    zone1
)

// Objet MonsterKube disponible pour les tests de capture
val kubeBasique = MonsterKube(
    id = 100,
    nom = "Monster Kube",
    description = "Une sphère permettant de tenter la capture d'un monstre.",
    chanceCapture = 35.0 // en pourcentage
)


fun main() {



    zone1.zoneSuivante = zone2
    zone2.zonePrecedente = zone1

    // Test temporaire: création d'un Badge
    val badgePierre = Badge(1, "Badge Roche", "Badge gagné lorsque le joueur atteint la arène de pierre.", rival)
    println("Badge créé: id=${badgePierre.id}, nom=${badgePierre.nom}, description=${badgePierre.description}, champion=${badgePierre.champion.nom}")

//    // Création de trois monstres pour aperçu de l'affichage détaillé
//    val monstre1 = IndividuMonstre(1, "Springleaf", Springleaf, joueur, 1500.0)
//    val monstre2 = IndividuMonstre(2, "Flamkip", Flamkip, joueur, 1500.0)
//     val monstre3 = IndividuMonstre(3, "Aquamy", Aquamy, joueur, 1500.0)
//
//    // Affichage du rendu de la méthode afficheDetail() pour chaque monstre
//    monstre1.afficheDetail()
//    println()
//    monstre2.afficheDetail()
//    println()
//    monstre3.afficheDetail()
//    println()

//    joueur.afficheDetail()
//    rival.afficheDetail()
//    joueur.argents+=50
//    joueur.afficheDetail()


    /** permet d'afficher les monstres de face */
//    println(Springleaf.afficheArt())
//    println(Flamkip.afficheArt())
//    println(Aquamy.afficheArt())


    /** permet d'afficher les monstres de dos grace a false*/
//    println(Springleaf.afficheArt(false))
//    println(Flamkip.afficheArt(false))
//    println(Aquamy.afficheArt(false))

//    val monstre1 = IndividuMonstre(1, "Springleaf", Springleaf, joueur, 1500.0)
//    val monstre2 = IndividuMonstre(2, "Flamkip", Flamkip, joueur, 1500.0)
//    val monstre3 = IndividuMonstre(3, "Aquamy", Aquamy, joueur, 1500.0)


    /** Vérifications niveau et statistiques > bases */
//    fun printStats(m: IndividuMonstre) {
//        println("--- ${m.nom} (niveau ${m.niveau}) ---")
//        println("Attaque: ${m.attaque} (base ${m.espece.baseAttaque})")
//        println("Défense: ${m.defense} (base ${m.espece.baseDefense})")
//        println("Vitesse: ${m.vitesse} (base ${m.espece.baseVitesse})")
//        println("AttaqueSpe: ${m.attaqueSpe} (base ${m.espece.baseAttaqueSpe})")
//        println("DéfenseSpe: ${m.defenseSpe} (base ${m.espece.baseDefenseSpe})")
//        println("pv: ${m.pv}/${m.pvMax} (base ${m.espece.basePv})")
//        println()
//    }
//
//    listOf(monstre1, monstre2, monstre3).forEach { m ->
//        printStats(m)
//        val lvlOk = m.niveau >= 5
//        val statsOk = m.attaque > m.espece.baseAttaque && m.defense > m.espece.baseDefense &&
//                m.vitesse > m.espece.baseVitesse && m.attaqueSpe > m.espece.baseAttaqueSpe &&
//                m.defenseSpe > m.espece.baseDefenseSpe && m.pvMax > m.espece.basePv
//        println("Niveau >= 5: $lvlOk, Stats > base: $statsOk")
//        println()
//    }
//
//    // Tester le setter d'expérience: on augmente fortement l'XP pour forcer des level-ups
//
//    println("== Test setter exp ==")
//    monstre1.exp += 1000.0 // devrait déclencher des level-up si palier atteint
//    monstre2.exp += 2000.0
//    monstre3.exp += 3000.0
//
//    listOf(monstre1, monstre2, monstre3).forEach { m ->
//        println("Après gain d'XP -> ${m.nom} est niveau ${m.niveau}")
//        printStats(m)
//    }
//
//    // Tester bornes des PV
//
//    println("== Test bornes PV ==")
//    val m = monstre1
//    val pvMaxOriginal = m.pvMax
//    m.pv = -999
//    println("PV après tentative <0: ${m.pv} (attendu 0)")
//    m.pv = pvMaxOriginal + 999
//    println("PV après tentative >pvMax: ${m.pv} (attendu ${m.pvMax})")
}