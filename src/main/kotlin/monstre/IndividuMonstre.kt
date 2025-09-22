package monstre

import dresseur.Entraineur
import kotlin.random.Random
import kotlin.math.roundToInt
import kotlin.math.pow

class IndividuMonstre(
    val id: Int,
    val nom: String,
    val espece: Double,
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
}