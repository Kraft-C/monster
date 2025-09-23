package item

import monstre.IndividuMonstre
import kotlin.random.Random

class MonsterKube(
    id: Int,
    nom: String,
    description: String,
    var chanceCapture: Double,
) : Item(id, nom, description), Utilisable {

    /**
     * Tente de capturer le monstre cible.
     *
     * Règles:
     * - ratioVie = pv / pvMax ∈ [0.0, 1.0]
     * - chanceEffective = chanceCapture * (1.5 - ratioVie)
     * - chanceEffective >= 5.0 (coerceAtLeast)
     * - tirage aléatoire ∈ [0, 100)
     * - succès si tirage < chanceEffective
     */
    override fun utiliser(cible: IndividuMonstre): Boolean {
        println("Vous lancez le Monster Kube !")

        // ratio de vie entre 0.0 et 1.0 (sécurisé)
        val ratioVie = if (cible.pvMax > 0)
            (cible.pv.toDouble() / cible.pvMax.toDouble()).coerceIn(0.0, 1.0)
        else
            1.0

        // chance effective, avec minimum 5%
        var chanceEffective = chanceCapture * (1.5 - ratioVie)
        chanceEffective = chanceEffective.coerceAtLeast(5.0)

        // tirage aléatoire sur [0, 100)
        val tirage = Random.nextDouble(0.0, 100.0)
        val succes = tirage < chanceEffective

        if (succes) {
            println("Le monstre est capturé !")
            // Option de renommage
            print("Voulez-vous donner un nouveau nom ? ")
            val nouveauNom = readLine()?.trim().orEmpty()
            if (nouveauNom.isNotEmpty()) {
                cible.nom = nouveauNom
            }
            return true
        } else {
            println("Presque ! Le Kube n'a pas pu capturer le monstre !")
            return false
        }
    }
}
