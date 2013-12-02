package controllers

import org.jsoup.{UnsupportedMimeTypeException, HttpStatusException, Jsoup}
import org.jsoup.nodes.{Entities, Element, Document}
import org.jsoup.select.Elements
import org.jsoup.parser.Parser
import java.net.{SocketTimeoutException, MalformedURLException, UnknownHostException, URL}
import java.io.IOException
import collection.mutable

class ContentExtractor (val site: Site) {

  private def getConnection (url: String) = {
    Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true).timeout(60000)
  }

  private def getDocument (url: String) = {
    try {
      if (isRSSlink){
        val uri = new URL(url)
        val stream = uri.openStream()
        val rssParser = Jsoup.parse(stream, site.encoding, url, Parser.xmlParser())
        stream.close()
        rssParser
      } else getConnection(url).get()
    } catch {
        case _:IOException | _:MalformedURLException | _:HttpStatusException |
             _:UnsupportedMimeTypeException | _:SocketTimeoutException |
             _:java.net.UnknownHostException => Document.createShell("")
      }
  }

  private def getRSSlink = {
    val links = getDocument(site.url).select("link[type=application/rss+xml]")
    if (links.size() > 0) {
      var link = links.get(0).attr("href")
/*      if (links.size() > 1)
        link = links.get(1).attr("href")   // временный костыль для теста немецкого баша
*/
      if (link.contains(site.url)) link else null
    }
    else null
  }

  var isRSSlink = false

  private def getLink = {
    val link = getRSSlink
    if (link == null) site.url
    else {
      isRSSlink = true
      link
    }
  }

  def update () {
  }

  type Uelements = mutable.LinkedHashSet[UmorElement]

  private var _cache:Uelements = _
  def cache = _cache
  def cache_=(cache: Uelements) {
    _cache = cache
  }

  private var lastUpdateTime:Long = 0

  private def createList (elemsin:Elements, links:String, names:String):Uelements = {
    if (elemsin.size() > 0) {
      val i = elemsin.iterator()
      var elemsout:Uelements = mutable.LinkedHashSet()
      while(i.hasNext) {
        val e = i.next()
        val u = new UmorElement(site, isRSSlink, "", "")
        if (links != null) u.setLink(e.select(links).text())
        else u.setLink(e.id().replaceAll("\\D+","")) //оставляем только цифры
        u.element_= (e.select(names).get(0))
        elemsout += u
      }
      elemsout
    } else mutable.LinkedHashSet()
  }

  def content: Uelements = {
    if (System.currentTimeMillis() - lastUpdateTime < 300000) cache
    else {
      lastUpdateTime = System.currentTimeMillis()
      val link = getLink
      if (link == null) cache
      else {
        val document = getDocument(link)
        val elems = if (isRSSlink) {
          createList (document.select("item"), "link", "description")
        }
        else createList(document.select(site.parsel), null, site.parsel)   //<a title="Постоянная ссылка и обсуждения в социальных сетях" class="button_link" href="/id/621695/"
        cache = elems
        cache
      }
    }
  }

   def contentHtml: String = {
    var str:String = ""
     content.foreach(x => str += x.elementHtml)
    str
  }
}
