package controllers

import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import org.apache.commons.lang3.StringEscapeUtils
import collection.mutable


/**
 * User: sagol
 * Date: 26.12.12
 * Time: 22:09
 * Класс с источниками данных
 */
class SourceInstance (val sites: List[Site]) {

  type Instance = List[ContentExtractor]

  var instance:Instance = List()

  val whitelist = Whitelist.simpleText().addTags("br").addTags("div", "p").
    addAttributes("div", "class").addAttributes("p", "class").addAttributes("div", "site")

  def prepareInstance (): Boolean = {
    if (!sites.isEmpty) {
      for (site <- sites) {
        try {
          instance ::= (new ContentExtractor(site))
        } catch {
          case e: ExceptionInInitializerError => null
        }
      }
      true
    }
    else false
  }

  def update (): Boolean = {
    if (!instance.isEmpty) {
      for (inst <- instance) {
        if (inst != null) inst.update()
      }
    true
    }
    else false
  }

  def contentByUrl (url: String):mutable.LinkedHashSet[UmorElement] = {
    def get (url: String, list: Instance): mutable.LinkedHashSet[UmorElement] = {
      if (list.isEmpty) mutable.LinkedHashSet()
      else if (list.head.site.url != url) get(url, list.tail)
      else list.head.content
    }
    if (!url.isEmpty)
      get (url, instance)
    else mutable.LinkedHashSet()
  }

  def contentAsStringByUrl (url: String): String = {
    def get (url: String, list: Instance): String = {
      if (list.isEmpty) ""
      else if (list.head.site.url != url) get(url, list.tail)
        else Jsoup.clean(StringEscapeUtils.unescapeHtml4(list.head.contentHtml), whitelist)
    }
    if (!url.isEmpty)
      get (url, instance)
    else ""
  }

  def contentByName (name: String): mutable.LinkedHashSet[UmorElement] = {
    def get (name: String, list: Instance, acc: mutable.LinkedHashSet[UmorElement]): mutable.LinkedHashSet[UmorElement] = {
      if (list.isEmpty) acc
      else if (list.head.site.name != name) get(name, list.tail, acc)
      else get(name, list.tail, list.head.content ++ acc)
    }
    if (!name.isEmpty)
      get (name, instance, mutable.LinkedHashSet())
    else mutable.LinkedHashSet()
  }

  def contentAsStringByName (name: String): String = {
    def get (name: String, list: Instance, acc: String): String = {
      if (list.isEmpty) {
        Jsoup.clean(StringEscapeUtils.unescapeHtml4(acc), whitelist)
      }
      else if (list.head.site.name != name) get(name, list.tail, acc)
        else get(name, list.tail, list.head.contentHtml + acc)
    }
    if (!name.isEmpty)
      get (name, instance, "")
    else ""
  }

  def contentBySite (site: String): mutable.LinkedHashSet[UmorElement] = {
    def get (site: String, list: Instance, acc: mutable.LinkedHashSet[UmorElement]): mutable.LinkedHashSet[UmorElement] = {
      if (list.isEmpty) acc
      else if (list.head.site.site != site) get(site, list.tail, acc)
      else get(site, list.tail, list.head.content ++ acc)
    }
    if (!site.isEmpty)
      get (site, instance, mutable.LinkedHashSet())
    else mutable.LinkedHashSet()
  }

  def contentAsStringBySite (site: String): String = {
    def get (site: String, list: Instance, acc: String): String = {
      if (list.isEmpty) Jsoup.clean(StringEscapeUtils.unescapeHtml4(acc), whitelist)
      else if (list.head.site.site != site) get(site, list.tail, acc)
        else get(site, list.tail, list.head.contentHtml + acc)
    }
    if (!site.isEmpty)
      get (site, instance, "")
    else ""
  }
}
