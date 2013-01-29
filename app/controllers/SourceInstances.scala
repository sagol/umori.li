package controllers

import java.util.Random
import scala.Predef._
import collection.mutable
import org.jsoup.{UnsupportedMimeTypeException, HttpStatusException, Jsoup}
import java.net.{SocketTimeoutException, MalformedURLException, URL}
import org.jsoup.parser.Parser
import java.io.IOException
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

/**
 * Created with IntelliJ IDEA.
 * User: sagol
 * Date: 31.12.12
 * Time: 13:10
 * object
 */
object SourceInstances {

  val instance_bash = new SourceInstance(SiteReader.bashimJokes)
  val instance_ithappens = new SourceInstance(SiteReader.ithappensJokes)
  val instance_zadolbali = new SourceInstance(SiteReader.zadolbaliJokes)
  val instance_shortiki = new SourceInstance(SiteReader.shortikiJokes)
  val instance_anekdot = new SourceInstance(SiteReader.anekdotJokes)

  val instances = List (instance_bash, instance_ithappens, instance_zadolbali, instance_shortiki, instance_anekdot)

  var lastUpdateTime = System.currentTimeMillis()

  private def init(): Map[SourceInstance, Boolean] = {
    var map:Map[SourceInstance, Boolean] = Map()
    for (i <- this.instances) map += (i -> i.prepareInstance())
    map
  }
  var instanceInitMap = init()

  def urlContent(url: String): mutable.LinkedHashSet[UmorElement] = {

    def findInstanse(url: String, i: Int): SourceInstance ={
      if (i >= instances.length) null
      else if (url.contains(instances(i).sites.head.site)) instances(i)
        else findInstanse(url, i + 1)
    }

    def findSite(url: String, sites: List[Site], i: Int): Site ={
      if (i >= sites.length) null
      else if (url.contains(sites(i).site)) sites(i)
        else findSite(url, sites, i + 1)
    }

    def getConnection (url: String) = {
      Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true).timeout(60000)
    }

    def getDocument (url: String) = {
      try {
         getConnection(url).get()
      } catch {
        case _:IOException | _:MalformedURLException | _:HttpStatusException |
             _:UnsupportedMimeTypeException | _:SocketTimeoutException |
             _:java.net.UnknownHostException => Document.createShell("")
      }
    }

    val instance = findInstanse(url, 0)
    val site = if (url != null && instance != null) findSite(url, instance.sites,0) else null

    def content:mutable.LinkedHashSet[UmorElement] = {
      val document = getDocument(url)
      var elemsin = document.select(site.parsel)
      if (elemsin.isEmpty) {               //"font-size: 16px; font-family: monospace, Courier New; max-width: 730px;"
        elemsin = document.select("div")
        for (i <- 0 to elemsin.size() - 1) {
          if (!elemsin.get(i).attr("style").contains("font-size: 16px; font-family: monospace, Courier New; max-width: 730px;"))
            elemsin.get(i).empty().removeAttr("style")
        }
        elemsin = elemsin.select("div[style]")
      }
      if (elemsin.size() > 0) {
        val i = elemsin.iterator()
        var elemsout:mutable.LinkedHashSet[UmorElement] = mutable.LinkedHashSet()
        while(i.hasNext) {
          val u = new UmorElement(site)
          u.link_= (url)
          u.element_= (i.next())
          elemsout += u
        }
        elemsout
      } else mutable.LinkedHashSet()
    }

    if (site == null) mutable.LinkedHashSet()
    else content
  }

  private def randomMix(num: Int = 0): mutable.LinkedHashSet[UmorElement] = {
    var tpl = List[(Int, Int, Int)]()
    val rnd = new Random()
    var len = num

    if (len == 0) {
      for (i <- 0 to 4) {
        if (instances(i).instance != null) {
          val ysz = instances(i).instance.size - 1
          for (y <- 0 to ysz) {
            if (!instances(i).instance(y).site.name.contains("abyss"))
              len += instances(i).instance(y).content.size
          }
        }
      }
    }

    for (i <- 0 to len) {
      val j = rnd.nextInt(5)
      try {
        if (instances(j).instance.size > 0) {
          var k = rnd.nextInt(instances(j).instance.size)
          while (instances(j).instance(k).site.name.contains("abyss"))
            k = rnd.nextInt(instances(j).instance.size)
          val l = instances(j).instance(k).content.size - 1
          if (l > 1)
            tpl ::= (j, k, rnd.nextInt(l) + 1)
        }
      } catch {
        case e:IllegalArgumentException =>
      }
    }
    var elems:mutable.LinkedHashSet[UmorElement] = mutable.LinkedHashSet()
    val s = tpl.toSet
    for (i <- s) {
      val inst = instances(i._1).instance(i._2)
      val cont = inst.content.toArray
      val el = cont.apply(i._3)
      elems += el
    }
    elems
  }

  var random = randomMix ()

  private def update(force: Boolean = false): Map[SourceInstance, Boolean] = {
    var map:Map[SourceInstance, Boolean] = Map()
    if (force || (System.currentTimeMillis() - lastUpdateTime > 60000)) {
      System.gc()
      for (i <- this.instances) map += (i -> i.update())
      this.random = randomMix()
      this.lastUpdateTime = System.currentTimeMillis()
    }
    map
  }
  var instanceUpdateMap = update()


  def updateAll() {
    this.instanceUpdateMap = update()
  }

}
