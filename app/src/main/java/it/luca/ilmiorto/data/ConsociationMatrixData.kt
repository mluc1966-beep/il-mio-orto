package it.luca.ilmiorto.data

data class ConsociationEntity(
    val id: String,
    val canonicalName: String,
    val displayName: String,
    val level: String,
)

data class ConsociationMatrixRelation(
    val id: String,
    val firstEntityId: String,
    val secondEntityId: String,
    val code: String,
    val label: String,
    val source: String,
    val appPolicy: String,
)

val CONSOCIATION_ENTITIES = listOf(
    ConsociationEntity("alliums", "Alliums", "Allium (gruppo)", "Gruppo/famiglia"),
    ConsociationEntity("asparagus", "Asparagus", "Asparago", "Coltura"),
    ConsociationEntity("beans_bush", "Beans, bush", "Fagiolo nano", "Coltura"),
    ConsociationEntity("beans_pole", "Beans, pole", "Fagiolo rampicante", "Coltura"),
    ConsociationEntity("beans_fava", "Beans, fava", "Fava", "Coltura"),
    ConsociationEntity("beets", "Beets", "Barbabietola", "Coltura"),
    ConsociationEntity("brassicas", "Brassicas", "Brassicacee (gruppo)", "Gruppo/famiglia"),
    ConsociationEntity("broccoli", "Broccoli", "Broccolo", "Coltura"),
    ConsociationEntity("brussels_sprouts", "Brussels sprouts", "Cavoletti di Bruxelles", "Coltura"),
    ConsociationEntity("cabbage", "Cabbage", "Cavolo", "Coltura"),
    ConsociationEntity("carrots", "Carrots", "Carota", "Coltura"),
    ConsociationEntity("cauliflower", "Cauliflower", "Cavolfiore", "Coltura"),
    ConsociationEntity("celery", "Celery", "Sedano", "Coltura"),
    ConsociationEntity("chard", "Chard", "Bietola", "Coltura"),
    ConsociationEntity("corn_maize", "Corn / Maize", "Mais", "Coltura"),
    ConsociationEntity("cucumber", "Cucumber", "Cetriolo", "Coltura"),
    ConsociationEntity("cucurbits", "Cucurbits", "Cucurbitacee (gruppo)", "Gruppo/famiglia"),
    ConsociationEntity("eggplant_or_aubergine", "Eggplant or Aubergine", "Melanzana", "Coltura"),
    ConsociationEntity("kohlrabi", "Kohlrabi", "Cavolo rapa", "Coltura"),
    ConsociationEntity("leek", "Leek", "Porro", "Coltura"),
    ConsociationEntity("legumes", "Legumes", "Leguminose (gruppo)", "Gruppo/famiglia"),
    ConsociationEntity("lettuce", "Lettuce", "Lattuga", "Coltura"),
    ConsociationEntity("mustard", "Mustard", "Senape", "Coltura"),
    ConsociationEntity("nightshades", "Nightshades", "Solanacee (gruppo)", "Gruppo/famiglia"),
    ConsociationEntity("okra", "Okra", "Okra", "Coltura"),
    ConsociationEntity("onion", "Onion", "Cipolla", "Coltura"),
    ConsociationEntity("parsnip", "Parsnip", "Pastinaca", "Coltura"),
    ConsociationEntity("peas", "Peas", "Pisello", "Coltura"),
    ConsociationEntity("peppers", "Peppers", "Peperone", "Coltura"),
    ConsociationEntity("potato", "Potato", "Patata", "Coltura"),
    ConsociationEntity("pumpkin", "Pumpkin", "Zucca", "Coltura"),
    ConsociationEntity("radish", "Radish", "Ravanello", "Coltura"),
    ConsociationEntity("soybean", "Soybean", "Soia", "Coltura"),
    ConsociationEntity("spinach", "Spinach", "Spinacio", "Coltura"),
    ConsociationEntity("squash", "Squash", "Zucchino / zucca estiva", "Coltura"),
    ConsociationEntity("sweet_potato", "Sweet potato", "Patata dolce", "Coltura"),
    ConsociationEntity("tomatoes", "Tomatoes", "Pomodoro", "Coltura"),
    ConsociationEntity("turnips_and_rutabagas", "Turnips and rutabagas", "Rapa e rutabaga", "Coltura"),
    ConsociationEntity("basil", "Basil", "Basilico", "Coltura"),
    ConsociationEntity("artichoke", "Artichoke", "Carciofo", "Coltura"),
    ConsociationEntity("cardoon", "Cardoon", "Cardo", "Coltura"),
    ConsociationEntity("chicory", "Chicory", "Cicoria", "Coltura"),
    ConsociationEntity("endive", "Endive", "Indivia e scarola", "Coltura"),
    ConsociationEntity("fennel", "Fennel", "Finocchio", "Coltura"),
    ConsociationEntity("strawberry", "Strawberry", "Fragola", "Coltura"),
    ConsociationEntity("melon", "Melon", "Melone", "Coltura"),
    ConsociationEntity("parsley", "Parsley", "Prezzemolo", "Coltura"),
    ConsociationEntity("arugula", "Arugula", "Rucola", "Coltura"),
    ConsociationEntity("corn_salad", "Corn salad", "Valeriana", "Coltura"),
    ConsociationEntity("watermelon", "Watermelon", "Anguria", "Coltura"),
)

