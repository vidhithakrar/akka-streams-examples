package com.example

import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.{Done, NotUsed}
import com.example.Main.{ec, materializer}

import scala.collection.immutable
import scala.concurrent.Future
import scala.util.Success

class BasicStream {

  def printElements(): Future[Done] = {
    val source: Source[Int, NotUsed] = Source(1 to 100)

    source.toMat(Sink.foreach(println(_)))(Keep.right).run()
  }

  def printElementsWithSugarCoatedAPIs(): Future[Done] = {
    val source: Source[Int, NotUsed] = Source(1 to 100)

    source.runForeach(println)
  }

  def sumElements(): Future[Int] = {
    val source: Source[Int, NotUsed] = Source(1 to 100)

    source.runWith(Sink.fold(0)(_ + _)).andThen {
      case Success(s) => println(s"Sum of elements is: $s")
    }
  }

  def reusableFlow(): Future[Done] = {
    val source1: Source[Char, NotUsed] = Source(immutable.Seq('A', 'B', 'C'))

    val source2: Source[Char, NotUsed] = Source(immutable.Seq('D', 'E', 'F'))

    val intToAsciiFlow: Flow[Char, Int, NotUsed] =
      Flow.fromFunction[Char, Int](i => i.toInt)

    source1.via(intToAsciiFlow).runForeach(c => println(s"the ASCII is : $c"))
    //source2.via(intToAsciiFlow).runForeach(println)

    val sink = intToAsciiFlow.toMat(
      Sink.foreach(c => println(s"the ASCII is : $c")))(Keep.right)
    source2.toMat(sink)(Keep.right).run()
  }

  def operations(): Future[Done] = {
    val source =
      Source(immutable.Seq('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'))
        .map(_.toInt)
        .filterNot(_ == 65)
        .fold(0)(_ + _)

    source.runForeach(c => println(s"the ASCII is : $c"))
  }

  def async(): Future[Seq[Int]] = {
    val source =
      Source(immutable.Seq('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'))
        .mapAsync(5)(c => Future {
          println(s"It is paralleled for: $c")
          c.toInt
        })

    source.toMat(Sink.seq)(Keep.right).run().andThen {
      case Success(s) =>
        println(s"Whole collection is: $s")
        s
    }
  }
}
