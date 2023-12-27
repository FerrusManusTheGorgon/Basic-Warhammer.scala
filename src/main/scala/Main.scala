//import scala.collection.mutable.ArrayBuffer
//import scala.io.StdIn
//import scala.util.Random
//
//object Main extends App {
//
//  val map: ArrayBuffer[ArrayBuffer[String]] = ArrayBuffer.fill(100)(ArrayBuffer.fill(100)(""))
//
//  def gameMap(): Unit =
//    for (i <- 0 until 100; j <- 0 until 100)
//      map(i)(j) = ""
//
//  def printMap(): Unit = {
//    println("+" + ("-" * 100) + "+")
//    for (i <- 0 until 100) {
//      print("|")
//      for (j <- 0 until 100) {
//        map(i)(j) match {
//          case "X" => print("X")
//          case "O" => print("O")
//          case _ => print(" ")
//        }
//      }
//      println("|")
//    }
//    println("+" + ("-" * 100) + "+")
//  }
//
//  gameMap() // Initialize map
//
//  var continueGame = true
//
//  while (continueGame) {
//    println("Please enter a position (example = 1,1)")
//    val input = StdIn.readLine()
//    var x = 0
//    var y = 0
//    try {
//      val positions = input.split(",")
//      x = positions(0).trim.toInt
//      y = positions(1).trim.toInt
//
//      var skipRound = false
//
//      if (x < 1 || x > 100 || y < 1 || y > 100) {
//        println("Position out of range. Please enter coordinates within 1 to 100.")
//      } else if (map(x - 1)(y - 1) != "") {
//        println("That position is already taken, try again")
//        skipRound = true
//      } else {
//        map(x - 1)(y - 1) = "X"
//        printMap()
//
//        if (!skipRound)
//          placeComputerMove()
//          printMap()
//      }
//    } catch {
//      case _: Throwable => println("Invalid input, please try again")
//    }
//
//    def randPos() = Random.nextInt(3)
//
//    def placeComputerMove(): Unit = {
//      var i = randPos()
//      var j = randPos()
//      while (map(i)(j) != "") {
//        i = randPos()
//        j = randPos()
//      }
//      map(i)(j) = "O"
//    }
//  }
//
//  }
