package controllers

import play.api._
import play.api.mvc._
import java.io.File
import scala.io.Source
import org.jsoup.Jsoup

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
    else Ok(views.html.bash("Ошибка", "Неизвестная ошибка"))
  }

  def agree = Action {
    Ok(views.html.agree("Пользовательское соглашение"))
  }

  def ithappens = Action {
    if (updateAll()) {
      Ok(views.html.bash("ithappens.ru", SourceInstances.instance_ithappens.getContentAsStringBySite("ithappens.ru")))
    }
    else Ok(views.html.bash("Ошибка", "Неизвестная ошибка"))
  }

  def bash(name: Option[String])= Action {
    if (updateAll()) {
      Ok(views.html.bash("bash.im", SourceInstances.instance_bash.getContentAsStringByName(name.getOrElse("bash"))))
    }
    else Ok(views.html.bash("Ошибка", "Неизвестная ошибка"))
  }

  def zadolbali = Action {
    if (updateAll()) {
      Ok(views.html.bash("zadolba.li", SourceInstances.instance_zadolbali.getContentAsStringBySite("zadolba.li")))
    }
    else Ok(views.html.bash("Ошибка", "Неизвестная ошибка"))
  }

  def shortiki = Action {
    if (updateAll()) {
      Ok(views.html.bash("shortiki.com", SourceInstances.instance_shortiki.getContentAsStringBySite("shortiki.com")))
    }
    else Ok(views.html.bash("Ошибка", "Неизвестная ошибка"))
  }

  def anekdot(name: Option[String]) = Action {
    if (updateAll()) {
      Ok(views.html.bash("anekdot.ru", SourceInstances.instance_anekdot.getContentAsStringByName(name.getOrElse("new anekdot"))))
    }
    else Ok(views.html.bash("Ошибка", "Неизвестная ошибка"))
  }

}


