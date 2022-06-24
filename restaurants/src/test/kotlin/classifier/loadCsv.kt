package app.jopiter.restaurants.classifier

private object LoadCSV

fun String.loadCsv() = LoadCSV::class.java.classLoader.getResourceAsStream(this)!!