package com.example

import akka.{Done, NotUsed}
import akka.stream.scaladsl.{Keep, Sink, Source}

import scala.concurrent.Future

object BasicAPI extends Main {

  override def run(): Future[Done] = {
    val source: Source[Int, NotUsed] = Source(1 to 100)

    loggingAdapter.info("Materializing Source")
    source.toMat(Sink.foreach(i => loggingAdapter.info(i.toString)))(Keep.right).run()
  }

}
