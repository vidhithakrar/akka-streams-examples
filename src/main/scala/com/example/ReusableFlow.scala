package com.example

import akka.{Done, NotUsed}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}

import scala.collection.immutable
import scala.concurrent.Future

object ReusableFlow extends Main {

  override def run(): Future[Done] = {
    val source1: Source[Char, NotUsed] = Source(immutable.Seq('A', 'B', 'C'))
    val source2: Source[Char, NotUsed] = Source(immutable.Seq('D', 'E', 'F'))

    val intToAsciiFlow: Flow[Char, Int, NotUsed] =
      Flow.fromFunction[Char, Int](i => i.toInt)

    source1.via(intToAsciiFlow).runForeach(c => loggingAdapter.info(s"the ASCII is : $c"))

    val sink = intToAsciiFlow.toMat(
      Sink.foreach(c => loggingAdapter.info(s"the ASCII is : $c")))(Keep.right)

    source2.runWith(sink)
  }
}
