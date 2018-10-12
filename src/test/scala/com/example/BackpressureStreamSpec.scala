package com.example

import org.scalatest.{BeforeAndAfterAll, WordSpec}
import TestExtension._

class BackpressureStreamSpec extends WordSpec with BeforeAndAfterAll {
  private val backpressureStream = new BackpressureStream()

  override protected def afterAll(): Unit = {
    Main.terminate()
  }

  "backpressure stream" in {
    val tuple = backpressureStream.run()
    tuple._1.await
    tuple._2.await
  }
}
