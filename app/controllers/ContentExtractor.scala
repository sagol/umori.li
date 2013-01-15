package controllers

import org.jsoup.{UnsupportedMimeTypeException, HttpStatusException, Jsoup}
import org.jsoup.nodes.{Entities, Element, Document}
import org.jsoup.select.Elements
import org.jsoup.parser.Parser
import java.net.{SocketTimeoutException, MalformedURLException, UnknownHostException, URL}
import java.io.IOException

class ContentExtractor (val site: Site) {

  private def getConnection (url: String) = {
    Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true).timeout(60000)
  }

  private def getDocument (url: String) = {
    try {
//      if (isRSSlink){
      if (false){
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
      val link = links.get(0).attr("href")
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

  private var cache: Elements = new Elements()
  private var lastUpdateTime:Long = 0

  private def addLinks (elemsin:Elements):Elements = {
    if (elemsin.size() > 0) {
      val i = elemsin.iterator()
      val elemsout = new Elements()
      while(i.hasNext) {
        val e = i.next()
        elemsout.add(e.select("description").get(0).attr("site", e.select("link").text()))
      }
      elemsout
    } else elemsin
  }

  def getContent: Elements = {
    if (System.currentTimeMillis() - lastUpdateTime < 60000) cache
    else {
      lastUpdateTime = System.currentTimeMillis()
      val link = getLink
      if (link == null) cache
      else {
        val document = getDocument(link)
        val elems = if (isRSSlink) {
          addLinks (document.select("item"))
        }
        else document.select(site.parsel)
        cache = elems.tagName("div").addClass("well")
        cache
      }
    }
  }
}
