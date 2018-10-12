package com.example

import akka.actor.{ActorSystem, Terminated}
import akka.stream._

import scala.concurrent._

object Main extends App {
  implicit lazy val system: ActorSystem = ActorSystem("Akka-Stream")
  implicit lazy val materializer: ActorMaterializer = ActorMaterializer()
  implicit lazy val ec : ExecutionContext = system.dispatcher

  def terminate(): Future[Terminated] = system.terminate()
}