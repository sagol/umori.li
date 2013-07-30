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
import play.api.libs.json

case class UmorElement(site: Site, var link: String, var elementPureHtml: String) {

  private var _link:String = null
  //  link = _link

  /*  def link_=(link: String) {
      if (link != "") {
        if (link.contains(site.site + site.linkpar))
          _link = controllers.routes.Application.url(Option(link)).toString()
        else
          _link = controllers.routes.Application.url(Option("http://" + site.site + site.linkpar + link)).toString()
      }
      else this.link = null
    }
    */
  def getLink : String = _link
  def setLink (link: String) {
    if (link != "") {
      if (link.contains(site.site + site.linkpar))
        _link = controllers.routes.Application.url(Option(link)).toString()
      else
        _link = controllers.routes.Application.url(Option("http://" + site.site + site.linkpar + link)).toString()
      this.link = _link
    }
    else this.link = null
  }

  private var _element:Element = _
  def element = _element
  private var _elementHtml:String = _
  private var _elementPureHtml:String = _
  private var _elementText:String = _
  var elementHtml = _elementHtml
  def elementText = _elementText
  def element_=(element: Element) {
    _element = element
    _elementHtml = Jsoup.clean(StringEscapeUtils.unescapeHtml4(_element.toString), Whitelist.simpleText().addTags("br").addTags("div", "p").
      addAttributes("div", "class").addAttributes("p", "class").addAttributes("div", "site"))
    _elementPureHtml = Jsoup.clean(StringEscapeUtils.unescapeHtml4(_element.toString), Whitelist.basic())
    _elementText = Jsoup.clean(_element.text(), Whitelist.none())
    this.elementHtml = _elementHtml
    this.elementPureHtml = _elementPureHtml
  }

}
import play.api.libs.json._

object UmorElement {

  implicit object UmorElementFormat extends Format[UmorElement] {

    def reads(json: JsValue) = JsSuccess(UmorElement(
      (json \ "Site").as[Site],
      (json \ "link").as[String],
      (json \ "elementPureHtml").as[String]
    ))

    def writes(json: UmorElement) = JsObject(Seq(
      "site" -> JsString(json.site.site),
      "name" -> JsString(json.site.name),
      "desc" -> JsString(json.site.desc),
      "link" -> JsString(json.link),
      "elementPureHtml" -> JsString(json.elementPureHtml)
    ))
  }
}