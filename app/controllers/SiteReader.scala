package controllers

/**
 * Created with IntelliJ IDEA.
 * User: sagol
 * Date: 27.12.12
 * Time: 21:15
 * Парсинг сайтов
 */

object SiteReader {

  object ParseSites {
    import scala.util.parsing.json._

    def getList[T](s: String): List[T] =
      JSON.parseFull(s).get.asInstanceOf[List[T]]

    def getMap(s: String): Map[String, Any] =
      JSON.parseFull(s).get.asInstanceOf[Map[String, Any]]

    def getSites(site: String, json: String): List[Site] =
      for (map <- getList[Map[String, Any]](json)) yield {
        val name = map("name")
        val url = map("url")
        val parsel = map("parsel")
        val encoding = map("encoding")
        val linkpar = map("linkpar")
        val desc = map("desc")
        new Site(site, name.toString, url.toString, parsel.toString, encoding.toString, linkpar.toString, desc.toString)
      }

    def getSiteData(site: String, json: String): List[Site] = {
      val l = getList[Map[String, Any]](json)
      for (map <- l) yield {
        val name = map("name")
        val url = map("url")
        val parsel = map("parsel")
        val encoding = map("encoding")
        val linkpar = map("linkpar")
        val desc = map("desc")
        new Site(site, name.toString, url.toString, parsel.toString, encoding.toString, linkpar.toString, desc.toString)
      }
    }
  }

  def toSiteSet(l: List[Site]): SiteSet = {
    l.foldLeft(new Empty: SiteSet)(_.incl(_))
  }

  def unparseToData(sts: List[Site]): String = {
    val buf = new StringBuffer
    for (st <- sts) {
      val json = "{ \"site\": \"" + st.site + "\", \"name\": \"" + st.name + "\", \"url\": \"" +
        st.url + "\", \"parsel\": " + st.parsel + "\", \"encoding\": \"" + st.encoding +
        "\", \"linkpar\": \"" +st.linkpar + "\", \"desc\": \"" + st.desc + "\"  }"
      buf.append(json + ",\n")
    }
    buf.toString
  }

  val sites = List("bash.im", /*"ithappens.me",*/ "zadolba.li", "anekdot.ru", "ideer.ru", "det.org.ru", "xkcdb.org")

  val bashimJokes = SiteReader.ParseSites.getSiteData("bash.im", SitesData.bashim)
/*  val ithappensJokes = SiteReader.ParseSites.getSiteData("ithappens.me", SitesData.ithappens)*/
  val zadolbaliJokes = SiteReader.ParseSites.getSiteData("zadolba.li", SitesData.zadolbali)
  val anekdotJokes = SiteReader.ParseSites.getSiteData("anekdot.ru", SitesData.anekdot)
  val ideerJokes = SiteReader.ParseSites.getSiteData("ideer.ru", SitesData.ideer)
  val detorgruJokes = SiteReader.ParseSites.getSiteData("det.org.ru", SitesData.detorgru)
  val xkcdbJokes = SiteReader.ParseSites.getSiteData("xkcdb.com", SitesData.xkcdb)

  val sources = List(bashimJokes, /*ithappensJokes,*/ zadolbaliJokes, anekdotJokes, ideerJokes, detorgruJokes,
    xkcdbJokes)

  val siteMap: Map[String, List[Site]] =
    Map() ++ Seq(
      (sites(0) -> bashimJokes),
//      (sites(1) -> ithappensJokes),
      (sites(1) -> zadolbaliJokes),
      (sites(2) -> anekdotJokes),
      (sites(3) -> ideerJokes),
      (sites(4) -> detorgruJokes),
      (sites(5) -> xkcdbJokes)
    )

  val siteSets: List[SiteSet] = sources.map(sites => toSiteSet(sites))

  private val siteSiteSetMap: Map[String, SiteSet] =
    Map() ++ (sites zip siteSets)

  private def unionOfAllSiteSets(curSets: List[SiteSet], acc: SiteSet): SiteSet =
    if (curSets.isEmpty) acc
    else unionOfAllSiteSets(curSets.tail, acc.union(curSets.head))

  val allSites: SiteSet = unionOfAllSiteSets(siteSets, new Empty)
}
