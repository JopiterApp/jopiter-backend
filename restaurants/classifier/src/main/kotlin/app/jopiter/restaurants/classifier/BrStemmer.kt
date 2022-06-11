package app.jopiter.restaurants.classifier

import app.jopiter.restaurants.classifier.BrStemmer.stem
import org.apache.lucene.analysis.br.BrazilianStemmer

/**
 * This object only exists because [stem] is protected in
 * [BrazilianStemmer], but we need to use it.
 */
object BrStemmer : BrazilianStemmer() {
  public override fun stem(term: String): String = super.stem(term).orEmpty()
}
