package jeu

import item.Utilisable
import monstre.IndividuMonstre

/**
 * Représente un combat entre un individu monstre du joueur et un individu monstre sauvage.
 *
 * @property monstreJoueur L'individu monstre contrôlé par le joueur.
 * @property monstreSauvage L'individu monstre adverse (sauvage).
 */
class CombatMonstre(
    var monstreJoueur: IndividuMonstre,
    val monstreSauvage: IndividuMonstre
) {
    // Numéro du round, commence à 1
    var round: Int = 1

    /**
     * Affiche l'état du combat et les arts ASCII des deux monstres.
     *
     * Détails affichés dans l'ordre:
     * - Entête avec le numéro du round
     * - Niveau et PV du monstre sauvage
     * - ASCII art du monstre sauvage (de face)
     * - ASCII art du monstre du joueur (de dos)
     * - Niveau et PV du monstre du joueur
     */
    fun afficheCombat() {
        println("======== Début Round : $round ========")

        // Infos monstre sauvage
        println("Niveau : ${monstreSauvage.niveau}")
        println("PV : ${monstreSauvage.pv} / ${monstreSauvage.pvMax}")
        println(monstreSauvage.espece.afficheArt(true))  // de face

        // Art du monstre du joueur (de dos), puis ses infos
        println(monstreJoueur.espece.afficheArt(false)) // de dos
        println("Niveau : ${monstreJoueur.niveau}")
        println("PV : ${monstreJoueur.pv} / ${monstreJoueur.pvMax}")
    }

    /**
     * Indique si le joueur a gagné le combat.
     *
     * Deux conditions de victoire :
     * - Le monstre sauvage est capturé.
     * - Les PV du monstre sauvage sont réduits à 0.
     *
     * Le monstre du joueur gagne de l'expérience uniquement si le monstre sauvage est vaincu (PV <= 0).
     *
     * @return true si le joueur a gagné, sinon false.
     */
    fun joueurGagne(): Boolean {
        // Victoire par K.O. du monstre sauvage
        if (monstreSauvage.pv <= 0) {
            println("${monstreJoueur.entraineur.nom} a gagné !")
            val gainExp = monstreSauvage.exp * 0.20
            monstreJoueur.exp += gainExp
            println("${monstreJoueur.nom} gagne $gainExp exp")
            return true
        }

        // Victoire par capture (le monstre sauvage appartient désormais à l'entraîneur du joueur)
        if (monstreSauvage.entraineur == monstreJoueur.entraineur) {
            println("${monstreSauvage.nom} a été capturé !")
            return true
        }

        // Le combat n'est pas gagné
        return false
    }

    /**
     * Exécute l'action de l'adversaire.
     *
     * Si le monstre sauvage a encore des PV (> 0), il attaque le monstre du joueur.
     */
    fun actionAdversaire() {
        if (monstreSauvage.pv > 0) {
            monstreSauvage.attaquer(monstreJoueur)
        }
    }

    /**
     * Gère l'action du joueur et indique si le combat doit continuer.
     *
     * Choix possibles:
     * 1. Attaquer: le monstre du joueur attaque le monstre sauvage.
     * 2. Utiliser un objet: possibilité d'utiliser un item (ex: capture). Si la capture réussit, le combat se termine.
     * 3. Changer de monstre: le joueur peut remplacer le monstre actuel par un autre de son équipe (ayant des PV > 0).
     *
     * @return true si le combat doit continuer, false sinon.
     */
    fun actionJoueur(): Boolean {
        val joueur = monstreJoueur.entraineur

        // Vérifier si le joueur peut encore se battre
        if (joueur.gameOver()) {
            return false
        }

        println("Que voulez-vous faire ?")
        println("  1) Attaquer")
        println("  2) Utiliser un objet")
        println("  3) Changer de monstre")
        print("Votre choix: ")
        val choixAction = readLine()?.trim()?.toIntOrNull()

        when (choixAction) {
            1 -> {
                monstreJoueur.attaquer(monstreSauvage)
                // Si le joueur a gagné (K.O. du sauvage, ou autre condition), fin du combat.
                return !joueurGagne()
            }

            2 -> {
                val sac = joueur.sacAItems
                if (sac.isEmpty()) {
                    println("Votre sac est vide.")
                    return true
                }

                println("Contenu du sac:")
                sac.forEachIndexed { i, item -> println("  [$i] ${item.nom}") }
                print("Entrez l'index de l'objet à utiliser: ")
                val indexChoix = readLine()?.trim()?.toIntOrNull()
                if (indexChoix == null || indexChoix !in sac.indices) {
                    println("Choix invalide.")
                    return true
                }

                val objetChoisi = sac[indexChoix]
                if (objetChoisi is Utilisable) {
                    val captureReussie = objetChoisi.utiliser(monstreSauvage)
                    if (captureReussie) {
                        // Victoire par capture -> le combat se termine
                        println("${monstreSauvage.nom} a été capturé !")
                        return false
                    }
                    // Sinon, le combat continue
                    return true
                } else {
                    println("Objet non utilisable.")
                    return true
                }
            }

            3 -> {
                val disponibles = joueur.equipeMonstre.filter { it.pv > 0 }
                if (disponibles.isEmpty()) {
                    println("Aucun monstre apte au combat dans l'équipe.")
                    return true
                }

                println("Choisissez un monstre (PV > 0) :")
                disponibles.forEachIndexed { i, m ->
                    val tag = if (m === monstreJoueur) " (actuel)" else ""
                    println("  [$i] ${m.nom} - PV: ${m.pv}/${m.pvMax}$tag")
                }
                print("Votre choix: ")
                val indexChoix = readLine()?.trim()?.toIntOrNull()
                if (indexChoix == null || indexChoix !in disponibles.indices) {
                    println("Choix invalide.")
                    return true
                }

                val choixMonstre = disponibles[indexChoix]
                if (choixMonstre.pv <= 0) {
                    println("Impossible ! Ce monstre est KO")
                    return true
                }

                println("${choixMonstre.nom} remplace ${monstreJoueur.nom}")
                monstreJoueur = choixMonstre
                return true
            }

            else -> {
                println("Choix invalide.")
                return true
            }
        }
    }

    /**
     * Joue un round en faisant agir d'abord le monstre le plus rapide.
     *
     * Séquence:
     * - Affiche l'état du combat.
     * - Si le joueur est plus rapide: actionJoueur(); si false -> return; puis actionAdversaire().
     * - Sinon: actionAdversaire(); si le joueur n'est pas en game over, alors actionJoueur(); si false -> return.
     * - Incrémente le numéro de round en fin d'exécution.
     */
    fun jouer() {
        val joueurPlusRapide = monstreJoueur.vitesse >= monstreSauvage.vitesse

        // Affichage du début de round
        afficheCombat()

        if (joueurPlusRapide) {
            val continuer = actionJoueur()
            if (!continuer) {
                return
            }
            // L'adversaire joue si le combat continue
            actionAdversaire()
        } else {
            // L'adversaire joue d'abord
            actionAdversaire()

            // Si le joueur n'a pas perdu, il peut agir
            if (!monstreJoueur.entraineur.gameOver()) {
                val continuer = actionJoueur()
                if (!continuer) {
                    return
                }
            }
        }

        // Passage au round suivant
        round += 1
    }
    /**
     * Lance le combat et gère les rounds jusqu'à la victoire ou la défaite.
     *
     * Affiche un message de fin si le joueur perd et restaure les PV
     * de tous ses monstres.
     */
    fun lanceCombat() {
        val joueur = monstreJoueur.entraineur
        while (!joueur.gameOver() && !joueurGagne()) {
            this.jouer()
            println("======== Fin du Round : $round ========")
            round++
        }
        if (joueur.gameOver()) {
            joueur.equipeMonstre.forEach { it.pv = it.pvMax }
            println("Game Over !")
        }
    }
}