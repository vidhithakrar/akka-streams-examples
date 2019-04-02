package com.example

import akka.Done.done
import akka.{Done, NotUsed}
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.Future
import scala.util.Success

object SugarCoatedAPI extends Main {

  override def run(): Future[Done] = {
    val source: Source[Int, NotUsed] = Source(1 to 100)
    source.runForeach(i => loggingAdapter.info(i.toString))

    source.runWith(Sink.fold(0)(_ + _)).andThen {
      case Success(s) => loggingAdapter.info(s"Sum of elements is: $s")
    }.map(_ => done())
  }
}
