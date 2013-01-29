package controllers

/**
 * User: sagol
 * Date: 26.01.13
 * Time: 21:06
 * Кдасс описывающий атомарную единицу сайта - шутку
 */

import org.jsoup.nodes.Element
import org.jsoup.Jsoup
import org.apache.commons.lang3.StringEscapeUtils
import org.jsoup.safety.Whitelist

class UmorElement(val site: Site) {

  val whitelist = Whitelist.simpleText().addTags("br").addTags("div", "p").
    addAttributes("div", "class").addAttributes("p", "class").addAttributes("div", "site")

  private var _link:String = _
  def link = _link
  def link_=(link: String) {
    if (link != "") {
      if (link.contains(site.site + site.linkpar))
        _link = controllers.routes.Application.url(Option(link)).toString()
      else
        _link = controllers.routes.Application.url(Option("http://" + site.site + site.linkpar + link)).toString()
    }
    else _link = null
  }

  private var _element:Element = _
  def element = _element
  private var _elementHtml:String = _
  def elementHtml = _elementHtml
  def element_=(element: Element) {
    _element = element
    _elementHtml = Jsoup.clean(StringEscapeUtils.unescapeHtml4(_element.toString), whitelist)
  }

}
