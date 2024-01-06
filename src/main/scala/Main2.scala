import scala.collection.mutable.ArrayBuffer
import scala.io.StdIn
import scala.util.Random
import models.Character


// Define your Character and Characters objects here
// For demonstration purposes, I'll provide a simplified Character structure

import scala.collection.mutable.ArrayBuffer
import scala.io.StdIn
import scala.util.Random



object Main2 extends App {

  val map: ArrayBuffer[ArrayBuffer[String]] = ArrayBuffer.fill(100)(ArrayBuffer.fill(100)(""))

  var xPos: (Int, Int) = (0, 0)
  var oPos: (Int, Int) = (0, 0)
  val horizontalBlockedCoordinates: List[(Int, Int)] = List((5, 5), (5, 6), (5, 7), (5, 8), (5, 9), (5, 10))
  val verticalBlockedCoordinates: List[(Int, Int)] = List((6, 5), (6, 5), (7, 5), (8, 5), (9, 5), (10, 5))

  var gameStartedX = false
  var gameStartedO = false

  def gameMap(): Unit =
    for (i <- 0 until 100; j <- 0 until 100)
      map(i)(j) = ""

  def printMap(): Unit = {
    println("+" + ("-" * 100) + "+")
    for (i <- 0 until 100) {
      print("|")
      for (j <- 0 until 100) {
        if (horizontalBlockedCoordinates.contains((i, j))) {
          print("-") // Represents a blocked coordinate
        } else if (verticalBlockedCoordinates.contains((i, j))) {
            print("|") // Represents a blocked coordinate
          } else {
            map(i)(j) match {
              case "X" => print("X")
              case "O" => print("O")
              case _ => print(" ")
            }
          }
        }
        println("|")
      }
      println("+" + ("-" * 100) + "+")
    }


    gameMap()

    var continueGame = true
    var isXMove = true

    while (continueGame) {
      val currentCharacter = if (isXMove) Characters.SpaceMarine else Characters.Ork
      val currentPlayer = if (isXMove) "Player X" else "Player O"

      println(s"$currentPlayer's turn. Please enter a position (example = 1,1) or 'stay' to remain in the same position")

      val input = StdIn.readLine()
      var x = 0
      var y = 0
      try {
        if (input.toLowerCase() != "stay") {
          val positions = input.split(",")
          x = positions(0).trim.toInt
          y = positions(1).trim.toInt
        } else {
          if (isXMove) {
            x = xPos._1 + 1
            y = xPos._2 + 1
          } else {
            x = oPos._1 + 1
            y = oPos._2 + 1
          }
        }

        if (x < 1 || x > 100 || y < 1 || y > 100) {
          println("Position out of range. Please enter coordinates within 1 to 100.")
        } else if ((isXMove && map(x - 1)(y - 1) == "O") || (!isXMove && map(x - 1)(y - 1) == "X")) {
          println("That position is already taken by the opposing player. Please enter a different coordinate.")
        } else {
          val (curX, curY) = if (isXMove) xPos else oPos // tuples representing the current position depending whose turn it is.
          val distance = Math.abs(curX - (x - 1)) + Math.abs(curY - (y - 1)) // Manhattan distance

          if (distance > currentCharacter.movement) {
            println(s"The distance is greater than ${currentCharacter.movement}. Please enter a valid coordinate.")
          } else {
            if (isXMove) {
              map(xPos._1)(xPos._2) = "" // removes the current position before creating new position
              xPos = (x - 1, y - 1)
              map(xPos._1)(xPos._2) = "X"
              if (!gameStartedX) gameStartedX = true
              checkAttack(xPos)
              checkRangedAttack(xPos)
            } else {
              map(oPos._1)(oPos._2) = ""
              oPos = (x - 1, y - 1)
              map(oPos._1)(oPos._2) = "O"
              if (!gameStartedO) gameStartedO = true
              checkAttack(oPos)
              checkRangedAttack(oPos)
            }
            printMap()
            isXMove = !isXMove

            if (gameStartedX && gameStartedO && (!map.flatten.contains("X") || !map.flatten.contains("O"))) {
              if (!map.flatten.contains("X")) {
                println("The Ork wins! The Hummie was Krumped.")
              } else {
                println("The Space Marine wins! The Xeno scum has been purged.")
              }
              continueGame = false
            }
          }
        }
      } catch {
        case _: Throwable => println("Invalid input, please try again")
      }
    }


    def checkAttack(position: (Int, Int)): Unit = {
      val adjacentPositions = List(
        (position._1 - 1, position._2), (position._1 + 1, position._2),
        (position._1, position._2 - 1), (position._1, position._2 + 1)
      )

      val attacker = if (isXMove) "X" else "O"
      val defender = if (isXMove) "O" else "X"
      val attackerWS = if (isXMove) Characters.SpaceMarine.weaponSkill else Characters.Ork.weaponSkill


      adjacentPositions.foreach { case (x, y) =>
        if (x >= 0 && x < 100 && y >= 0 && y < 100 && map(x)(y) == defender) {
          val randomChance = Random.nextInt(100) + 1
          if (randomChance <= attackerWS) {
            println(s"$attacker attacked $defender and slaughtered!")
            map(x)(y) = ""
          } else {
            println(s"$attacker swung at $defender but missed!")
          }
        }
      }
    }

    def checkRangedAttack(position: (Int, Int)): Unit = {
      val row = position._1
      val col = position._2
      val currentCharacter = if (isXMove) Characters.SpaceMarine else Characters.Ork
      val range = currentCharacter.range

      // Check vertically above and below within range
      for (x <- row - 1 to Math.max(0, row - range) by -1) {
        if (map(x)(col) == "O") {
          performRangedAttack((x, col))
          return
        } else if (map(x)(col) == "X") {
          return
        }
      }

      for (x <- row + 1 to Math.min(99, row + range)) {
        if (map(x)(col) == "O") {
          performRangedAttack((x, col))
          return
        } else if (map(x)(col) == "X") {
          return
        }
      }

      // Check horizontally left and right within range
      for (y <- col - 1 to Math.max(0, col - range) by -1) {
        if (map(row)(y) == "O") {
          performRangedAttack((row, y))
          return
        } else if (map(row)(y) == "X") {
          return
        }
      }

      for (y <- col + 1 to Math.min(99, col + range)) {
        if (map(row)(y) == "O") {
          performRangedAttack((row, y))
          return
        } else if (map(row)(y) == "X") {
          return
        }
      }
    }

    def performRangedAttack(position: (Int, Int)): Unit = {
      val attacker = if (isXMove) "X" else "O"
      val defender = if (isXMove) "O" else "X"
      val attackerBS = if (isXMove) Characters.SpaceMarine.ballisticSkill else Characters.Ork.ballisticSkill

      val randomChance = Random.nextInt(100) + 1

      if (isXMove) {
        if (randomChance <= attackerBS) {
          println("Space Marine opened fire with his Bolter and eliminated the Ork!")
          map(position._1)(position._2) = "" // Remove the defeated enemy
        } else {
          println("Space Marine Bolts missed the Ork !")
        }
      } else {
        if (randomChance <= attackerBS) {
          println("Ork unleashed his Big Shoota and blasted the Space Marine to bitz!")
          map(position._1)(position._2) = "" // Remove the defeated enemy
        } else {
          println("Ork dakka dakka dakka dakka missed!")
        }
      }
    }


  }



