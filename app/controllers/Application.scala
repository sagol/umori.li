package controllers

import play.api._
import play.api.mvc._
import org.apache.commons.lang3.StringEscapeUtils

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
      Ok(views.html.bash("Микс", SourceInstances.random))
    }
    else Ok(views.html.error("Ошибка", "Неизвестная ошибка"))
  }

  def agree = Action {
    Ok(views.html.agree("Пользовательское соглашение"))
  }

  def url(url: Option[String])= Action {
    val content = SourceInstances.urlContent(url.getOrElse(""))
    val meta = "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\n\t\t" +
               "<meta name=\"title\" content=\"" +
                  StringEscapeUtils.escapeHtml4(content.head.element.text().take(20)) + "\" />\n\t\t" +
               "<meta name=\"description\" content=\"" +
                  StringEscapeUtils.escapeHtml4(content.head.element.text().take(120)) + "\" />\n\t\t" +
               "<link rel=\"image_src\" href=\"http://www.umori.li/assets/images/big_smile72.png\" />"
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

}


