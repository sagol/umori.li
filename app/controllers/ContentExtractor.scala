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
  var rss = html.select("link[type=application/rss+xml]")

  def getRSSContent():Elements = {
    if (rss.size() > 0 && rss.get(0).attr("href").contains(site.url)) {
      try {
        val rssConnection = Jsoup.connect(rss.get(0).attr("href")).ignoreHttpErrors(true).ignoreContentType(true).timeout(60000)
        val rssParser = Jsoup.parse(rssConnection.get().html(), rss.get(0).text(), Parser.xmlParser())
        rssParser.select("item").select("description").tagName("div").addClass("well")
      } catch {
        case _: IllegalArgumentException | _: NullPointerException | _: UnknownHostException => getContent()
      }
    }
    else getContent()
  }

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
    elems.tagName("div").addClass("well")
  }

  def GetClassSet (name: String): java.util.Set[String] = {
    val set : java.util.Set[String] = new java.util.HashSet[String]()
    set.add(name)
    set
  }

}
