package controllers

import org.jsoup.{UnsupportedMimeTypeException, HttpStatusException, Jsoup}
import org.jsoup.nodes.{Entities, Element, Document}
import org.jsoup.select.Elements
import org.jsoup.parser.Parser
import java.net.{SocketTimeoutException, MalformedURLException, UnknownHostException, URL}
import java.io.IOException

class ContentExtractor (val site: Site) {

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
    if (rssconnection != null) {
      try {
/*        val url = new URL(rss.get(0).attr("href"))
        val stream = url.openStream()
        val rssParser = Jsoup.parse(stream, site.encoding, rss.get(0).attr("href"), Parser.xmlParser())
*/
        val rssParser = Jsoup.parse(rss.toString, rss_links.get(0).attr("href"), Parser.xmlParser())
//        stream.close()
        addLinks(rssParser.select("item"))
      } catch {
        case _: IllegalArgumentException | _: NullPointerException | _: ArrayIndexOutOfBoundsException => getContent()
      }
    }
    else getContent()
  }

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
