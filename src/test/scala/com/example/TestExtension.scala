package com.example

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object TestExtension {

  implicit class RichFuture[T](val f: Future[T]) {
    implicit def await: T = Await.result(f, 10000.millis)
  }

}
