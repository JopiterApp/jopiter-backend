package app.jopiter.restaurants.classifier

import kotlin.streams.toList

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class StopwordsProvider {

  val portugueseStopwordsCsv: String by lazy {
    portugueseStopwordsFile().bufferedReader().lines().toList().joinToString(",")
  }
}

private fun portugueseStopwordsFile() = StopwordsProvider::class.java.classLoader.getResourceAsStream("portuguese_stopwords.txt")!!
