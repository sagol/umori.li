package controllers

import play.api._
import play.api.mvc._
import java.io.File
import scala.io.Source
import org.jsoup.Jsoup

object Application extends Controller {

  def index = Action {
 //   Ok(views.html.index())
    SourceInstances.updateAll()
    Ok(views.html.bash("Микс", SourceInstances.random))
  }

  def agree = Action {
    Ok(views.html.agree("Пользовательское соглашение"))
  }

  def ithappens = Action {
    SourceInstances.updateAll()
    Ok(views.html.bash("ithappens.ru", SourceInstances.instance_ithappens.getContentAsStringBySite("ithappens.ru")))
  }

  def bash = Action {
    SourceInstances.updateAll()
    Ok(views.html.bash("bash.im", SourceInstances.instance_bash.getContentAsStringBySite("bash.im")))
  }

  def zadolbali = Action {
    SourceInstances.updateAll()
    Ok(views.html.bash("zadolba.li", SourceInstances.instance_zadolbali.getContentAsStringBySite("zadolba.li")))
  }

  def shortiki = Action {
    SourceInstances.updateAll()
    Ok(views.html.bash("shortiki.com", SourceInstances.instance_shortiki.getContentAsStringBySite("shortiki.com")))
  }

  def anekdot = Action {
    SourceInstances.updateAll()
    Ok(views.html.bash("anekdot.ru", SourceInstances.instance_anekdot.getContentAsStringBySite("anekdot.ru")))
  }

}


