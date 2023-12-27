import scala.collection.mutable.ArrayBuffer
import scala.io.StdIn
import scala.util.Random
import models.Character


// Define your Character and Characters objects here
// For demonstration purposes, I'll provide a simplified Character structure

object Main2 extends App {

  val map: ArrayBuffer[ArrayBuffer[String]] = ArrayBuffer.fill(100)(ArrayBuffer.fill(100)(""))

  // Set initial positions of X and O
  var xPos: (Int, Int) = (0, 0)
  var oPos: (Int, Int) = (0, 0)

  def gameMap(): Unit =
    for (i <- 0 until 100; j <- 0 until 100)
      map(i)(j) = ""

  def printMap(): Unit = {
    println("+" + ("-" * 100) + "+")
    for (i <- 0 until 100) {
      print("|")
      for (j <- 0 until 100) {
        map(i)(j) match {
          case "X" => print("X")
          case "O" => print("O")
          case _ => print(" ")
        }
      }
      println("|")
    }
    println("+" + ("-" * 100) + "+")
  }

  gameMap() // Initialize map

  var continueGame = true
  var isXMove = true // Flag to know current player's move

  while (continueGame) {
    val currentCharacter = if (isXMove) Characters.SpaceMarine else Characters.Ork
    val currentPlayer = if (isXMove) "Player X" else "Player O"

    println(s"$currentPlayer's turn. Please enter a position (example = 1,1)")

    val input = StdIn.readLine()
    var x = 0
    var y = 0
    try {
      val positions = input.split(",")
      x = positions(0).trim.toInt
      y = positions(1).trim.toInt

      if (x < 1 || x > 100 || y < 1 || y > 100) {
        println("Position out of range. Please enter coordinates within 1 to 100.")
      } else if (map(x - 1)(y - 1) == "X" || map(x - 1)(y - 1) == "O") {
        println("That position is already taken. Please enter a different coordinate.")
      } else {
        val (curX, curY) = if (isXMove) xPos else oPos
        val distance = Math.abs(curX - (x - 1)) + Math.abs(curY - (y - 1))

        if (distance > currentCharacter.movement) {
          println(s"The distance is greater than ${currentCharacter.movement}. Please enter a valid coordinate.")
        } else {
          if (isXMove) {
            map(xPos._1)(xPos._2) = ""
            xPos = (x - 1, y - 1)
            map(xPos._1)(xPos._2) = "X"
          } else {
            map(oPos._1)(oPos._2) = ""
            oPos = (x - 1, y - 1)
            map(oPos._1)(oPos._2) = "O"
          }
          printMap()
          isXMove = !isXMove // Switch turns
        }
      }
    } catch {
      case _: Throwable => println("Invalid input, please try again")
    }
  }
}

