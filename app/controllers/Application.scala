package controllers

import play.api._
import play.api.mvc._
import java.io.File
import scala.io.Source

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def ithappens = Action {
    val document = new ContentExtractor("http://ithappens.ru")
    val str = document.html.select("p[id]")
//    val rssUrl = document.select("link[rel=alternate][type=application/rss+xml]").attr("href")

    Ok(views.html.bash("ithappens.ru", document.Format(str, 0).toString))
  }

  def bash = Action {
    val document = new ContentExtractor("http://bash.im")
    val str = document.html.getElementsByClass("text")
    Ok(views.html.bash("bash.im", document.Format(str, 0).toString))
  }

  def zadolbali = Action {
    val document = new ContentExtractor("http://zadolba.li")
    val str = document.html.getElementsByClass("the")
    Ok(views.html.bash("zadolba.li", document.Format(str, 0).toString))
  }

  def shortiki = Action {
    val document = new ContentExtractor("http://shortiki.com")
    val str = document.html.getElementsByClass("shortik")
    Ok(views.html.bash("shortiki.com", document.Format(str, 0).toString))
  }

  def anekdot = Action {
    val document = new ContentExtractor("http://www.anekdot.ru/last/j.html")
    val str = document.html.getElementsByClass("text")
    Ok(views.html.bash("anekdot.ru", document.Format(str, 0).toString))
  }

}


