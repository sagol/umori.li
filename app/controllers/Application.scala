package controllers

import play.api.mvc._
import play.api.libs.json._

object Application extends Controller {

  def index = Action {
    Ok(views.html.bash("Микс", SourcesData.get("random").slice(0, 50)))
  }

  def agree = Action {
    Ok(views.html.agree("Пользовательское соглашение"))
  }

  def api = Action {
    Ok(views.html.api("REST API"))
  }

  def get(site: Option[String], name: Option[String], num: Option[Int])= Action {
    val content = SourcesData.get(name.getOrElse("bash"))
    if (content != null && content.size > 0) {
       Ok(Json.toJson(content.slice(0, num.getOrElse(50)))).as("application/json; charset=utf-8")
     }
     else Ok(Json.toJson("Can't find source")).as("application/json; charset=utf-8")
  }

  def sources = Action {
    Ok(Json.toJson(SiteReader.sources)).as("application/json; charset=utf-8")
  }

  def random(num: Option[Int]) = Action {
    Ok(Json.toJson(SourcesData.get("random").slice(0, num.getOrElse(50)))).as("application/json; charset=utf-8")
  }

  def url(url: Option[String])= Action {
    val content = SourceInstances.urlContent(url.getOrElse(""))
    var meta:String = ""
    if (content.size > 0) {
      meta = "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\n\t\t" +
        "<meta name=\"title\" content=\"" +
        content.head.elementText.take(30) + "...\" />\n\t\t" +
        "<meta name=\"description\" content=\"" +
        content.head.elementText.take(120) + "\" />\n\t\t" +
        "<link rel=\"image_src\" href=\"http://www.umori.li/assets/images/big_smile72.png\" />\n\t\t" +
        "<meta property=\"og:title\" content=\"" +
        content.head.elementText.take(30) + "...\" />\n\t\t" +
        "<meta property=\"og:description\" content=\"" +
        content.head.elementText.take(120) + "\" />\n\t\t" +
        "<meta property=\"og:image\" href=\"http://www.umori.li/assets/images/big_smile72.png\" />\n\t\t" +
        //               "<meta name=\"twitter:title\" content=\"" +
        //                StringEscapeUtils.escapeHtml4(content.head.element.text().take(65)) + "...\" />\n\t\t" +
        //           "<meta name=\"twitter:description\" content=\"" +
        //            StringEscapeUtils.escapeHtml4(content.head.element.text().take(199)) + "\" />\n\t\t" +
        //       "<meta name=\"twitter:image\" href=\"http://www.umori.li/assets/images/big_smile72.png\" />" +
        "<meta name=\"twitter:card\" content=\"summary\" />"
    }


    Ok(views.html.url("...", content)(meta))
  }

  def ithappens = Action {
    Ok(views.html.bash("ithappens.me", SourcesData.get("ithappens")))
  }

  def bash(name: Option[String])= Action {
    Ok(views.html.bash("bash.im", SourcesData.get(name.getOrElse("bash"))))
  }

  def zadolbali = Action {
    Ok(views.html.bash("zadolba.li", SourcesData.get("zadolbali")))
  }

  def shortiki = Action {
    Ok(views.html.bash("shortiki.com", SourcesData.get("shortiki")))
  }

  def anekdot(name: Option[String]) = Action {
      Ok(views.html.bash("anekdot.ru", SourcesData.get(name.getOrElse("new anekdot"))))
  }

}


