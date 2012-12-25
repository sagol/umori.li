package controllers

import org.jsoup.Jsoup
import org.jsoup.nodes.{Element, Document}
import org.jsoup.select.Elements
import java.util

class ContentExtractor (val inputUrl: String) {

  val url = inputUrl

  val connection = Jsoup.connect(this.url)
  val html = connection.get()

  def GetClassSet (name: String): java.util.Set[String] = {
    val set : java.util.Set[String] = new java.util.HashSet[String]()
    set.add("well")
    set
  }

  def Format (elems: Elements, i: Int): Elements = {
    if (i < elems.size()) {
      elems.get(i).classNames(GetClassSet ("well"))
      Format(elems, i + 1)
    }
    elems
  }
}
