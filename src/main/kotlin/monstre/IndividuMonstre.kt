package monstre

import dresseur.Entraineur
import kotlin.random.Random
import kotlin.math.roundToInt
import kotlin.math.pow

class IndividuMonstre(
    val id: Int,
    var nom: String,
    val espece: EspeceMonstre,
    val entraineur: Entraineur,
    expInit: Double
) {

    var niveau: Int = 1

    var attaque: Int = espece.baseAttaque + Random.nextInt(-2, 3)
    var defense: Int = espece.baseDefense + Random.nextInt(-2, 3)
    var vitesse: Int = espece.baseVitesse + Random.nextInt(-2, 3)

    var attaqueSpe: Int = espece.baseAttaqueSpe + Random.nextInt(-2, 3)
    var defenseSpe: Int = espece.baseDefenseSpe + Random.nextInt(-2, 3)

    var pvMax: Int = espece.basePv + Random.nextInt(-5, 6)

    var potentiel: Double = Random.nextDouble(0.5, 2.0)

    var exp: Double = 0.0
        get() = field
        set(value) {
            // Assigner field = value
            field = value

            // Vérifier si niveau == 1 et marquer l'état initial
            val estNiveau1 = (niveau == 1)

            // Si on atteint ou dépasse le palier du niveau actuel, on level-up
            if (field >= palierExp(niveau)) {
                // Appeler levelUp() tant que l'exp dépasse les paliers successifs
                // pour gérer les gros gains d'expérience d'un coup
                while (field >= palierExp(niveau)) {
                    levelUp()
                }

                // Si on n'était pas niveau 1 à l'origine (ou qu'on vient de quitter le niveau 1)
                if (!estNiveau1) {
                    println("Le monstre $nom est maintenant niveau $niveau !")
                }
            }
        }

    /**
     *  @property pv  Points de vie actuels.
     *  Ne peut pas être inférieur à 0 ni supérieur à [pvMax].
     */

    var pv: Int = pvMax
        get() = field
        set(nouveauPv) {
            field = nouveauPv.coerceIn(0, pvMax)
        }

    init {
        // applique le setter et déclenche un éventuel level-up
        this.exp = expInit
    }

    /**
     * Calcule l'expérience totale nécessaire pour atteindre un niveau donné.
     *
     * @param niveau Niveau cible.
     * @return Expérience cumulée nécessaire pour atteindre ce niveau.
     */

    fun palierExp(niveau: Int): Double {
        return 100.0 * (niveau - 1).toDouble().pow(2.0)
    }

    /**
     * Augmente le niveau du monstre et met à jour ses caractéristiques.
     *
     * Règles:
     * - Incrémente le niveau.
     * - Chaque caractéristique (sauf pvMax) gagne: round(modCaractéristique * potentiel) + aléa [-2, 2].
     * - pvMax gagne: round(modPv * potentiel) + aléa [-5, 5].
     * - pv gagne le même montant que l'augmentation de pvMax (puis est borné par le setter).
     */

    fun levelUp() {
        // Passage de niveau
        niveau += 1

        // Gagnés pour les stats (arrondis + aléa)
        val gainAtt = espece.modAttaque * potentiel
        val gainDef = espece.modDefense * potentiel
        val gainVit = espece.modVitesse * potentiel
        val gainAttSpe = espece.modAttaqueSpe * potentiel
        val gainDefSpe = espece.modDefenseSpe * potentiel
        val gainPv = espece.modPv * potentiel

        attaque += gainAtt.roundToInt() + Random.nextInt(-2, 3)
        defense += gainDef.roundToInt() + Random.nextInt(-2, 3)
        vitesse += gainVit.roundToInt() + Random.nextInt(-2, 3)
        attaqueSpe += gainAttSpe.roundToInt() + Random.nextInt(-2, 3)
        defenseSpe += gainDefSpe.roundToInt() + Random.nextInt(-2, 3)

        val deltaPvMax = gainPv.roundToInt() + Random.nextInt(-5, 6)
        pvMax += deltaPvMax

        pv += deltaPvMax
    }

    /**
     * Attaque un autre [IndividuMonstre] et inflige des dégâts.
     *
     * Les dégâts sont calculés de manière très simple pour le moment :
     * `dégâts = attaque - (défense / 2)` (minimum 1 dégât).
     *
     * @param cible Monstre cible de l'attaque.
     * @return Les dégâts effectivement infligés.
     */
    
    fun attaquer(cible: IndividuMonstre): Int {

        val degatBrut: Int = this.attaque
        var degatTotal: Int = degatBrut - (cible.defense / 2)

        if (degatTotal < 1) degatTotal = 1

        val pvAvant = cible.pv

        cible.pv -= degatTotal

        val pvApres = cible.pv
        val infliges = pvAvant - pvApres

        println("$nom inflige $infliges dégâts à ${cible.nom}")

        return infliges
    }

    /**
     * Affiche les caractéristiques détaillées du monstre et son art ASCII côte à côte.
     * Implémente l'algorithme du diagramme: découpe en lignes, calcul des largeurs, padding et rendu ligne par ligne.
     */
    fun afficheDetail() {
        // Obtenir l'art ASCII
        val art = espece.afficheArt(true)
        val artLines = art.lines()

        // Construire les lignes de détails
        val separateur = "=".repeat(40)
        val details = buildList {
            add(separateur)
            add("Nom: $nom    Niveau: $niveau")
            add("Exp: $exp")
            add("PV: $pv / $pvMax")
            add(separateur)
            add("Atq: $attaque    Def: $defense    Vitesse: $vitesse")
            add("AtqSpe: $attaqueSpe    DefSpe: $defenseSpe")
            add(separateur)
        }

        // Calculs de largeur et de nombre de lignes
        val maxArtWidth = artLines.maxOfOrNull { it.length } ?: 0
        val maxLines = maxOf(artLines.size, details.size)

        // Affichage côte à côte
        for (i in 0 until maxLines) {
            val artLine = if (i < artLines.size) artLines[i] else ""
            val detailLine = if (i < details.size) details[i] else ""
            val paddedArt = artLine.padEnd(maxArtWidth + 4)
            println(paddedArt + detailLine)
        }
    }
}

/**
 * Demande au joueur de renommer le monstre.
 * Si l'utilisateur entre un texte vide, le nom n'est pas modifié.
 */

fun renommerMonstre(monstre: IndividuMonstre) {
    print("Renommer ${monstre.nom} ? ")
    val nouveauNom = readLine()?.trim().orEmpty()
    if (nouveauNom.isNotEmpty()) {
        monstre.nom = nouveauNom
    }
}