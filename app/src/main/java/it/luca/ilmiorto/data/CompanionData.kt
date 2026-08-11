package it.luca.ilmiorto.data

enum class PlacementRole(val label: String, val priority: Int) {
    EDGE("lungo un bordo", 0),
    STAKE("in file o lungo i tutori", 1),
    COMPANION("vicino alla coltura associata", 2),
    COMPACT("in file compatte", 3),
    ROW("in file regolari", 3),
    FREE("posizione libera", 4),
}

data class CompanionSource(
    val id: String,
    val title: String,
    val url: String,
)

data class CompanionRule(
    val firstCatalogId: String,
    val secondCatalogId: String,
    val relation: String,
    val evidence: String,
    val benefit: String,
    val placement: String,
    val minimumDistanceCm: Double,
    val maximumDistanceCm: Double? = null,
    val sourceIds: List<String>,
)

val COMPANION_SOURCES = listOf(
    CompanionSource(
        "umn",
        "University of Minnesota Extension — Companion planting in home gardens",
        "https://extension.umn.edu/gardening-minnesota/companion-planting-home-gardens",
    ),
    CompanionSource(
        "delaware",
        "University of Delaware Cooperative Extension — Adding diversity to the garden",
        "https://www.udel.edu/academics/colleges/canr/cooperative-extension/fact-sheets/adding-diversity-garden/",
    ),
    CompanionSource(
        "basilStudy",
        "Mamo et al. — Tomato–basil intercropping: density and row arrangements",
        "https://jurnal.uns.ac.id/arj/article/view/54333",
    ),
    CompanionSource(
        "tomatoLettuce",
        "Rezende et al. — Agronomic efficiency of intercropping tomato and lettuce",
        "https://pubmed.ncbi.nlm.nih.gov/21861045/",
    ),
    CompanionSource(
        "cucumberLettuce",
        "Agriculture (MDPI) — Cucumber–lettuce intercropping and land equivalent ratio",
        "https://www.mdpi.com/2077-0472/10/3/88",
    ),
    CompanionSource(
        "lettuceRocket",
        "PubMed — Lettuce–rocket intercropping and land-use efficiency",
        "https://pubmed.ncbi.nlm.nih.gov/29698401/",
    ),
)

val COMPANION_RULES = listOf(
    CompanionRule(
        "pomodoro", "basilico", "Favorevole", "Evidenza media",
        "Intercoltura studiata; può favorire diversificazione, uso dello spazio e, in alcune condizioni, crescita o gestione di alcuni insetti.",
        "Collocare il basilico vicino alle file dei pomodori, indicativamente a 25–50 cm dal fusto, evitando ombra fitta e competizione eccessiva.",
        25.0, 50.0, listOf("umn", "basilStudy"),
    ),
    CompanionRule(
        "pomodoro", "lattuga", "Favorevole per lo spazio", "Evidenza media",
        "In prove di intercoltura il sistema ha migliorato l’efficienza d’uso del terreno in specifiche epoche di trapianto.",
        "Lattuga negli spazi liberi o davanti alle file, da raccogliere prima che la chioma del pomodoro chiuda completamente.",
        20.0, 65.0, listOf("tomatoLettuce"),
    ),
    CompanionRule(
        "cetriolo", "lattuga", "Favorevole in condizioni studiate", "Evidenza media",
        "Alcune densità di intercoltura hanno ottenuto un rapporto equivalente di terreno superiore alla monocoltura.",
        "Lattuga nella fascia libera tra le file, controllando ombreggiamento e disponibilità idrica.",
        20.0, 70.0, listOf("cucumberLettuce"),
    ),
    CompanionRule(
        "lattuga", "rucola", "Utile ma competitiva", "Evidenza media",
        "L’efficienza complessiva d’uso del terreno può aumentare, anche se la resa individuale delle colture può diminuire.",
        "File alternate e densità moderate; evitare di riempire tutti gli spazi senza considerare la competizione.",
        10.0, 35.0, listOf("lettuceRocket"),
    ),
    CompanionRule(
        "pomodoro", "tagete", "Tradizionale / incerta", "Evidenza limitata",
        "Spesso consigliato nelle tabelle tradizionali, ma le prove dipendono da parassita, varietà e disposizione.",
        "Usare soprattutto per diversificare e attirare insetti, senza considerarlo un repellente garantito.",
        25.0, 80.0, listOf("umn", "delaware"),
    ),
    CompanionRule(
        "zucchino", "basilico", "Non dimostrata", "Evidenza limitata",
        "Non è inclusa fra le consociazioni con supporto specifico nella banca dati iniziale.",
        "Può convivere se luce, acqua e spazio sono sufficienti; non viene applicato alcun vincolo favorevole automatico.",
        30.0, null, listOf("delaware"),
    ),
)

fun companionSource(id: String): CompanionSource? = COMPANION_SOURCES.firstOrNull { it.id == id }

fun companionRule(firstCatalogId: String, secondCatalogId: String): CompanionRule? =
    COMPANION_RULES.firstOrNull {
        (it.firstCatalogId == firstCatalogId && it.secondCatalogId == secondCatalogId) ||
            (it.firstCatalogId == secondCatalogId && it.secondCatalogId == firstCatalogId)
    }

fun Crop.catalogKey(): String = (catalogItem(catalogId) ?: inferCatalog(name))?.id ?: "custom"

fun cropPlacementRole(crop: Crop): PlacementRole = when (crop.catalogKey()) {
    "zucchino", "trombetta", "hokkaido" -> PlacementRole.EDGE
    "pomodoro", "fagiolino", "cetriolo" -> PlacementRole.STAKE
    "basilico", "tagete" -> PlacementRole.COMPANION
    "lattuga", "rucola", "cipolla", "porro", "barbabietola", "finocchio" -> PlacementRole.COMPACT
    "custom" -> PlacementRole.FREE
    else -> PlacementRole.ROW
}
