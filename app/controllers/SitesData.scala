package controllers

/**
 * Created with IntelliJ IDEA.
 * User: sagol
 * Date: 27.12.12
 * Time: 21:00
 * Объект с набором сайтов для парсинга
 */
object SitesData {
  val bashim = """[
    { "site": "bash.im", "name" : "bash", "url": "http://bash.im", "parsel": ".text" },
    { "site": "bash.im", "name" : "abyss", "url": "http://bash.im/abyss", "parsel": ".text" }
  ]"""
  val ithappens = """[
    { "site": "ithappens.ru", "name" : "ithappens", "url": "http://ithappens.ru", "parsel": "p[id]" }
  ]"""
  val zadolbali = """[
    { "site": "zadolba.li", "name" : "zadolbali", "url": "http://zadolba.li", "parsel": ".the" }
  ]"""
  val shortiki = """[
    { "site": "shortiki.com", "name" : "shortiki", "url": "http://shortiki.com", "parsel": ".shortik" }
  ]"""
  val anekdot = """[
    { "site": "anekdot.ru", "name" : "anekdot", "url": "http://www.anekdot.ru/last/j.html", "parsel": ".text" }
  ]"""
}