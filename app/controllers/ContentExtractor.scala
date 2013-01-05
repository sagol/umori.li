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

  def getContent: Elements = {
    if (System.currentTimeMillis() - lastUpdateTime < 60000) cache
    else {
      lastUpdateTime = System.currentTimeMillis()
      val link = getLink
      if (link == null) cache
      else {
        val document = getDocument(link)
        val elems = if (isRSSlink) {
          document.select("item").select("description")
        }
        else document.select(site.parsel)
        cache = elems.tagName("div").addClass("well")
        cache
      }
    }
  }
}

/*
class ContentExtractor2 (val site: Site) {

  val connection = Jsoup.connect(site.url).ignoreContentType(true).ignoreHttpErrors(true).timeout(60000)

  var html:Document = {
    if (connection != null)
      try {
        connection.get()
      }
      catch {
        case _:IOException | _:MalformedURLException | _:HttpStatusException |
             _:UnsupportedMimeTypeException | _:SocketTimeoutException |
             _:java.net.UnknownHostException => Document.createShell("")
      }
    else
      Document.createShell("")}
  var content = html.select(site.parsel)
  var rss_links = html.select("link[type=application/rss+xml]")
  var correctrss = false
/*
  def getRSSConnection = {
    try {
      if (rss_links.size() > 0 && rss_links.get(0).attr("href").contains(site.url)) {
        Jsoup.connect(rss_links.get(0).attr("href")).ignoreContentType(true).ignoreHttpErrors(true).
          timeout(60000)
      }
      else null
    } catch {
        case _: IllegalArgumentException | _: NullPointerException | _: UnknownHostException => null
    }

  }

  var rssconnection = getRSSConnection

  var rss:Document = {
    if (rssconnection != null)
      try {
        rssconnection.get()
      }
      catch {
        case _:IOException | _:MalformedURLException | _:HttpStatusException |
             _:UnsupportedMimeTypeException | _:SocketTimeoutException |
             _:java.net.UnknownHostException => Document.createShell("")
      }
    else
      Document.createShell("")}

  */

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
    if (rss_links.size() > 0 && rss_links.get(0).attr("href").contains(site.url)) {
//    if (rssconnection != null) {
      try {
/*        val url = new URL(rss.get(0).attr("href"))
        val stream = url.openStream()
        val rssParser = Jsoup.parse(stream, site.encoding, rss.get(0).attr("href"), Parser.xmlParser())
*/
        val rssconnection = Jsoup.connect(rss_links.get(0).attr("href")).ignoreContentType(true).ignoreHttpErrors(true).
          timeout(60000)
        val rss = rssconnection.get()
        val rssParser = Jsoup.parse(rss.html(), rss_links.get(0).attr("href"), Parser.xmlParser())
//        stream.close()
        addLinks(rssParser.select("item"))
      } catch {
        case _: IllegalArgumentException | _: NullPointerException |
             _: ArrayIndexOutOfBoundsException | _: UnknownHostException => getContent()
      }
    }
    else getContent()
  }
/*
  def update () {
    if (correctrss == false) {
      html = {
        if (connection != null)
          try {
            connection.get()
          }
          catch {
            case _:IOException | _:MalformedURLException | _:HttpStatusException |
                 _:UnsupportedMimeTypeException | _:SocketTimeoutException |
                 _:java.net.UnknownHostException => Document.createShell("")
          }
        else
          Document.createShell("")}
      content = html.select(site.parsel)
    }
    else
    {
      rss = {
      if (rssconnection != null)
        try {
          rssconnection.get()
        }
        catch {
          case _:IOException | _:MalformedURLException | _:HttpStatusException |
               _:UnsupportedMimeTypeException | _:SocketTimeoutException |
               _:java.net.UnknownHostException => Document.createShell("")
        }
      else
        Document.createShell("")}
    }
  }
*/
  def update () {
    html = connection.get()
    content = html.select(site.parsel)
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
  */