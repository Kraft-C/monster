package jeu

import dresseur.Entraineur
import monde.Zone
import monstre.EspeceMonstre
import monstre.IndividuMonstre
import monstre.renommerMonstre
import jeu.CombatMonstre
import zone1

class Partie(
    var id: Int,
    var joueur: Entraineur,
    var zone: Zone
) {
    /**
     * Boucle principale de jeu pour la partie.
     * Affiche la zone courante et propose des actions:
     * 1 => Rencontrer un monstre sauvage (appelle zone.rencontreMonstre())
     * 2 => Examiner l’équipe (examineEquipe())
     * 3 => Aller à la zone suivante (si zoneSuivante != null)
     * 4 => Aller à la zone précédente (si zonePrecedente != null)
     * q => Quitter la boucle jouer()
     */
    fun jouer() {
        while (true) {
            println("=== Vous êtes dans la zone: ${zone.nom} ===")
            println("Que voulez-vous faire ?")
            println("1 => Rencontrer un monstre sauvage")
            println("2 => Examiner l’équipe de monstres")
            println("3 => Aller à la zone suivante")
            println("4 => Aller à la zone précédente")
            println("q => Quitter")

            when (readLine()?.trim()?.lowercase()) {
                "1" -> {
                    try {
                        // Peut ne rien faire si la zone n'a pas encore implémenté rencontreMonstre
                        zone.CombatMonstre

                    } catch (e: Throwable) {
                        println("Action indisponible: ${e.message}")
                    }
                }
                "2" -> examineEquipe()
                "3" -> {
                    val suivante = zone.zoneSuivante
                    if (suivante != null) {
                        zone = suivante
                        println("Vous vous déplacez vers: ${zone.nom}")
                    } else {
                        println("Il n'y a pas de zone suivante.")
                    }
                }
                "4" -> {
                    val precedente = zone.zonePrecedente
                    if (precedente != null) {
                        zone = precedente
                        println("Vous retournez vers: ${zone.nom}")
                    } else {
                        println("Il n'y a pas de zone précédente.")
                    }
                }
                "q" -> return
                else -> println("Choix invalide. Veuillez réessayer.")
            }
        }
    }
    /**
     * Affiche les informations de l'équipe du joueur et propose des actions.
     * - Affiche la liste des monstres avec leurs positions.
     * - Tapez un numéro pour voir le détail d'un monstre.
     * - Tapez 'm' pour modifier l'ordre de l'équipe.
     * - Tapez 'q' pour revenir au menu principal.
     */
    fun examineEquipe() {
        while (true) {
            val equipe = joueur.equipeMonstre
            println("=== Équipe du joueur ===")
            if (equipe.isEmpty()) {
                println("Aucun monstre dans l'équipe.")
                println("Tapez 'q' pour revenir.")
            } else {
                equipe.forEachIndexed { i, m ->
                    println("${i + 1}. ${m.nom} (PV: ${m.pv})")
                }
                println("- Tapez le numéro d'un monstre pour voir son détail")
            }
            println("- Tapez 'm' pour modifier l'ordre des monstres")
            println("- Tapez 'q' pour revenir au menu principal")

            val reponseBrute = readLine()
            val reponse = reponseBrute?.trim() ?: ""
            when (reponse.lowercase()) {
                "" -> println("Entrée vide, veuillez réessayer.")
                "q" -> return
                "m" -> {
                    modifierOrdreEquipe()
                }
                else -> {
                    val numero = reponse.toIntOrNull()
                    if (numero == null) {
                        println("Entrée invalide: tapez un numéro, 'm' ou 'q'.")
                    } else {
                        val equipeSize = joueur.equipeMonstre.size
                        if (numero !in 1..equipeSize) {
                            println("Numéro hors limites. Choisissez entre 1 et $equipeSize.")
                        } else {
                            val monstre = joueur.equipeMonstre[numero - 1]
                            println("=== Détail du monstre #$numero ===")
                            monstre.afficheDetail()
                            // Après affichage du détail, on reste dans la boucle.
                        }
                    }
                }
            }
        }
    }
    /**
     * Permet d'inverser la position de deux monstres dans l'équipe du joueur.
     * Identique au comportement demandé pour Entraineur.modifierOrdreEquipe(),
     * mais exposé au niveau de la Partie pour un accès pratique côté jeu.
     */
    fun modifierOrdreEquipe() {
        val equipe = joueur.equipeMonstre
        if (equipe.size < 2) {
            println("Impossible de modifier l'ordre: il faut au moins 2 monstres dans l'équipe.")
            return
        }
        println("=== Équipe actuelle ===")
        equipe.forEachIndexed { index, monstre ->
            println("${index + 1}. ${monstre.nom} (PV: ${monstre.pv})")
        }
        fun lirePosition(message: String): Int? {
            println(message)
            val input = readLine()?.trim()
            val pos = input?.toIntOrNull()
            if (pos == null) {
                println("Entrée invalide: veuillez saisir un nombre.")
                return null
            }
            if (pos !in 1..equipe.size) {
                println("Position hors limites. Doit être comprise entre 1 et ${equipe.size}.")
                return null
            }
            return pos
        }
        val posA = lirePosition("Entrez la position du monstre à déplacer (1..${equipe.size}) :") ?: return
        val posB = lirePosition("Entrez la nouvelle position (1..${equipe.size}) :") ?: return
        if (posA == posB) {
            println("Les positions sont identiques: aucun changement effectué.")
            return
        }
        val i = posA - 1
        val j = posB - 1
        val tmp = equipe[i]
        equipe[i] = equipe[j]
        equipe[j] = tmp
        println("Ordre modifié: ${posA} <-> ${posB}")
        println("=== Nouvelle équipe ===")
        equipe.forEachIndexed { index, monstre ->
            println("${index + 1}. ${monstre.nom} (PV: ${monstre.pv})")
        }
    }
    // 1) Retourne le premier monstre apte au combat (PV > 0), sinon null.
    fun monstreActif(): IndividuMonstre? =
        joueur.equipeMonstre.firstOrNull { it.pv > 0 }

    // 2) Démarre un combat contre un monstre sauvage donné.
    //    Gère le cas où aucun monstre du joueur n'est disponible.
    fun demarrerCombat(monstreSauvage: IndividuMonstre) {
        val actif = monstreActif()
        if (actif == null) {
            println("Aucun monstre apte au combat.")
            return
        }
        val combat = CombatMonstre(actif, monstreSauvage)
        combat.lanceCombat()
    }

    // 3) Change la zone courante.
    fun changerZone(nouvelleZone: Zone) {
        zone = nouvelleZone
    }

    // 4) Affiche un état simple de la partie (sans hypothèse forte sur Zone).
    fun afficherEtat() {
        println("Partie #$id")
        joueur.afficheDetail()
        println("Zone actuelle: $zone")
    }

    // 5) Propose 3 starters au joueur, en ajoute un à l'équipe et confirme son appartenance.
    fun choixStarter() {
        // Création des espèces de départ
        val springleaf = EspeceMonstre(
            id = 101, nom = "Springleaf", type = "Plante",
            baseAttaque = 7, baseDefense = 9, baseVitesse = 6,
            baseAttaqueSpe = 8, baseDefenseSpe = 9, basePv = 30,
            modAttaque = 1.5, modDefense = 1.5, modVitesse = 1.2,
            modAttaqueSpe = 1.5, modDefenseSpe = 1.5, modPv = 4.0,
            description = "Un starter Plante équilibré et robuste."
        )
        val flamkip = EspeceMonstre(
            id = 102, nom = "Flamkip", type = "Feu",
            baseAttaque = 9, baseDefense = 6, baseVitesse = 8,
            baseAttaqueSpe = 9, baseDefenseSpe = 6, basePv = 28,
            modAttaque = 1.8, modDefense = 1.1, modVitesse = 1.6,
            modAttaqueSpe = 1.7, modDefenseSpe = 1.1, modPv = 3.6,
            description = "Un starter Feu offensif et rapide."
        )
        val aquamy = EspeceMonstre(
            id = 103, nom = "Aquamy", type = "Eau",
            baseAttaque = 8, baseDefense = 8, baseVitesse = 7,
            baseAttaqueSpe = 8, baseDefenseSpe = 8, basePv = 32,
            modAttaque = 1.6, modDefense = 1.4, modVitesse = 1.3,
            modAttaqueSpe = 1.5, modDefenseSpe = 1.4, modPv = 4.2,
            description = "Un starter Eau polyvalent."
        )

        // Création des individus (appartiennent au joueur)
        val monstre1 = IndividuMonstre(id = 1, nom = "Springleaf", espece = springleaf, entraineur = joueur, expInit = 0.0)
        val monstre2 = IndividuMonstre(id = 2, nom = "Flamkip", espece = flamkip, entraineur = joueur, expInit = 0.0)
        val monstre3 = IndividuMonstre(id = 3, nom = "Aquamy", espece = aquamy, entraineur = joueur, expInit = 0.0)
        val starters = listOf(monstre1, monstre2, monstre3)

        // Boucle de choix
        while (true) {
            println("=== Choix du starter ===")
            println("1/ Springleaf"); monstre1.afficheDetail()
            println("2/ Flamkip");    monstre2.afficheDetail()
            println("3/ Aquamy");     monstre3.afficheDetail()
            println("Choisissez votre starter (1/2/3) :")
            val choix = readLine()?.trim()?.toIntOrNull()
            if (choix != null && choix in 1..3) {
                val starter = starters[choix - 1]
                println("Vous avez choisi ${starter.nom}")
                // Renommer si souhaité
                renommerMonstre(starter)
                // Ajouter à l'équipe du joueur
                joueur.equipeMonstre.add(starter)
                println("${starter.nom} rejoint l'équipe du joueur !")
                break
            } else {
                println("Choix invalide, veuillez recommencer.")
            }


        }
    }
}