package controllers

import org.jsoup.{UnsupportedMimeTypeException, HttpStatusException, Jsoup}
import org.jsoup.nodes.{Entities, Element, Document}
import org.jsoup.select.Elements
import org.jsoup.parser.Parser
import java.net.{SocketTimeoutException, MalformedURLException, UnknownHostException, URL}
import java.io.IOException

class ContentExtractor (val site: Site) {

  var html:Document = {
      try {
        Jsoup.connect(site.url).ignoreContentType(true).ignoreHttpErrors(true).timeout(60000).get()
      }
      catch {
        case _:IOException | _:MalformedURLException | _:HttpStatusException |
             _:UnsupportedMimeTypeException | _:SocketTimeoutException |
             _:java.net.UnknownHostException => Document.createShell("")
      }
  }

  var content = html.select(site.parsel)
  var rss = html.select("link[type=application/rss+xml]")
  var correctrss = false

  def addLinks (elemsin:Elements):Elements = {
    if (elemsin.size() > 0) correctrss = true
    else correctrss = false
    val i = elemsin.iterator()
    val elemsout = new Elements()
    while(i.hasNext) {
      val e = i.next()
      elemsout.add(e.select("description").get(0).attr("site", e.select("link").text())
        .tagName("div").addClass("well"))
    }
    elemsout
  }

  def getRSSContent():Elements = {
    if (rss.size() > 0 && rss.get(0).attr("href").contains(site.url)) {
      try {
        val url = new URL(rss.get(0).attr("href"))
//        val rssParser = Jsoup.parse(url.openStream(), site.encoding, rss.get(0).text(), Parser.xmlParser())
        val rssParser = Jsoup.parse(Jsoup.connect(rss.get(0).text()).ignoreContentType(true).ignoreHttpErrors(true).
          timeout(60000).get().html(), rss.get(0).text(), Parser.xmlParser())
        addLinks(rssParser.select("item"))
      } catch {
        case _: IllegalArgumentException | _: NullPointerException | _: UnknownHostException => getContent()
      }
    }
    else getContent()
  }

  def update () {
    if (correctrss == false) {
      this.html = Jsoup.connect(site.url).ignoreContentType(true).ignoreHttpErrors(true).timeout(60000).get()
      this.content = html.select(site.parsel)
    }
  }

  def getContent (id: Int = -1, count: Int = 1): Elements = {
    correctrss = false
    var elems = content
    if (id != -1 && id < content.size()) {
      elems = new Elements()
      for (i <- id to id + count)
        elems.add(content.get(i))
    }
    //нада тоже доделать формирование полного адреса ссылки
    elems.attr("site", site.url).tagName("div").addClass("well")
  }
}
