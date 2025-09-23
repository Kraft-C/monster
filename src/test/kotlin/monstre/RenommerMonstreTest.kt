package monstre

import dresseur.Entraineur
import java.io.ByteArrayInputStream
import kotlin.test.Test
import kotlin.test.assertEquals

class RenommerMonstreTest {

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
    fun `renommerMonstre met a jour le nom si saisie non vide`() {
        val entraineur = Entraineur(1, "Dresseur", 100)
        val monstre = monstreDeTest(1, "AncienNom", entraineur)

        val originalIn = System.`in`
        try {
            val saisie = "NouveauNom\n"
            System.setIn(ByteArrayInputStream(saisie.toByteArray()))

            renommerMonstre(monstre)

            assertEquals("NouveauNom", monstre.nom, "Le nom du monstre doit être mis à jour avec la saisie non vide")
        } finally {
            System.setIn(originalIn)
        }
    }

    @Test
    fun `renommerMonstre ne change pas le nom si saisie vide ou espaces`() {
        val entraineur = Entraineur(2, "Dresseur", 100)
        val monstre = monstreDeTest(2, "NomInitial", entraineur)

        val originalIn = System.`in`
        try {
            val saisie = "   \n" // après trim() => vide
            System.setIn(ByteArrayInputStream(saisie.toByteArray()))

            renommerMonstre(monstre)

            assertEquals("NomInitial", monstre.nom, "Le nom ne doit pas changer si la saisie est vide")
        } finally {
            System.setIn(originalIn)
        }
    }
}
