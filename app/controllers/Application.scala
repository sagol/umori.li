package controllers

import play.api._
import play.api.mvc._
import java.io.File
import scala.io.Source
import org.jsoup.Jsoup

object Application extends Controller {

  val si = SourceInstances

  def index = Action {
 //   Ok(views.html.index())
    si.updateAll()
    Ok(views.html.bash("Микс", si.random))
  }

  def agree = Action {
    Ok(views.html.agree("Пользовательское соглашение"))
  }

  def ithappens = Action {
    si.updateAll()
    Ok(views.html.bash("ithappens.ru", si.instance_ithappens.getContentAsStringBySite("ithappens.ru")))
  }

  def bash(name: Option[String])= Action {
    si.updateAll()
    Ok(views.html.bash("bash.im", si.instance_bash.getContentAsStringByName(name.getOrElse("bash"))))
  }

  def zadolbali = Action {
    si.updateAll()
    Ok(views.html.bash("zadolba.li", si.instance_zadolbali.getContentAsStringBySite("zadolba.li")))
  }

  def shortiki = Action {
    si.updateAll()
    Ok(views.html.bash("shortiki.com", si.instance_shortiki.getContentAsStringBySite("shortiki.com")))
  }

  def anekdot(name: Option[String]) = Action {
    si.updateAll()
    Ok(views.html.bash("anekdot.ru", si.instance_anekdot.getContentAsStringByName(name.getOrElse("new anekdot"))))
  }

}


