package it.luca.ilmiorto.data

import java.text.Normalizer

data class CropGuideItem(
    val id: String,
    val name: String,
    val familyCode: String,
    val avoidAfter: List<String>,
    val favorableAfter: List<String>,
    val returnYears: Int,
    val toleratesHalfShade: Boolean?,
    val companions: List<String>,
    val rowSpacingCm: Double,
    val plantSpacingCm: Double,
    val organicQuantity: String,
    val fertilizeInHole: Boolean,
    val fertilizeMonthly: Boolean,
    val reduceBeforeHarvest: Boolean,
    val suspendBeforeHarvest: Boolean,
    val irrigationAfterEstablishment: String,
    val emoji: String,
    val colorHex: String,
    val aliases: List<String> = emptyList(),
)

data class CropHistoryEntry(
    val id: String = java.util.UUID.randomUUID().toString(),
    val year: Int,
    val zoneId: String,
    val speciesId: String,
    val speciesName: String,
)

data class CompanionGuideRelation(
    val item: CropGuideItem?,
    val displayName: String,
    val direct: Boolean,
    val sourceSpeciesName: String,
)

val CROP_GUIDE: List<CropGuideItem> = listOf(
    CropGuideItem(
        id = "aglio", name = "Aglio", familyCode = "LIL",
        avoidAfter = listOf("PATATA", "BIETOLA"), favorableAfter = listOf("COMPOSITE"), returnYears = 4,
        toleratesHalfShade = true, companions = listOf("LATTUGA", "FRAGOLA", "CAROTA"),
        rowSpacingCm = 30.0, plantSpacingCm = 15.0, organicQuantity = "MEDIA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = false, suspendBeforeHarvest = true,
        irrigationAfterEstablishment = "AL TRAPIANTO E POI SETTIMANALMENTE SOLO IN CASO DI SICCITÀ", emoji = "🧄", colorHex = "#d9e8bf", aliases = listOf("aglio"),
    ),
    CropGuideItem(
        id = "anguria", name = "Anguria", familyCode = "CUC",
        avoidAfter = listOf("CUCURBITACEE"), favorableAfter = listOf("LEGUMI", "COMPOSITE"), returnYears = 4,
        toleratesHalfShade = false, companions = listOf("LATTUGA", "CIPOLLA", "SEDANO"),
        rowSpacingCm = 200.0, plantSpacingCm = 100.0, organicQuantity = "ABBONDANTE",
        fertilizeInHole = true, fertilizeMonthly = true,
        reduceBeforeHarvest = false, suspendBeforeHarvest = true,
        irrigationAfterEstablishment = "ABBONDARE A INIZIO ALLEGAGIONE E INGROSSAMENTO DEI FRUTTI", emoji = "🍉", colorHex = "#cde6b4", aliases = listOf("anguria"),
    ),
    CropGuideItem(
        id = "asparago", name = "Asparago", familyCode = "LIL",
        avoidAfter = listOf("PATATA", "CAROTA", "ASPARAGO"), favorableAfter = listOf("MAIS", "FRAGOLA"), returnYears = 7,
        toleratesHalfShade = true, companions = listOf("LATTUGA", "SPINACIO", "FAGIOLINO"),
        rowSpacingCm = 100.0, plantSpacingCm = 30.0, organicQuantity = "ABBONDANTE",
        fertilizeInHole = true, fertilizeMonthly = false,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "SETTIMANALMENTE NEI PERIODI ASCIUTTI", emoji = "🌱", colorHex = "#e4d5a7", aliases = listOf("asparago"),
    ),
    CropGuideItem(
        id = "basilico", name = "Basilico", familyCode = "LAB",
        avoidAfter = listOf("LABIATE"), favorableAfter = listOf("SOLANACEE"), returnYears = 2,
        toleratesHalfShade = true, companions = listOf("POMODORO", "FINOCCHIO", "CETRIOLO"),
        rowSpacingCm = 30.0, plantSpacingCm = 20.0, organicQuantity = "MEDIA",
        fertilizeInHole = false, fertilizeMonthly = true,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "FREQUENTE SENZA BAGNARE LE FOGLIE", emoji = "🌿", colorHex = "#acd89d", aliases = listOf("basilico"),
    ),
    CropGuideItem(
        id = "bietola_da_coste", name = "Bietola da coste", familyCode = "CHE",
        avoidAfter = listOf("SPINACIO", "BIETOLE"), favorableAfter = listOf("CIPOLLA", "FAGIOLO"), returnYears = 3,
        toleratesHalfShade = true, companions = listOf("CIPOLLA", "RAVANELLO", "CAVOLI"),
        rowSpacingCm = 30.0, plantSpacingCm = 25.0, organicQuantity = "MEDIA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "FREQUENTE, SENZA RISTAGNI", emoji = "🥬", colorHex = "#b9dfaa", aliases = listOf("costa", "coste", "bietola da coste"),
    ),
    CropGuideItem(
        id = "bietoline_da_taglio", name = "Bietoline da taglio", familyCode = "CHE",
        avoidAfter = listOf("SPINACIO", "BIETOLE"), favorableAfter = listOf("CIPOLLA", "FAGIOLO"), returnYears = 3,
        toleratesHalfShade = true, companions = listOf("CIPOLLA", "RAVANELLO"),
        rowSpacingCm = 30.0, plantSpacingCm = 10.0, organicQuantity = "MEDIA",
        fertilizeInHole = false, fertilizeMonthly = true,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "FREQUENTE, SENZA RISTAGNI", emoji = "🥬", colorHex = "#c8e7b3", aliases = listOf("bietoline"),
    ),
    CropGuideItem(
        id = "bietola_da_radice", name = "Bietola da radice", familyCode = "CHE",
        avoidAfter = listOf("SPINACIO", "BIETOLE"), favorableAfter = listOf("CIPOLLA", "FAGIOLO"), returnYears = 3,
        toleratesHalfShade = true, companions = listOf("CIPOLLA", "RAVANELLO", "LATTUGA"),
        rowSpacingCm = 30.0, plantSpacingCm = 10.0, organicQuantity = "MEDIA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = true, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "FREQUENTE, SENZA RISTAGNI, RIDURRE PRIMA DELLA RACCOLTA", emoji = "🟣", colorHex = "#d9b4c5", aliases = listOf("barbabietola", "bietola da radice"),
    ),
    CropGuideItem(
        id = "carciofo", name = "Carciofo", familyCode = "COM",
        avoidAfter = listOf("CARDO", "CARCIOFO"), favorableAfter = listOf("SOLANACEE", "BIETOLA"), returnYears = 5,
        toleratesHalfShade = false, companions = listOf("LATTUGA", "SPINACIO", "RAVANELLO"),
        rowSpacingCm = 100.0, plantSpacingCm = 100.0, organicQuantity = "ABBONDANTE",
        fertilizeInHole = true, fertilizeMonthly = false,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "SETTIMANALMENTE, IN FASE DI PRODUZIONE, IN CASO DI SICCITÀ", emoji = "🌿", colorHex = "#bcd7a5", aliases = listOf("carciofo"),
    ),
    CropGuideItem(
        id = "cardo", name = "Cardo", familyCode = "COM",
        avoidAfter = listOf("CARDO", "CARCIOFO"), favorableAfter = listOf("SOLANACEE", "BIETOLA"), returnYears = 4,
        toleratesHalfShade = false, companions = listOf("LATTUGA", "SPINACIO", "RAVANELLO"),
        rowSpacingCm = 100.0, plantSpacingCm = 100.0, organicQuantity = "ABBONDANTE",
        fertilizeInHole = true, fertilizeMonthly = true,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "FREQUENTI EVITANDO I RISTAGNI NEI PERIODI CALDI E SICCITOSI", emoji = "🌿", colorHex = "#d8d1a7", aliases = listOf("cardo"),
    ),
    CropGuideItem(
        id = "carota", name = "Carota", familyCode = "OMB",
        avoidAfter = listOf("OMBRELLIFERE", "BIETOLA"), favorableAfter = listOf("LILIACEE", "PATATA"), returnYears = 3,
        toleratesHalfShade = true, companions = listOf("CIPOLLA", "PORRO", "LATTUGA"),
        rowSpacingCm = 25.0, plantSpacingCm = 5.0, organicQuantity = "MEDIA",
        fertilizeInHole = true, fertilizeMonthly = false,
        reduceBeforeHarvest = true, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "SETTIMANALE IN FASE DI INGROSSAMENTO, SENZA RISTAGNI", emoji = "🥕", colorHex = "#e8ca96", aliases = listOf("carota"),
    ),
    CropGuideItem(
        id = "cavoli", name = "Cavoli", familyCode = "CRU",
        avoidAfter = listOf("SOLANACEE", "OMBRELLIFERE"), favorableAfter = listOf("LATTUGA", "CIPOLLA", "LEGUMI"), returnYears = 2,
        toleratesHalfShade = true, companions = listOf("FINOCCHIO", "SEDANO", "LATTUGA"),
        rowSpacingCm = 50.0, plantSpacingCm = 40.0, organicQuantity = "ABBONDANTE",
        fertilizeInHole = true, fertilizeMonthly = true,
        reduceBeforeHarvest = true, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "IRRIGAZIONI FREQUENTI CON VOLUMI NON ECCESSIVI", emoji = "🥬", colorHex = "#bfd9ad", aliases = listOf("cavolo", "cavoli", "cavolfiore", "broccolo", "verza", "cappuccio"),
    ),
    CropGuideItem(
        id = "cavolo_rapa", name = "Cavolo rapa", familyCode = "CRU",
        avoidAfter = listOf("SOLANACEE", "OMBRELLIFERE"), favorableAfter = listOf("LATTUGA", "CIPOLLA", "LEGUMI"), returnYears = 2,
        toleratesHalfShade = true, companions = listOf("BIETOLA", "CIPOLLA", "LATTUGA"),
        rowSpacingCm = 40.0, plantSpacingCm = 20.0, organicQuantity = "MEDIA",
        fertilizeInHole = true, fertilizeMonthly = false,
        reduceBeforeHarvest = true, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "IRRIGAZIONI FREQUENTI CON VOLUMI NON ECCESSIVI", emoji = "🥬", colorHex = "#cbe1b7", aliases = listOf("cavolo rapa"),
    ),
    CropGuideItem(
        id = "cece", name = "Cece", familyCode = "LEG",
        avoidAfter = listOf("LEGUMINOSE"), favorableAfter = emptyList(), returnYears = 2,
        toleratesHalfShade = false, companions = listOf("CAVOLO", "CETRIOLO", "FINOCCHIO"),
        rowSpacingCm = 50.0, plantSpacingCm = 10.0, organicQuantity = "SCARSA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = false, suspendBeforeHarvest = true,
        irrigationAfterEstablishment = "RESISTENTE ALLA SICCITÀ", emoji = "🫘", colorHex = "#d7dfaa", aliases = listOf("cece"),
    ),
    CropGuideItem(
        id = "cetriolo", name = "Cetriolo", familyCode = "CUC",
        avoidAfter = listOf("CUCURBITACEE"), favorableAfter = listOf("LEGUMI"), returnYears = 3,
        toleratesHalfShade = null, companions = listOf("LATTUGA", "SEDANO", "CIPOLLA"),
        rowSpacingCm = 100.0, plantSpacingCm = 40.0, organicQuantity = "ABBONDANTE",
        fertilizeInHole = true, fertilizeMonthly = true,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "IRRIGAZIONI FREQUENTI CON VOLUMI NON ECCESSIVI", emoji = "🥒", colorHex = "#b7dfa8", aliases = listOf("cetriolo"),
    ),
    CropGuideItem(
        id = "cicorie_da_cespo", name = "Cicorie da cespo", familyCode = "COM",
        avoidAfter = listOf("LATTUGA"), favorableAfter = listOf("PORRO", "PISELLO"), returnYears = 3,
        toleratesHalfShade = true, companions = listOf("FINOCCHIO", "LATTUGA", "POMODORO"),
        rowSpacingCm = 40.0, plantSpacingCm = 30.0, organicQuantity = "MEDIA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = true, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "ATTENDERE SEMPRE L'ASCIUGATURA DEL TERRENO", emoji = "🥬", colorHex = "#c7e0aa", aliases = listOf("cicoria", "cicorie", "radicchio"),
    ),
    CropGuideItem(
        id = "cipolla", name = "Cipolla", familyCode = "LIL",
        avoidAfter = listOf("CAVOLO", "BIETOLA"), favorableAfter = listOf("CETRIOLO", "POMODORO"), returnYears = 3,
        toleratesHalfShade = false, companions = listOf("LATTUGA", "CAROTA", "PISELLO"),
        rowSpacingCm = 30.0, plantSpacingCm = 15.0, organicQuantity = "MEDIA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = false, suspendBeforeHarvest = true,
        irrigationAfterEstablishment = "IN CASO DI SICCITÀ SETTIMANALMENTE IN INGROSSAMENTO", emoji = "🧅", colorHex = "#efd9a9", aliases = listOf("cipolla"),
    ),
    CropGuideItem(
        id = "cipollotto", name = "Cipollotto", familyCode = "LIL",
        avoidAfter = listOf("LILIACEE"), favorableAfter = listOf("PATATA", "FAGIOLO"), returnYears = 3,
        toleratesHalfShade = true, companions = listOf("LATTUGA", "CAROTA", "PISELLO"),
        rowSpacingCm = 30.0, plantSpacingCm = 5.0, organicQuantity = "MEDIA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = true, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "ATTENDERE SEMPRE L'ASCIUGATURA DEL TERRENO", emoji = "🧅", colorHex = "#e6d8a3", aliases = listOf("cipollotto"),
    ),
    CropGuideItem(
        id = "indivia_riccia_e_scarola", name = "Indivia riccia e scarola", familyCode = "COM",
        avoidAfter = listOf("LATTUGA", "CICORIA"), favorableAfter = listOf("RAVANELLO"), returnYears = 3,
        toleratesHalfShade = true, companions = listOf("RAVANELLO", "CARCIOFO", "CIPOLLA"),
        rowSpacingCm = 40.0, plantSpacingCm = 30.0, organicQuantity = "MEDIA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = false, suspendBeforeHarvest = true,
        irrigationAfterEstablishment = "ATTENDERE SEMPRE L'ASCIUGATURA DEL TERRENO", emoji = "🥬", colorHex = "#c9dfa9", aliases = listOf("indivia", "scarola"),
    ),
    CropGuideItem(
        id = "fagiolo_e_fagiolino", name = "Fagiolo e fagiolino", familyCode = "LEG",
        avoidAfter = listOf("CUCURBITACEE", "BIETOLA"), favorableAfter = listOf("BROCCOLI", "CAVOLI"), returnYears = 2,
        toleratesHalfShade = true, companions = listOf("SEDANO", "CAVOLI", "LATTUGA"),
        rowSpacingCm = 50.0, plantSpacingCm = 10.0, organicQuantity = "SCARSA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = true, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "IRRIGAZIONI SCARSE MA FREQUENTI", emoji = "🫘", colorHex = "#d8e8a7", aliases = listOf("fagiolo", "fagiolino", "fagiolini"),
    ),
    CropGuideItem(
        id = "fava", name = "Fava", familyCode = "LEG",
        avoidAfter = listOf("LEGUMINOSE"), favorableAfter = listOf("BROCCOLI", "CAVOLI"), returnYears = 2,
        toleratesHalfShade = false, companions = listOf("PATATA", "LATTUGA", "CAVOLI"),
        rowSpacingCm = 50.0, plantSpacingCm = 10.0, organicQuantity = "SCARSA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "ESIGENZE IDRICHE LIMITATE. IRRIGARE SOLO CON SICCITÀ", emoji = "🫘", colorHex = "#cfdc9b", aliases = listOf("fava"),
    ),
    CropGuideItem(
        id = "finocchio", name = "Finocchio", familyCode = "OMB",
        avoidAfter = listOf("OMBRELLIFERE"), favorableAfter = listOf("LEGUMI"), returnYears = 3,
        toleratesHalfShade = false, companions = listOf("LATTUGA", "CAVOLO", "SPINACIO"),
        rowSpacingCm = 40.0, plantSpacingCm = 25.0, organicQuantity = "ABBONDANTE",
        fertilizeInHole = true, fertilizeMonthly = true,
        reduceBeforeHarvest = true, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "IRRIGAZIONI FREQUENTI EVITANDO I RISTAGNI", emoji = "🌿", colorHex = "#dcebd1", aliases = listOf("finocchio"),
    ),
    CropGuideItem(
        id = "fragola", name = "Fragola", familyCode = "ROS",
        avoidAfter = listOf("ROSACEE"), favorableAfter = listOf("CAVOLO"), returnYears = 3,
        toleratesHalfShade = true, companions = listOf("LATTUGA", "CIPOLLA", "PREZZEMOLO"),
        rowSpacingCm = 100.0, plantSpacingCm = 30.0, organicQuantity = "MEDIA",
        fertilizeInHole = true, fertilizeMonthly = false,
        reduceBeforeHarvest = true, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "IRRIGAZIONI SCARSE MA FREQUENTI", emoji = "🍓", colorHex = "#f0b4b1", aliases = listOf("fragola"),
    ),
    CropGuideItem(
        id = "lattuga", name = "Lattuga", familyCode = "COM",
        avoidAfter = listOf("RADICCHIO", "LATTUGA"), favorableAfter = listOf("SPINACIO", "PORRO"), returnYears = 2,
        toleratesHalfShade = true, companions = listOf("CIPOLLA", "RAVANELLO", "POMODORO"),
        rowSpacingCm = 40.0, plantSpacingCm = 25.0, organicQuantity = "MEDIA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "IRRIGAZIONI FREQUENTI EVITANDO I RISTAGNI", emoji = "🥬", colorHex = "#bde2a6", aliases = listOf("lattuga"),
    ),
    CropGuideItem(
        id = "lattughini_da_taglio", name = "Lattughini da taglio", familyCode = "COM",
        avoidAfter = listOf("COMPOSITE"), favorableAfter = listOf("SOLANACEE", "BIETOLA"), returnYears = 2,
        toleratesHalfShade = true, companions = listOf("SEDANO", "SPINACIO", "FINOCCHIO"),
        rowSpacingCm = 30.0, plantSpacingCm = 5.0, organicQuantity = "MEDIA",
        fertilizeInHole = false, fertilizeMonthly = true,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "IRRIGAZIONI FREQUENTI EVITANDO I RISTAGNI", emoji = "🥬", colorHex = "#c9e8b6", aliases = listOf("lattughini"),
    ),
    CropGuideItem(
        id = "mais_dolce", name = "Mais dolce", familyCode = "GRA",
        avoidAfter = listOf("GRAMINACEE"), favorableAfter = listOf("LEGUMI"), returnYears = 3,
        toleratesHalfShade = false, companions = listOf("ZUCCA", "POMODORO", "LATTUGA"),
        rowSpacingCm = 30.0, plantSpacingCm = 50.0, organicQuantity = "ABBONDANTE",
        fertilizeInHole = true, fertilizeMonthly = true,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "ALLA FORMAZIONE E INGROSSAMENTO DELLA PANNOCCHIA", emoji = "🌽", colorHex = "#e7d99c", aliases = listOf("mais dolce"),
    ),
    CropGuideItem(
        id = "melanzana", name = "Melanzana", familyCode = "SOL",
        avoidAfter = listOf("SOLANACEE", "CUCURBITACEE"), favorableAfter = listOf("CIC. LATT."), returnYears = 4,
        toleratesHalfShade = false, companions = listOf("LATTUGA", "FAGIOLINO", "CAVOLO"),
        rowSpacingCm = 100.0, plantSpacingCm = 40.0, organicQuantity = "ABBONDANTE",
        fertilizeInHole = true, fertilizeMonthly = true,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "TEME CARENZA E RISTAGNI. INNESTATA È MENO DELICATA", emoji = "🍆", colorHex = "#d6b6e8", aliases = listOf("melanzana"),
    ),
    CropGuideItem(
        id = "melone", name = "Melone", familyCode = "CUC",
        avoidAfter = listOf("CUCURBITACEE"), favorableAfter = listOf("LEGUMI"), returnYears = 4,
        toleratesHalfShade = false, companions = listOf("LATTUGA", "CAVOLO", "FINOCCHIO"),
        rowSpacingCm = 150.0, plantSpacingCm = 80.0, organicQuantity = "ABBONDANTE",
        fertilizeInHole = true, fertilizeMonthly = true,
        reduceBeforeHarvest = false, suspendBeforeHarvest = true,
        irrigationAfterEstablishment = "ABBONDARE A INIZIO ALLEGAGIONE E FINE INGROSSAMENTO", emoji = "🍈", colorHex = "#e6c5a3", aliases = listOf("melone"),
    ),
    CropGuideItem(
        id = "okra", name = "Okra", familyCode = "MAL",
        avoidAfter = listOf("MALVACEE"), favorableAfter = listOf("LEGUMI"), returnYears = 3,
        toleratesHalfShade = false, companions = listOf("LATTUGA", "PEPERONE", "SEDANO"),
        rowSpacingCm = 100.0, plantSpacingCm = 100.0, organicQuantity = "MEDIA",
        fertilizeInHole = false, fertilizeMonthly = true,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "NEI PERIODI ARIDI, IRRIGAZIONI SCARSE MA FREQUENTI", emoji = "🌱", colorHex = "#c8dfa9", aliases = listOf("okra"),
    ),
    CropGuideItem(
        id = "patata", name = "Patata", familyCode = "SOL",
        avoidAfter = listOf("SOLANACEE"), favorableAfter = listOf("LEGUMI", "CAVOLI"), returnYears = 4,
        toleratesHalfShade = true, companions = listOf("FAVA", "SPINACIO", "FAGIOLO", "MAIS"),
        rowSpacingCm = 80.0, plantSpacingCm = 30.0, organicQuantity = "ABBONDANTE",
        fertilizeInHole = true, fertilizeMonthly = true,
        reduceBeforeHarvest = false, suspendBeforeHarvest = true,
        irrigationAfterEstablishment = "EVITARE CARENZE PRIMA E DURANTE LA FIORITURA", emoji = "🥔", colorHex = "#ddc4a1", aliases = listOf("patata"),
    ),
    CropGuideItem(
        id = "peperone", name = "Peperone", familyCode = "SOL",
        avoidAfter = listOf("SOLANACEE"), favorableAfter = listOf("CIC. LATT."), returnYears = 4,
        toleratesHalfShade = true, companions = listOf("LATTUGA", "CAVOLO", "FINOCCHIO"),
        rowSpacingCm = 100.0, plantSpacingCm = 40.0, organicQuantity = "ABBONDANTE",
        fertilizeInHole = true, fertilizeMonthly = true,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "TEME CARENZA E RISTAGNI. CONSIGLIATA BAULATURA", emoji = "🫑", colorHex = "#cbe8a9", aliases = listOf("peperone"),
    ),
    CropGuideItem(
        id = "piselli", name = "Piselli", familyCode = "LEG",
        avoidAfter = listOf("FAGIOLO"), favorableAfter = listOf("CICORIA", "CAVOLI"), returnYears = 2,
        toleratesHalfShade = true, companions = listOf("CAVOLO", "CETRIOLO", "FINOCCHIO"),
        rowSpacingCm = 50.0, plantSpacingCm = 10.0, organicQuantity = "SCARSA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "ESIGENZE IDRICHE LIMITATE. IRRIGARE CON SICCITÀ", emoji = "🫛", colorHex = "#c7dda6", aliases = listOf("pisello", "piselli"),
    ),
    CropGuideItem(
        id = "pomodoro", name = "Pomodoro", familyCode = "SOL",
        avoidAfter = listOf("CUCURBITACEE", "ASPARAGO"), favorableAfter = listOf("CIC. LATT."), returnYears = 3,
        toleratesHalfShade = false, companions = listOf("LATTUGA", "SEDANO", "PISELLO"),
        rowSpacingCm = 100.0, plantSpacingCm = 40.0, organicQuantity = "ABBONDANTE",
        fertilizeInHole = true, fertilizeMonthly = true,
        reduceBeforeHarvest = true, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "ABBONDARE NEI PERIODI SICCITOSI", emoji = "🍅", colorHex = "#f6b1aa", aliases = listOf("pomodoro"),
    ),
    CropGuideItem(
        id = "porro", name = "Porro", familyCode = "LIL",
        avoidAfter = listOf("LILIACEE"), favorableAfter = listOf("CAVOLO", "CETRIOLO", "POMODORO"), returnYears = 3,
        toleratesHalfShade = true, companions = listOf("CAVOLO", "SEDANO", "FINOCCHIO"),
        rowSpacingCm = 30.0, plantSpacingCm = 15.0, organicQuantity = "ABBONDANTE",
        fertilizeInHole = true, fertilizeMonthly = true,
        reduceBeforeHarvest = true, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "IRRIGAZIONI FREQUENTI EVITANDO I RISTAGNI", emoji = "🧅", colorHex = "#d9e9c5", aliases = listOf("porro"),
    ),
    CropGuideItem(
        id = "prezzemolo", name = "Prezzemolo", familyCode = "OMB",
        avoidAfter = listOf("OMBRELLIFERE"), favorableAfter = listOf("LATTUGA"), returnYears = 3,
        toleratesHalfShade = true, companions = listOf("RAVANELLO", "SPINACIO", "FRAGOLA"),
        rowSpacingCm = 15.0, plantSpacingCm = 5.0, organicQuantity = "SCARSA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "IRRIGAZIONI SCARSE MA FREQUENTI", emoji = "🌿", colorHex = "#c9d9ad", aliases = listOf("prezzemolo"),
    ),
    CropGuideItem(
        id = "ravanello", name = "Ravanello", familyCode = "CRU",
        avoidAfter = listOf("CRUCIFERE"), favorableAfter = listOf("INSALATE", "POMODORI", "PORRI"), returnYears = 2,
        toleratesHalfShade = true, companions = listOf("PREZZEMOLO", "SPINACIO", "LATTUGA"),
        rowSpacingCm = 15.0, plantSpacingCm = 5.0, organicQuantity = "SCARSA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = true, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "IRRIGAZIONI SCARSE MA FREQUENTI", emoji = "🔴", colorHex = "#ebb2b2", aliases = listOf("ravanello"),
    ),
    CropGuideItem(
        id = "rucola", name = "Rucola", familyCode = "CRU",
        avoidAfter = listOf("CRUCIFERE"), favorableAfter = emptyList(), returnYears = 2,
        toleratesHalfShade = true, companions = listOf("PORRO", "SPINACIO", "LATTUGA"),
        rowSpacingCm = 15.0, plantSpacingCm = 5.0, organicQuantity = "MEDIA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = true, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "IRRIGAZIONI FREQUENTI EVITANDO I RISTAGNI. IRRIGANDO SALTUARIAMENTE SI OTTIENE UN SAPORE PIÙ INTENSO", emoji = "🌿", colorHex = "#b5dca7", aliases = listOf("rucola"),
    ),
    CropGuideItem(
        id = "scalogno", name = "Scalogno", familyCode = "LIL",
        avoidAfter = listOf("CAVOLO", "BIETOLA"), favorableAfter = listOf("CETRIOLO", "POMODORO"), returnYears = 3,
        toleratesHalfShade = true, companions = listOf("LATTUGA", "CAROTA", "PISELLO"),
        rowSpacingCm = 30.0, plantSpacingCm = 15.0, organicQuantity = "MEDIA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = false, suspendBeforeHarvest = true,
        irrigationAfterEstablishment = "SETTIMANALMENTE SOLO IN CASO DI SICCITÀ", emoji = "🧅", colorHex = "#ead8aa", aliases = listOf("scalogno"),
    ),
    CropGuideItem(
        id = "sedano", name = "Sedano", familyCode = "OMB",
        avoidAfter = listOf("OMBRELLIFERE"), favorableAfter = listOf("LEGUMI"), returnYears = 3,
        toleratesHalfShade = true, companions = listOf("MELANZANA", "POMODORO", "PORRO"),
        rowSpacingCm = 40.0, plantSpacingCm = 25.0, organicQuantity = "ABBONDANTE",
        fertilizeInHole = true, fertilizeMonthly = true,
        reduceBeforeHarvest = true, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "IRRIGAZIONI FREQUENTI EVITANDO I RISTAGNI", emoji = "🌿", colorHex = "#b8d8a5", aliases = listOf("sedano"),
    ),
    CropGuideItem(
        id = "spinacio", name = "Spinacio", familyCode = "CHE",
        avoidAfter = listOf("BIETOLA", "SPINACIO"), favorableAfter = listOf("CAVOLO", "INSALATE", "ZUCCHINE"), returnYears = 2,
        toleratesHalfShade = true, companions = listOf("FAGIOLO", "CIPOLLA", "CARCIOFO"),
        rowSpacingCm = 25.0, plantSpacingCm = 5.0, organicQuantity = "MEDIA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "IRRIGAZIONI FREQUENTI EVITANDO I RISTAGNI", emoji = "🥬", colorHex = "#b8dfb4", aliases = listOf("spinacio"),
    ),
    CropGuideItem(
        id = "valeriana", name = "Valeriana", familyCode = "VAL",
        avoidAfter = listOf("VALERINACEE"), favorableAfter = emptyList(), returnYears = 2,
        toleratesHalfShade = true, companions = listOf("LATTUGA", "CICORIA", "PORRO"),
        rowSpacingCm = 25.0, plantSpacingCm = 5.0, organicQuantity = "MEDIA",
        fertilizeInHole = false, fertilizeMonthly = false,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "IRRIGAZIONI FREQUENTI EVITANDO I RISTAGNI", emoji = "🥬", colorHex = "#c9e2bd", aliases = listOf("valeriana", "valerianella"),
    ),
    CropGuideItem(
        id = "zucca", name = "Zucca", familyCode = "CUC",
        avoidAfter = listOf("ZUCCHINO", "POMODORO"), favorableAfter = listOf("LEGUMI", "LATTUGA"), returnYears = 3,
        toleratesHalfShade = false, companions = listOf("SEDANO", "FINOCCHIO", "CAVOLO"),
        rowSpacingCm = 200.0, plantSpacingCm = 100.0, organicQuantity = "MEDIA",
        fertilizeInHole = true, fertilizeMonthly = true,
        reduceBeforeHarvest = false, suspendBeforeHarvest = true,
        irrigationAfterEstablishment = "ABBONDARE A INIZIO ALLEGAGIONE E INGROSSAMENTO DEI FRUTTI", emoji = "🎃", colorHex = "#f2c08f", aliases = listOf("zucca", "hokkaido", "zucca hokkaido"),
    ),
    CropGuideItem(
        id = "zucchino", name = "Zucchino", familyCode = "CUC",
        avoidAfter = listOf("CUCURBIT.", "POMODORO"), favorableAfter = listOf("LEGUMI", "LATTUGA", "CAVOLI"), returnYears = 3,
        toleratesHalfShade = false, companions = listOf("LATTUGA", "RAVANELLO", "SPINACIO"),
        rowSpacingCm = 120.0, plantSpacingCm = 80.0, organicQuantity = "ABBONDANTE",
        fertilizeInHole = true, fertilizeMonthly = true,
        reduceBeforeHarvest = false, suspendBeforeHarvest = false,
        irrigationAfterEstablishment = "ABBONDARE A GIORNI ALTERNI NEI PERIODI MOLTO CALDI", emoji = "🥒", colorHex = "#b9dfaa", aliases = listOf("zucchino", "zucchini", "zucchina", "zucchine", "trombetta"),
    )
)

