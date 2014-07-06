package controllers

import java.util.{Random, Date, Calendar}

import scala.collection.immutable.HashMap
import scala.collection.{immutable, mutable}


object SourcesData {

      private var _sources : immutable.HashMap[String, (mutable.LinkedHashSet[UmorElement], Date)] = HashMap()

      def get(name: String) : mutable.LinkedHashSet[UmorElement] = {
          val data = _sources.get(name)
          if (data.isDefined) data.get._1
          else mutable.LinkedHashSet()
      }

      def update(name: String, data: mutable.LinkedHashSet[UmorElement]) =
        if (data.size > 0)
          _sources = _sources.updated(name, (data, Calendar.getInstance().getTime()))

      def update_random(num: Int) = {
        if (_sources.size > 0) {
          var tpl = List[(Int, Int)]()
          val rnd = new Random()
          var len = 0

          for ((key, value) <- _sources) {
            if (key != "abyss") {
              len = len + value._1.size
            }
          }

          if (num < len && num > 0) len = num

          for (i <- 0 to len) {
            val r = rnd.nextInt(_sources.size)
            val s = _sources.slice(r, _sources.size).head._2._1.size
            if (s > 0)
              tpl ::=(r, rnd.nextInt(s))
          }

          var elems: mutable.LinkedHashSet[UmorElement] = mutable.LinkedHashSet()
          val s = tpl.toSet
          for (i <- s) {
            val inst = _sources.slice(i._1, _sources.size).head
            if (inst._1 != "abyss")
              elems += _sources.slice(i._1, _sources.size).head._2._1.slice(i._2, i._2 + 1).head
          }
          _sources = _sources.updated("random", (elems, Calendar.getInstance().getTime()))
        }
      }
}
