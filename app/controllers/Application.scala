package controllers

import play.api.mvc._
import play.api.libs.json._

object Application extends Controller {

  def updateAll(): Boolean = {
    try {
      SourceInstances.updateAll()
      true
    } catch {
      case _: ExceptionInInitializerError | _:NoClassDefFoundError => false
    }
  }

  def index = Action {
    if (updateAll()) {
      Ok(views.html.bash("Микс", SourceInstances.random.slice(0, 50)))
    }
    else Ok(views.html.error("Ошибка", "Неизвестная ошибка"))
  }

  def agree = Action {
    Ok(views.html.agree("Пользовательское соглашение"))
  }

  def api = Action {
    Ok(views.html.api("REST API"))
  }

  def get(site: Option[String], name: Option[String], num: Option[Int])= Action {
//    if (updateAll()) {
      val instance = SourceInstances.findInstanceByName(site.getOrElse("bash"), 0)
      if (instance != null) {
        val content = instance.contentByName(name.getOrElse("bash"))
        if (content != null && content.size > 0) {
          Ok(Json.toJson(content.slice(0, num.getOrElse(50)))).as("application/json; charset=utf-8")
        }
        else Ok(Json.toJson("Can't find source")).as("application/json; charset=utf-8")
      }
      else Ok(Json.toJson("Can't find source")).as("application/json; charset=utf-8")
//    }
  //  else Ok(Json.toJson("Error updating info")).as("application/json; charset=utf-8")
  }

  def sources = Action {
    Ok(Json.toJson(SiteReader.sources)).as("application/json; charset=utf-8")
  }

  def random(num: Option[Int]) = Action {
    Ok(Json.toJson(SourceInstances.random.slice(0, num.getOrElse(50)))).as("application/json; charset=utf-8")
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
    if (updateAll()) {
      Ok(views.html.bash("ithappens.ru", SourceInstances.instance_ithappens.contentBySite("ithappens.ru")))
    }
    else Ok(views.html.error("Ошибка", "Неизвестная ошибка"))
  }

  def bash(name: Option[String])= Action {
    if (updateAll()) {
      Ok(views.html.bash("bash.im", SourceInstances.instance_bash.contentByName(name.getOrElse("bash"))))
    }
    else Ok(views.html.error("Ошибка", "Неизвестная ошибка"))
  }

  def zadolbali = Action {
    if (updateAll()) {
      Ok(views.html.bash("zadolba.li", SourceInstances.instance_zadolbali.contentBySite("zadolba.li")))
    }
    else Ok(views.html.error("Ошибка", "Неизвестная ошибка"))
  }

  def shortiki = Action {
    if (updateAll()) {
      Ok(views.html.bash("shortiki.com", SourceInstances.instance_shortiki.contentBySite("shortiki.com")))
    }
    else Ok(views.html.error("Ошибка", "Неизвестная ошибка"))
  }

  def anekdot(name: Option[String]) = Action {
    if (updateAll()) {
      Ok(views.html.bash("anekdot.ru", SourceInstances.instance_anekdot.contentByName(name.getOrElse("new anekdot"))))
    }
    else Ok(views.html.error("Ошибка", "Неизвестная ошибка"))
  }

  def bashorg = Action {
    if (updateAll()) {
      Ok(views.html.bash("bash.org", SourceInstances.instance_bashorg.contentByName("bashorg")))
    }
    else Ok(views.html.error("Ошибка", "Неизвестная ошибка"))
  }

}


