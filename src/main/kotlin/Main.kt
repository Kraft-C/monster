import dresseur.Entraineur
import monde.Zone
import monstre.EspeceMonstre

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



fun main() {

    zone1.zoneSuivante = zone2
    zone2.zonePrecedente = zone1


//    joueur.afficheDetail()
//    rival.afficheDetail()
//    joueur.argents+=50
//    joueur.afficheDetail()


    /** permet d'afficher les monstres de face */
    println(Springleaf.afficheArt())
    println(Flamkip.afficheArt())
    println(Aquamy.afficheArt())


    /** permet d'afficher les monstres de dos grace a false*/
    println(Springleaf.afficheArt(false))
    println(Flamkip.afficheArt(false))
    println(Aquamy.afficheArt(false))

}