const val CROP_GUIDE_SOURCE = "Tabella Consociazioni OrtoMio fornita dall’utente, 2 pagine"
const val CROP_GUIDE_SOURCE_NOTE = "Il documento non riporta una bibliografia dettagliata: le indicazioni sono presentate come fonte pratica, non come prova scientifica definitiva."

private fun normalizeGuide(value: String): String = Normalizer.normalize(value.lowercase(), Normalizer.Form.NFD)
    .replace(Regex("[\u0300-\u036f]"), "")
    .replace(Regex("[^a-z0-9]+"), " ")
    .trim()

fun cropGuideItem(id: String): CropGuideItem? = CROP_GUIDE.firstOrNull { it.id == id }

fun cropGuideForName(name: String): CropGuideItem? {
    val normalized = normalizeGuide(name)
    if (normalized.isBlank()) return null
    return CROP_GUIDE.firstOrNull { guide ->
        normalizeGuide(guide.name) == normalized || guide.aliases.any { alias ->
            normalized.contains(normalizeGuide(alias)) || normalizeGuide(alias).contains(normalized)
        }
    }
}

private fun tokenMatchesGuide(token: String, guide: CropGuideItem): Boolean {
    val normalized = normalizeGuide(token)
    return normalizeGuide(guide.name) == normalized || guide.aliases.any { alias ->
        normalized == normalizeGuide(alias) || normalized.contains(normalizeGuide(alias)) || normalizeGuide(alias).contains(normalized)
    }
}

