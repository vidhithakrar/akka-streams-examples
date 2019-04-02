package com.example

import akka.Done
import akka.stream.scaladsl.Source

import scala.collection.immutable
import scala.concurrent.Future

object Operations extends Main {

  override def run(): Future[Done] = {
    val source =
      Source(immutable.Seq('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'))
        .map { c =>
          //          Thread.sleep(50)
          loggingAdapter.info(c.toString)
          c.toLower
        }.async
        .map { c =>
          //          Thread.sleep(100)
          loggingAdapter.info(c.toString)
          c.toInt
        }
        .filterNot(_ > 99)
        .fold("")((s, c) => s.concat(c.toString))

    source.runForeach(c => loggingAdapter.info(s"the lower case string is : $c"))
  }
}
