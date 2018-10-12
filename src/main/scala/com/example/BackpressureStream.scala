package com.example

import java.nio.file.Paths

import akka.NotUsed
import akka.stream.scaladsl.{Broadcast, FileIO, Flow, GraphDSL, Keep, RunnableGraph, Sink, Source}
import akka.stream.{ClosedShape, IOResult, OverflowStrategy, ThrottleMode}
import akka.util.ByteString
import com.example.Main.materializer

import scala.concurrent.Future
import scala.concurrent.duration._

class BackpressureStream {

  def run(): (Future[IOResult], Future[IOResult]) = {
    val source: Source[Int, NotUsed] = Source(1 to 100)
    val factorials: Source[BigInt, NotUsed] = source.scan(BigInt(1))((acc, next) => acc * next)
    val sink1 = lineSink("factorial1.txt")
    val sink2 = lineSink("factorial2.txt")

    val slowSink2 = Flow[String]
      .via(Flow[String].throttle(1, 1.second, 1, ThrottleMode.shaping))
      .toMat(sink2)(Keep.right)

    val bufferedSink2 = Flow[String]
      .buffer(30, OverflowStrategy.backpressure)
      .via(Flow[String].throttle(1, 1.second, 1, ThrottleMode.shaping))
      .toMat(sink2)(Keep.right)

    val g = RunnableGraph.fromGraph(GraphDSL.create(sink1, bufferedSink2)((_, _)) { implicit b =>
      (s1, s2) =>
        import GraphDSL.Implicits._

        val bcast = b.add(Broadcast[String](2))

        factorials.map(_.toString) ~> bcast.in
        bcast ~> s1.in
        bcast ~> s2.in
        ClosedShape
    })

    g.run()
  }

  private def lineSink(filename: String): Sink[String, Future[IOResult]] = {
    Flow[String]
      .alsoTo(Sink.foreach(s => println(s"$filename: $s")))
      .map(s => ByteString(s + "\n"))
      .toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)
  }
}
