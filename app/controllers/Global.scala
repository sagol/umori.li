import controllers.UpdateActor
import play.api.mvc.RequestHeader

import scala.concurrent.duration.DurationInt
//import play.api.Application
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.concurrent.Akka
import akka.actor.Props
import play.api._

object Global extends GlobalSettings {

  override def onStart(app: play.api.Application) {
    super.onStart(app)
    play.api.Play.mode(app) match {
      case play.api.Mode.Test => // do not schedule anything for Test
      case _ => updateDaemon(app)
    }

  }

  override def onBadRequest(request: RequestHeader, error: String) = {
    Logger.info("Bad Request: " + error)
    null
  }

  def updateDaemon(app: play.api.Application) = {
    Logger.info("Scheduling the updater daemon")
    val updateActor = Akka.system(app).actorOf(Props(classOf[UpdateActor], (0, "")))
    Akka.system(app).scheduler.schedule(0 seconds, 60 seconds, updateActor, "updateall")
  }

}