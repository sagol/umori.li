package controllers

/**
 * Created with IntelliJ IDEA.
 * User: sagol
 * Date: 27.12.12
 * Time: 21:00
 * Объект с набором сайтов для парсинга
 */
object SitesData {
  //<a href="/quote/420939/bayan" class="old" id="vb420939"> "/quote/420939"
  //<p class="text" id="story_10495">  "/story/10495"
  val bashim = """[
    { "site": "bash.im", "name": "bash", "url": "http://bash.im", "parsel": ".quote__body",
     "encoding": "windows-1251", "linkpar": "/quote/", "desc": "Цитатник Рунета"},
     { "site": "bash.im", "name": "abyss", "url": "http://bash.im/abyss", "parsel": ".quote__body",
       "encoding": "windows-1251", "linkpar": "none", "desc": "Цитатник Рунета - Бездна"}
  ]"""
  val ithappens = """[
    { "site": "ithappens.ru", "name": "ithappens", "url": "http://ithappens.ru", "parsel": "p[id]",
     "encoding": "UTF-8", "linkpar": "/story/", "desc": "IT Happens"}
  ]"""
  //<p class="the" id="story_10329"> "/story/10329"
  val zadolbali = """[
    { "site": "zadolba.li", "name": "zadolbali", "url": "http://zadolba.li", "parsel": ".text",
     "encoding": "UTF-8", "linkpar": "/story/", "desc": "Задолба!ли"}
  ]"""
  ////<div class="shell" id="5865"> shortik.php?shortik=5865
  val shortiki = """[
    { "site": "shortiki.com", "name": "shortiki", "url": "http://shortiki.com", "parsel": ".shortik",
     "encoding": "UTF-8", "linkpar": "/shortik.php?shortik=", "desc": "Шортики"}
  ]"""
  //<div class="text" id="txt_id_621997"> "/id/621997/"
  val anekdot = """[
    { "site": "anekdot.ru", "name": "new anekdot", "url": "http://www.anekdot.ru/last/j.html", "parsel": ".text",
     "encoding": "windows-1251", "linkpar": "/id/", "desc": "Свежие анекдоты"},
    { "site": "anekdot.ru", "name": "new story", "url": "http://www.anekdot.ru/last/o.html", "parsel": ".text",
     "encoding": "windows-1251", "linkpar": "/id/", "desc": "Новые истории"},
    { "site": "anekdot.ru", "name": "new aforizm", "url": "http://www.anekdot.ru/last/a.html", "parsel": ".text",
     "encoding": "windows-1251", "linkpar": "/id/", "desc": "Новые афоризмы и фразы"},
    { "site": "anekdot.ru", "name": "new stihi", "url": "http://www.anekdot.ru/last/c.html", "parsel": ".text",
     "encoding": "windows-1251", "linkpar": "/id/", "desc": "Стишки"}
  ]"""
}
