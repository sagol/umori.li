package controllers

import java.util.Random
import org.jsoup.select.Elements
import org.jsoup.Jsoup
import org.apache.commons.lang3.StringEscapeUtils
import org.jsoup.safety.Whitelist

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

  private def getRandomMix(num: Int = 0):String = {
    var tpl = List[(Int, Int, Int)]()
    val rnd = new Random()
    var len = num

    if (len == 0) {
      for (i <- 0 to 4) {
        if (instances(i).instance != null) {
          val ysz = instances(i).instance.size - 1
          for (y <- 0 to ysz) {
           len += instances(i).instance(y).getContent.size()
          }
        }
      }
    }

    for (i <- 0 to len) {
      val j = rnd.nextInt(5)
      try {
        if (instances(j).instance.size > 0) {
          val k = rnd.nextInt(instances(j).instance.size)
          val l = instances(j).instance(k).getContent.size()
          if (l > 0)
            tpl ::= (j, k, rnd.nextInt(l))
        }
      } catch {
        case e:IllegalArgumentException =>
      }
    }

    val whitelist = Whitelist.simpleText().addTags("br").addTags("div", "p").
      addAttributes("div", "class").addAttributes("p", "class").addAttributes("div", "site")

    val elems = new Elements()
    val s = tpl.toSet
    for (i <- s) {
      elems.add(instances(i._1).instance(i._2).getContent.get(i._3))
    }
    Jsoup.clean(StringEscapeUtils.unescapeHtml4(elems.toString), whitelist)
  }

  var random = getRandomMix ()

  private def update(force: Boolean = false): Map[SourceInstance, Boolean] = {
    var map:Map[SourceInstance, Boolean] = Map()
    if (force || (System.currentTimeMillis() - lastUpdateTime > 60000)) {
      System.gc()
      for (i <- this.instances) map += (i -> i.update())
      this.random = getRandomMix()
      this.lastUpdateTime = System.currentTimeMillis()
    }
    map
  }
  var instanceUpdateMap = update()


  def updateAll() {
    this.instanceUpdateMap = update()
  }

}
