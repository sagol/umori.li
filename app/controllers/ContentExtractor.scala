package controllers

import org.jsoup.Jsoup
import org.jsoup.nodes.{Element, Document}
import org.jsoup.select.Elements
import java.util

class ContentExtractor (val site: Site) {

  val connection = Jsoup.connect(site.url)
  var html = connection.get()
  var content = html.select(site.parsel)

  def update () {
    this.html = connection.get()
    this.content = html.select(site.parsel)
  }

  def getContent (id: Int = -1, count: Int = 1): Elements = {
    var elems = content
    if (id != -1 && id < content.size()) {
      elems = new Elements()
      for (i <- id to id + count)
        elems.add(content.get(i))
    }
    format(elems, 0)
  }

  def GetClassSet (name: String): java.util.Set[String] = {
    val set : java.util.Set[String] = new java.util.HashSet[String]()
    set.add(name)
    set
  }

  def format (elems: Elements, i: Int): Elements = {
    if (i < elems.size()) {
      elems.get(i).classNames(GetClassSet ("well"))
      format(elems, i + 1)
    }
    elems
  }
}
