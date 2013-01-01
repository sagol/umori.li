package controllers

import play.api.mvc.Action
import javax.lang.model.util.Elements

/**
 * Created with IntelliJ IDEA.
 * User: sagol
 * Date: 26.12.12
 * Time: 22:09
 * Класс с источниками данных
 */
class SourceInstance (val sites: List[Site]) {

  type Instance = List[ContentExtractor]

  var instance:Instance = List()

  def prepareInstance (): Boolean = {
    if (!sites.isEmpty) {
      for (site <- sites) instance ::= (new ContentExtractor(site))
      true
    }
    else false
  }

  def update (): Boolean = {
    if (!instance.isEmpty) {
      for (instance <- instance) instance.update()
    true
    }
    else false
  }

  def getContentAsStringByUrl (url: String): String = {
    def get (url: String, list: Instance): String = {
      if (list.isEmpty) ""
      else if (list.head.site.url != url) get(url, list.tail)
        else list.head.getContent().toString
    }
    if (!url.isEmpty)
      get (url, instance)
    else ""
  }
  def getContentAsStringByName (name: String): String = {
    def get (name: String, list: Instance, acc: String): String = {
      if (list.isEmpty) acc
      else if (list.head.site.name != name) get(name, list.tail, acc)
        else get(name, list.tail, list.head.getContent().toString + acc)
    }
    if (!name.isEmpty)
      get (name, instance, "")
    else ""
  }
  def getContentAsStringBySite (site: String): String = {
    def get (site: String, list: Instance, acc: String): String = {
      if (list.isEmpty) acc
      else if (list.head.site.site != site) get(site, list.tail, acc)
        else get(site, list.tail, list.head.getContent().toString + acc)
    }
    if (!site.isEmpty)
      get (site, instance, "")
    else ""
  }
}