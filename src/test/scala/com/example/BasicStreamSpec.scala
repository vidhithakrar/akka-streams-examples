package com.example

import com.example.TestExtension._
import org.scalatest.{BeforeAndAfterAll, WordSpec}

class BasicStreamSpec extends WordSpec with BeforeAndAfterAll {
  private val basicStream = new BasicStream()

  override protected def afterAll(): Unit = {
    Main.terminate()
  }

  "print elements of a stream" in {
    basicStream.printElements().await
  }

  "print elements of a stream with some sugar coat APIs" in {
    basicStream.printElementsWithSugarCoatedAPIs().await
  }

  "sum of the elements of a stream" in {
    basicStream.sumElements().await
  }

  "reusable processing of a stream" in {
    basicStream.reusableFlow().await
  }

  "operations on a stream" in {
    basicStream.operations().await
  }

  "async operations on a stream" in {
    basicStream.async().await
  }

}
