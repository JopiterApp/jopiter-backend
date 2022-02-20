package app.jopiter.restaurants.classifier

import smile.nlp.bag
import smile.nlp.tfidf
import smile.nlp.vectorize

// FIXME This class is only tested as part of ClassifierTests. We should create tests for it
internal class PreProcesser(
  corpus: List<String>,
  private val stopwordsProvider: StopwordsProvider = StopwordsProvider(),
  private val stemmer: BrStemmer = BrStemmer,
) {

  private val bags = corpus.map { it.bagged() }
  private val vocabulary = bags.flatMap { it.keys }.toTypedArray()
  private val vectorizedCorpus = bags.map { it.vectorized() }

  val preparedDocuments = tfidf(vectorizedCorpus).toTypedArray()

  fun preProcess(document: String) = vectorize(vocabulary, document.bagged())

  private fun String.bagged() = bag(stopwordsProvider.portugueseStopwordsCsv, stemmer::stem)

  private fun Map<String, Int>.vectorized() = vectorize(vocabulary, this)
}
