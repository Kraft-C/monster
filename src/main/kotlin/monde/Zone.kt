package monde

import monstre.EspeceMonstre

class Zone (
    val id: Int,
    val nom: String,
    val expZone: Int,
    val especesmonstres: MutableList<EspeceMonstre> = mutableListOf(),
    val zoneSuivante: Zone? = null,
    val zonePrecedente: Zone? = null

    //TODO - faire la méthode genereMonstre()
    //TODO - faire la méthode rencontreMonstre()
) {
}