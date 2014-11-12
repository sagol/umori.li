package controllers

import akka.actor.Actor
import akka.actor.Props
import akka.event.Logging
import play.api.libs.concurrent.Akka
import play.api.Play.current

class UpdateActor(inst: (Int, String) = (0, "")) extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case "update" => {
      log.info("received update - " + inst._2)
      val instance = SourceInstances.instances(inst._1)
      if (inst._1 != null)
        SourcesData.update(inst._2, instance.contentByName(inst._2))
    }
    case "update_random" => {
      log.info("received update_random")
      SourcesData.update_random(0)
    }
    case "updateall" => {
      log.info("received update - all")
      for (i <- 0 to 4) {
        val ysz = SourceInstances.instances(i).instance.size - 1
          for (y <- 0 to ysz) {
            val instance = SourceInstances.instances(i).instance(y)
            val n = instance.site.name
            val updateActor = Akka.system.actorOf(Props(classOf[UpdateActor], (i, n)))
            updateActor ! "update"
          }
      }
      val updateActor = Akka.system.actorOf(Props(classOf[UpdateActor], (0, "")))
      updateActor ! "update_random"

    }
    case _ => log.info("received unknown message")
  }
}

 /*
case class Request(url: String)
case class Response(response: String)

class Requestor extends Actor {
  def receive = {
    case Request(url) =>
      val resp = "response"
      sender ! Response(resp)
  }
}

class Supervisor extends Actor {
  private val num = 1000000
  private val responses = new ArrayBuffer[String](num)
  def receive = {
    case 'Start =>
      for (idx <- 1 to num) {
        val actor = context.actorOf(Props(new Requestor))
        actor ! Request(s"http://localhost/?id=$idx")
      }
    case Response(resp) =>
      responses += resp
      if (responses.size == num) {
        sender ! responses
      }
  }
}

def main() {
val supervisor = actorSystem.actorOf(Props(new Supervisor))
val result = pattern.ask(supervisor, 'Start)
Await.result(result, 1.minute)
}

   */