fun companionRelationsFor(guide: CropGuideItem): List<CompanionGuideRelation> {
    val result = mutableListOf<CompanionGuideRelation>()
    val seen = mutableSetOf<String>()
    guide.companions.forEach { token ->
        val other = cropGuideForName(token)
        val key = other?.id ?: normalizeGuide(token)
        if (seen.add(key)) result += CompanionGuideRelation(other, other?.name ?: token, direct = true, sourceSpeciesName = guide.name)
    }
    CROP_GUIDE.filterNot { it.id == guide.id }.forEach { source ->
        if (source.companions.any { tokenMatchesGuide(it, guide) } && seen.add(source.id)) {
            result += CompanionGuideRelation(source, source.name, direct = false, sourceSpeciesName = source.name)
        }
    }
    return result
}

fun GardenState.withHistory(entry: CropHistoryEntry): GardenState = copy(history = history + entry)
fun GardenState.removeHistory(entryId: String): GardenState = copy(history = history.filterNot { it.id == entryId })
fun GardenState.applyGuideDistances(guideId: String): GardenState {
    val guide = cropGuideItem(guideId) ?: return this
    return copy(crops = crops.map { crop ->
        if (cropGuideForName(crop.name)?.id == guideId) crop.copy(
            plantSpacingCm = guide.plantSpacingCm,
            rowSpacingCm = guide.rowSpacingCm,
        ) else crop
    })
}