val CONSOCIATION_RELATIONS = listOf(
    ConsociationMatrixRelation(
        "alliums__asparagus", "alliums", "asparagus", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "alliums__beets", "alliums", "beets", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "alliums__brassicas", "alliums", "brassicas", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "alliums__carrots", "alliums", "carrots", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "alliums__cucumber", "alliums", "cucumber", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "alliums__legumes", "alliums", "legumes", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "alliums__lettuce", "alliums", "lettuce", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "alliums__nightshades", "alliums", "nightshades", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "alliums__peas", "alliums", "peas", "M",
        "Mista / fonti discordanti", "GitHub/Wikipedia + Orto Mio (fonti discordanti)", "Solo informativa; non usare per la disposizione automatica"
    ),
    ConsociationMatrixRelation(
        "alliums__peppers", "alliums", "peppers", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "alliums__potato", "alliums", "potato", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "alliums__tomatoes", "alliums", "tomatoes", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "alliums__strawberry", "alliums", "strawberry", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "asparagus__beans_bush", "asparagus", "beans_bush", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "asparagus__lettuce", "asparagus", "lettuce", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "asparagus__onion", "asparagus", "onion", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "asparagus__potato", "asparagus", "potato", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "asparagus__spinach", "asparagus", "spinach", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "asparagus__tomatoes", "asparagus", "tomatoes", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beans_bush__beets", "beans_bush", "beets", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beans_bush__cabbage", "beans_bush", "cabbage", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beans_bush__celery", "beans_bush", "celery", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beans_bush__cucumber", "beans_bush", "cucumber", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beans_bush__eggplant_or_aubergine", "beans_bush", "eggplant_or_aubergine", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beans_bush__legumes", "beans_bush", "legumes", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "beans_bush__lettuce", "beans_bush", "lettuce", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beans_bush__soybean", "beans_bush", "soybean", "M",
        "Mista / fonti discordanti", "GitHub/Wikipedia + Orto Mio (fonti discordanti)", "Solo informativa; non usare per la disposizione automatica"
    ),
    ConsociationMatrixRelation(
        "beans_pole__beets", "beans_pole", "beets", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "beans_pole__brassicas", "beans_pole", "brassicas", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "beans_pole__corn_maize", "beans_pole", "corn_maize", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beans_pole__kohlrabi", "beans_pole", "kohlrabi", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "beans_pole__radish", "beans_pole", "radish", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beans_fava__cabbage", "beans_fava", "cabbage", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beans_fava__celery", "beans_fava", "celery", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beans_fava__lettuce", "beans_fava", "lettuce", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beans_fava__potato", "beans_fava", "potato", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beets__brassicas", "beets", "brassicas", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beets__broccoli", "beets", "broccoli", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beets__cabbage", "beets", "cabbage", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beets__cucumber", "beets", "cucumber", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beets__kohlrabi", "beets", "kohlrabi", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beets__legumes", "beets", "legumes", "M",
        "Mista / fonti discordanti", "GitHub/Wikipedia + Orto Mio (fonti discordanti)", "Solo informativa; non usare per la disposizione automatica"
    ),
    ConsociationMatrixRelation(
        "beets__lettuce", "beets", "lettuce", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beets__onion", "beets", "onion", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "beets__tomatoes", "beets", "tomatoes", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "brassicas__celery", "brassicas", "celery", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "brassicas__chard", "brassicas", "chard", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "brassicas__corn_maize", "brassicas", "corn_maize", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "brassicas__legumes", "brassicas", "legumes", "M",
        "Mista / fonti discordanti", "GitHub/Wikipedia + Orto Mio (fonti discordanti)", "Solo informativa; non usare per la disposizione automatica"
    ),
    ConsociationMatrixRelation(
        "brassicas__mustard", "brassicas", "mustard", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "brassicas__nightshades", "brassicas", "nightshades", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "brassicas__onion", "brassicas", "onion", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "brassicas__peas", "brassicas", "peas", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "brassicas__peppers", "brassicas", "peppers", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "brassicas__potato", "brassicas", "potato", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "brassicas__spinach", "brassicas", "spinach", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "brassicas__tomatoes", "brassicas", "tomatoes", "M",
        "Mista / fonti discordanti", "GitHub/Wikipedia + Orto Mio (fonti discordanti)", "Solo informativa; non usare per la disposizione automatica"
    ),
    ConsociationMatrixRelation(
        "broccoli__lettuce", "broccoli", "lettuce", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "broccoli__mustard", "broccoli", "mustard", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "broccoli__onion", "broccoli", "onion", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "broccoli__tomatoes", "broccoli", "tomatoes", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "broccoli__turnips_and_rutabagas", "broccoli", "turnips_and_rutabagas", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "brussels_sprouts__mustard", "brussels_sprouts", "mustard", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "brussels_sprouts__peppers", "brussels_sprouts", "peppers", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "cabbage__cauliflower", "cabbage", "cauliflower", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cabbage__celery", "cabbage", "celery", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cabbage__chard", "cabbage", "chard", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cabbage__eggplant_or_aubergine", "cabbage", "eggplant_or_aubergine", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cabbage__leek", "cabbage", "leek", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cabbage__legumes", "cabbage", "legumes", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cabbage__lettuce", "cabbage", "lettuce", "M",
        "Mista / fonti discordanti", "GitHub/Wikipedia + Orto Mio (fonti discordanti)", "Solo informativa; non usare per la disposizione automatica"
    ),
    ConsociationMatrixRelation(
        "cabbage__mustard", "cabbage", "mustard", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cabbage__onion", "cabbage", "onion", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cabbage__peas", "cabbage", "peas", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cabbage__peppers", "cabbage", "peppers", "M",
        "Mista / fonti discordanti", "GitHub/Wikipedia + Orto Mio (fonti discordanti)", "Solo informativa; non usare per la disposizione automatica"
    ),
    ConsociationMatrixRelation(
        "cabbage__pumpkin", "cabbage", "pumpkin", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cabbage__tomatoes", "cabbage", "tomatoes", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cabbage__fennel", "cabbage", "fennel", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cabbage__melon", "cabbage", "melon", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "carrots__cucumber", "carrots", "cucumber", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "carrots__leek", "carrots", "leek", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "carrots__legumes", "carrots", "legumes", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "carrots__lettuce", "carrots", "lettuce", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "carrots__nightshades", "carrots", "nightshades", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "carrots__onion", "carrots", "onion", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "carrots__parsnip", "carrots", "parsnip", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "carrots__potato", "carrots", "potato", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "carrots__radish", "carrots", "radish", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "carrots__tomatoes", "carrots", "tomatoes", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cauliflower__celery", "cauliflower", "celery", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cauliflower__legumes", "cauliflower", "legumes", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cauliflower__mustard", "cauliflower", "mustard", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cauliflower__peas", "cauliflower", "peas", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cauliflower__spinach", "cauliflower", "spinach", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "celery__corn_maize", "celery", "corn_maize", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "celery__cucumber", "celery", "cucumber", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "celery__eggplant_or_aubergine", "celery", "eggplant_or_aubergine", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "celery__leek", "celery", "leek", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "celery__legumes", "celery", "legumes", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "celery__lettuce", "celery", "lettuce", "M",
        "Mista / fonti discordanti", "GitHub/Wikipedia + Orto Mio (fonti discordanti)", "Solo informativa; non usare per la disposizione automatica"
    ),
    ConsociationMatrixRelation(
        "celery__okra", "celery", "okra", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "celery__pumpkin", "celery", "pumpkin", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "celery__tomatoes", "celery", "tomatoes", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "celery__watermelon", "celery", "watermelon", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "chard__kohlrabi", "chard", "kohlrabi", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "chard__leek", "chard", "leek", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "chard__legumes", "chard", "legumes", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "chard__lettuce", "chard", "lettuce", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "chard__onion", "chard", "onion", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "chard__radish", "chard", "radish", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "corn_maize__cucurbits", "corn_maize", "cucurbits", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "corn_maize__legumes", "corn_maize", "legumes", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "corn_maize__lettuce", "corn_maize", "lettuce", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "corn_maize__mustard", "corn_maize", "mustard", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "corn_maize__nightshades", "corn_maize", "nightshades", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "corn_maize__peas", "corn_maize", "peas", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "corn_maize__potato", "corn_maize", "potato", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "corn_maize__pumpkin", "corn_maize", "pumpkin", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "corn_maize__soybean", "corn_maize", "soybean", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "corn_maize__squash", "corn_maize", "squash", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "corn_maize__tomatoes", "corn_maize", "tomatoes", "M",
        "Mista / fonti discordanti", "GitHub/Wikipedia + Orto Mio (fonti discordanti)", "Solo informativa; non usare per la disposizione automatica"
    ),
    ConsociationMatrixRelation(
        "cucumber__kohlrabi", "cucumber", "kohlrabi", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cucumber__legumes", "cucumber", "legumes", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cucumber__lettuce", "cucumber", "lettuce", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cucumber__onion", "cucumber", "onion", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cucumber__peas", "cucumber", "peas", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cucumber__potato", "cucumber", "potato", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "cucumber__radish", "cucumber", "radish", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cucumber__spinach", "cucumber", "spinach", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "cucumber__basil", "cucumber", "basil", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "eggplant_or_aubergine__legumes", "eggplant_or_aubergine", "legumes", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "eggplant_or_aubergine__lettuce", "eggplant_or_aubergine", "lettuce", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "eggplant_or_aubergine__peppers", "eggplant_or_aubergine", "peppers", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "eggplant_or_aubergine__radish", "eggplant_or_aubergine", "radish", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "eggplant_or_aubergine__tomatoes", "eggplant_or_aubergine", "tomatoes", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "kohlrabi__lettuce", "kohlrabi", "lettuce", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "kohlrabi__onion", "kohlrabi", "onion", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "leek__onion", "leek", "onion", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "leek__tomatoes", "leek", "tomatoes", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "leek__fennel", "leek", "fennel", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "leek__arugula", "leek", "arugula", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "leek__corn_salad", "leek", "corn_salad", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "legumes__lettuce", "legumes", "lettuce", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "legumes__mustard", "legumes", "mustard", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "legumes__nightshades", "legumes", "nightshades", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "legumes__okra", "legumes", "okra", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "legumes__onion", "legumes", "onion", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "legumes__peas", "legumes", "peas", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "legumes__peppers", "legumes", "peppers", "M",
        "Mista / fonti discordanti", "GitHub/Wikipedia + Orto Mio (fonti discordanti)", "Solo informativa; non usare per la disposizione automatica"
    ),
    ConsociationMatrixRelation(
        "legumes__potato", "legumes", "potato", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "legumes__pumpkin", "legumes", "pumpkin", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "legumes__radish", "legumes", "radish", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "legumes__soybean", "legumes", "soybean", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "legumes__spinach", "legumes", "spinach", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "legumes__squash", "legumes", "squash", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "legumes__tomatoes", "legumes", "tomatoes", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "legumes__fennel", "legumes", "fennel", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "lettuce__okra", "lettuce", "okra", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "lettuce__onion", "lettuce", "onion", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "lettuce__peppers", "lettuce", "peppers", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "lettuce__radish", "lettuce", "radish", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "lettuce__spinach", "lettuce", "spinach", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "lettuce__squash", "lettuce", "squash", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "lettuce__tomatoes", "lettuce", "tomatoes", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "lettuce__artichoke", "lettuce", "artichoke", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "lettuce__cardoon", "lettuce", "cardoon", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "lettuce__chicory", "lettuce", "chicory", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "lettuce__fennel", "lettuce", "fennel", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "lettuce__strawberry", "lettuce", "strawberry", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "lettuce__melon", "lettuce", "melon", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "lettuce__arugula", "lettuce", "arugula", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "lettuce__corn_salad", "lettuce", "corn_salad", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "lettuce__watermelon", "lettuce", "watermelon", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "mustard__peppers", "mustard", "peppers", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "mustard__radish", "mustard", "radish", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "mustard__tomatoes", "mustard", "tomatoes", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "mustard__turnips_and_rutabagas", "mustard", "turnips_and_rutabagas", "M",
        "Mista / fonti discordanti", "GitHub/Wikipedia + Orto Mio (fonti discordanti)", "Solo informativa; non usare per la disposizione automatica"
    ),
    ConsociationMatrixRelation(
        "okra__peppers", "okra", "peppers", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "okra__potato", "okra", "potato", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "okra__squash", "okra", "squash", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "okra__sweet_potato", "okra", "sweet_potato", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "okra__tomatoes", "okra", "tomatoes", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "onion__peas", "onion", "peas", "M",
        "Mista / fonti discordanti", "GitHub/Wikipedia + Orto Mio (fonti discordanti)", "Solo informativa; non usare per la disposizione automatica"
    ),
    ConsociationMatrixRelation(
        "onion__peppers", "onion", "peppers", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "onion__potato", "onion", "potato", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "onion__spinach", "onion", "spinach", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "onion__tomatoes", "onion", "tomatoes", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "onion__endive", "onion", "endive", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "onion__strawberry", "onion", "strawberry", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "onion__watermelon", "onion", "watermelon", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "peas__potato", "peas", "potato", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "peas__radish", "peas", "radish", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "peas__spinach", "peas", "spinach", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "peas__tomatoes", "peas", "tomatoes", "M",
        "Mista / fonti discordanti", "GitHub/Wikipedia + Orto Mio (fonti discordanti)", "Solo informativa; non usare per la disposizione automatica"
    ),
    ConsociationMatrixRelation(
        "peas__turnips_and_rutabagas", "peas", "turnips_and_rutabagas", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "peas__fennel", "peas", "fennel", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "peppers__tomatoes", "peppers", "tomatoes", "M",
        "Mista / fonti discordanti", "GitHub/Wikipedia + Orto Mio (fonti discordanti)", "Solo informativa; non usare per la disposizione automatica"
    ),
    ConsociationMatrixRelation(
        "peppers__fennel", "peppers", "fennel", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "potato__pumpkin", "potato", "pumpkin", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "potato__spinach", "potato", "spinach", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "potato__squash", "potato", "squash", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "potato__tomatoes", "potato", "tomatoes", "C",
        "Da evitare", "GitHub/Wikipedia (Orto Mio riporta soltanto relazioni favorevoli)", "Avviso forte; non divieto assoluto"
    ),
    ConsociationMatrixRelation(
        "pumpkin__radish", "pumpkin", "radish", "B",
        "Favorevole", "Matrice integrata GitHub/Wikipedia e/o Orto Mio", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "pumpkin__fennel", "pumpkin", "fennel", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "radish__spinach", "radish", "spinach", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "radish__squash", "radish", "squash", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "radish__artichoke", "radish", "artichoke", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "radish__cardoon", "radish", "cardoon", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "radish__endive", "radish", "endive", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "radish__parsley", "radish", "parsley", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "spinach__squash", "spinach", "squash", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "spinach__artichoke", "spinach", "artichoke", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "spinach__cardoon", "spinach", "cardoon", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "spinach__fennel", "spinach", "fennel", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "spinach__parsley", "spinach", "parsley", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "spinach__arugula", "spinach", "arugula", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "tomatoes__basil", "tomatoes", "basil", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "tomatoes__chicory", "tomatoes", "chicory", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "basil__fennel", "basil", "fennel", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "artichoke__endive", "artichoke", "endive", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "chicory__fennel", "chicory", "fennel", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "chicory__corn_salad", "chicory", "corn_salad", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "fennel__melon", "fennel", "melon", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
    ConsociationMatrixRelation(
        "strawberry__parsley", "strawberry", "parsley", "B",
        "Favorevole", "Orto Mio; possibile presenza anche nel dataset GitHub/Wikipedia", "Preferenza agronomica; non vincolo rigido"
    ),
)

val GUIDE_TO_CONSOCIATION_ENTITY = mapOf(
    "aglio" to "alliums",
    "anguria" to "watermelon",
    "asparago" to "asparagus",
    "basilico" to "basil",
    "bietola_da_coste" to "chard",
    "bietoline_da_taglio" to "chard",
    "bietola_da_radice" to "chard",
    "carciofo" to "artichoke",
    "cardo" to "cardoon",
    "carota" to "carrots",
    "cavoli" to "cabbage",
    "cavolo_rapa" to "kohlrabi",
    "cece" to "legumes",
    "cetriolo" to "cucumber",
    "cicorie_da_cespo" to "chicory",
    "cipolla" to "onion",
    "cipollotto" to "onion",
    "indivia_riccia_e_scarola" to "endive",
    "fagiolo_e_fagiolino" to "beans_bush",
    "fava" to "beans_fava",
    "finocchio" to "fennel",
    "fragola" to "strawberry",
    "lattuga" to "lettuce",
    "lattughini_da_taglio" to "lettuce",
    "mais_dolce" to "corn_maize",
    "melanzana" to "eggplant_or_aubergine",
    "melone" to "melon",
    "okra" to "okra",
    "patata" to "potato",
    "peperone" to "peppers",
    "piselli" to "peas",
    "pomodoro" to "tomatoes",
    "porro" to "leek",
    "prezzemolo" to "parsley",
    "ravanello" to "radish",
    "rucola" to "arugula",
    "scalogno" to "alliums",
    "sedano" to "celery",
    "spinacio" to "spinach",
    "valeriana" to "corn_salad",
    "zucca" to "pumpkin",
    "zucchino" to "squash",
)

val CONSOCIATION_ENTITY_TO_GUIDE = mapOf(
    "alliums" to "aglio",
    "watermelon" to "anguria",
    "asparagus" to "asparago",
    "basil" to "basilico",
    "chard" to "bietola_da_coste",
    "artichoke" to "carciofo",
    "cardoon" to "cardo",
    "carrots" to "carota",
    "cabbage" to "cavoli",
    "kohlrabi" to "cavolo_rapa",
    "legumes" to "cece",
    "cucumber" to "cetriolo",
    "chicory" to "cicorie_da_cespo",
    "onion" to "cipolla",
    "endive" to "indivia_riccia_e_scarola",
    "beans_bush" to "fagiolo_e_fagiolino",
    "beans_fava" to "fava",
    "fennel" to "finocchio",
    "strawberry" to "fragola",
    "lettuce" to "lattuga",
    "corn_maize" to "mais_dolce",
    "eggplant_or_aubergine" to "melanzana",
    "melon" to "melone",
    "okra" to "okra",
    "potato" to "patata",
    "peppers" to "peperone",
    "peas" to "piselli",
    "tomatoes" to "pomodoro",
    "leek" to "porro",
    "parsley" to "prezzemolo",
    "radish" to "ravanello",
    "arugula" to "rucola",
    "celery" to "sedano",
    "spinach" to "spinacio",
    "corn_salad" to "valeriana",
    "pumpkin" to "zucca",
    "squash" to "zucchino",
)

fun consociationEntity(id: String): ConsociationEntity? =
    CONSOCIATION_ENTITIES.firstOrNull { it.id == id }

fun consociationRelationsFor(entityId: String): List<ConsociationMatrixRelation> =
    CONSOCIATION_RELATIONS.filter { it.firstEntityId == entityId || it.secondEntityId == entityId }

fun ConsociationMatrixRelation.otherEntityId(entityId: String): String =
    if (firstEntityId == entityId) secondEntityId else firstEntityId
