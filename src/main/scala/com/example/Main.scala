package com.example

import akka.Done
import akka.actor.{ActorSystem, Terminated}
import akka.event.Logging
import akka.stream._

import scala.concurrent._

abstract class Main extends App {
  implicit lazy val system: ActorSystem = ActorSystem("Akka-Stream")
  implicit lazy val materializer: ActorMaterializer = ActorMaterializer()
  implicit lazy val ec : ExecutionContext = system.dispatcher
  lazy val loggingAdapter = Logging(system.eventStream, "BasicStream")

  def terminate(): Future[Terminated] = system.terminate()

  override def main(args: Array[String]): Unit = {
    val eventualDone: Future[Done] = run()
    eventualDone.foreach(_ => terminate())
  }

  def run() : Future[Done]
}