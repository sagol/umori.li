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
    { "site": "bash.im", "name": "bash", "url": "http://bash.im", "parsel": ".text",
     "encoding": "windows-1251" },
    { "site": "bash.im", "name": "abyss", "url": "http://bash.im/abyss", "parsel": ".text",
     "encoding": "windows-1251" }
  ]"""
  val ithappens = """[
    { "site": "ithappens.ru", "name": "ithappens", "url": "http://ithappens.ru", "parsel": "p[id]",
     "encoding": "windows-1251" }
  ]"""
  val zadolbali = """[
    { "site": "zadolba.li", "name": "zadolbali", "url": "http://zadolba.li", "parsel": ".the",
     "encoding": "windows-1251" }
  ]"""
  val shortiki = """[
    { "site": "shortiki.com", "name": "shortiki", "url": "http://shortiki.com", "parsel": ".shortik",
     "encoding": "UTF-8" }
  ]"""
  val anekdot = """[
    { "site": "anekdot.ru", "name": "new anekdot", "url": "http://www.anekdot.ru/last/j.html", "parsel": ".text",
     "encoding": "windows-1251" },
    { "site": "anekdot.ru", "name": "new story", "url": "http://www.anekdot.ru/last/o.html", "parsel": ".text",
     "encoding": "windows-1251" },
    { "site": "anekdot.ru", "name": "new aforizm", "url": "http://www.anekdot.ru/last/a.html", "parsel": ".text",
     "encoding": "windows-1251" },
    { "site": "anekdot.ru", "name": "new stihi", "url": "http://www.anekdot.ru/last/c.html", "parsel": ".text",
     "encoding": "windows-1251" }
  ]"""
}