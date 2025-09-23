package monstre

import dresseur.Entraineur
import kotlin.test.Test
import kotlin.test.assertEquals

class IndividuMonstreTest {

    private fun especeTest(): EspeceMonstre =
        EspeceMonstre(
            id = 1,
            nom = "Test",
            type = "Normal",
            baseAttaque = 10,
            baseDefense = 10,
            baseVitesse = 10,
            baseAttaqueSpe = 10,
            baseDefenseSpe = 10,
            basePv = 50,
            modAttaque = 1.0,
            modDefense = 1.0,
            modVitesse = 1.0,
            modAttaqueSpe = 1.0,
            modDefenseSpe = 1.0,
            modPv = 5.0
        )

    private fun monstreDeTest(id: Int, nom: String, entraineur: Entraineur): IndividuMonstre =
        IndividuMonstre(
            id = id,
            nom = nom,
            espece = especeTest(),
            entraineur = entraineur,
            expInit = 0.0
        )

    @Test
    fun `attaquer diminue les PV selon la formule simple`() {
        val dresseurA = Entraineur(1, "A", 100)
        val dresseurB = Entraineur(2, "B", 100)
        val attaquant = monstreDeTest(1, "Attaquant", dresseurA)
        val cible = monstreDeTest(2, "Cible", dresseurB)

        attaquant.attaque = 20
        cible.defense = 6
        cible.pvMax = 100
        cible.pv = 50

        // dégâts attendus: 20 - (6 / 2) = 20 - 3 = 17
        val degats = attaquant.attaquer(cible)

        assertEquals(17, degats, "Les dégâts retournés doivent correspondre au calcul attendu")
        assertEquals(33, cible.pv, "Les PV de la cible doivent avoir diminué correctement")
    }

    @Test
    fun `attaquer inflige au minimum 1 degat`() {
        val dresseurA = Entraineur(3, "A", 100)
        val dresseurB = Entraineur(4, "B", 100)
        val attaquant = monstreDeTest(3, "Attaquant", dresseurA)
        val cible = monstreDeTest(4, "Cible", dresseurB)

        attaquant.attaque = 2
        cible.defense = 10
        cible.pvMax = 100
        cible.pv = 75

        // calcul brut: 2 - (10 / 2) = 2 - 5 = -3 -> min 1
        val degats = attaquant.attaquer(cible)

        assertEquals(1, degats, "Doit infliger au minimum 1 dégât")
        assertEquals(74, cible.pv, "Les PV de la cible doivent être décrémentés d'au moins 1")
    }

    @Test
    fun `attaquer borne les PV de la cible a 0 si necessaire`() {
        val dresseurA = Entraineur(5, "A", 100)
        val dresseurB = Entraineur(6, "B", 100)
        val attaquant = monstreDeTest(5, "Attaquant", dresseurA)
        val cible = monstreDeTest(6, "Cible", dresseurB)

        attaquant.attaque = 100
        cible.defense = 0
        cible.pvMax = 100
        cible.pv = 1

        val degats = attaquant.attaquer(cible)

        assertEquals(1, degats, "Doit infliger exactement ce qu'il faut pour tomber à 0")
        assertEquals(0, cible.pv, "Les PV de la cible ne doivent pas être négatifs")
    }
}
