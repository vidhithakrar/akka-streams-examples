package com.example

import akka.Done
import akka.Done.done
import akka.stream.scaladsl.{Keep, Sink, Source}

import scala.collection.immutable
import scala.concurrent.Future
import scala.util.Success

object AsynchronousBoundry extends Main {

  override def run(): Future[Done] = {
    val source =
      Source(immutable.Seq('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'))
        .mapAsync(2)(c => Future {
          loggingAdapter.info(s"It is paralleled for: $c")
          c.toLower
        })
        .map { c =>
          loggingAdapter.info(c.toString)
          c.toInt
        }

    source.toMat(Sink.seq)(Keep.right).run().andThen {
      case Success(s) =>
        loggingAdapter.info(s"Whole collection is: $s")
        s
    }.map(_ => done())
  }
}
