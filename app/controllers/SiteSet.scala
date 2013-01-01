package controllers

/**
 * Created with IntelliJ IDEA.
 * User: sagol
 * Date: 27.12.12
 * Time: 22:08
 * xxxx
 */
class Site(val site: String, val name: String, val url: String, val parsel: String) {

  override def toString: String =
    "Site: " + site + "\n" + "Name: " + name + " " +
      "Url: " + url + " [" + parsel + "]"

}

abstract class SiteSet {

  def filter(p: Site => Boolean): SiteSet = filter0(p, new Empty())
  def filter0(p: Site => Boolean, accu: SiteSet): SiteSet

  def union(that: SiteSet): SiteSet = {
    if (that.isEmpty) this
    else this.incl(that.head).union(that.tail)
  }

  def incl(x: Site): SiteSet
  def contains(x: Site): Boolean
  def isEmpty: Boolean
  def head: Site
  def tail: SiteSet

  def foreach(f: Site => Unit): Unit = {
    if (!this.isEmpty) {
      f(this.head)
      this.tail.foreach(f)
    }
  }

  def remove(tw: Site): SiteSet
}

class Empty extends SiteSet {

  def filter0(p: Site => Boolean, accu: SiteSet): SiteSet = accu

  def contains(x: Site): Boolean = false
  def incl(x: Site): SiteSet = new NonEmpty(x, new Empty, new Empty)
  def isEmpty = true
  def head = throw new Exception("Empty.head")
  def tail = throw new Exception("Empty.tail")
  def remove(tw: Site): SiteSet = this
}

class NonEmpty(elem: Site, left: SiteSet, right: SiteSet) extends SiteSet {

  def filter0(p: Site => Boolean, accu: SiteSet): SiteSet = {
    if (p (this.head)) this.tail.filter0(p, accu.incl (this.head))
    else
      this.tail.filter0 (p, accu)
  }

  def contains(x: Site): Boolean =
    if (x.url < elem.url) left.contains(x)
    else if (elem.url < x.url) right.contains(x)
    else true

  def incl(x: Site): SiteSet = {
    if (x.url < elem.url) new NonEmpty(elem, left.incl(x), right)
    else if (elem.url < x.url) new NonEmpty(elem, left, right.incl(x))
    else this
  }

  def isEmpty = false
  def head = if (left.isEmpty) elem else left.head
  def tail = if (left.isEmpty) right else new NonEmpty(elem, left.tail, right)

  def remove(st: Site): SiteSet =
    if (st.url < elem.url) new NonEmpty(elem, left.remove(st), right)
    else if (elem.url < st.url) new NonEmpty(elem, left, right.remove(st))
    else left.union(right)
}
