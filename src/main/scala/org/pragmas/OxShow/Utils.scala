/******************************************************************************
 
 App: OxShow
 source: Utils.scala
 Developers: Massimo Maria Ghisalberti <massimo.ghisalberti@pragmas.org> 
 Date: 2017-01-25
 Licence: Apache 2.0

 ******************************************************************************/


package org.pragmas.OxShow

import scala.scalajs.js
import js.JSConverters._
import scala.scalajs.js.annotation.JSBracketAccess

object Utils {

  def randomString(str: String, size: Int) : String = {
    val r = scala.util.Random.shuffle(('A' to 'Z') ++ ('a' to 'z') ++ ('0' to '9'))
    r.slice(0, size).mkString
  }

  def uuid : String = java.util.UUID.randomUUID.toString
  def uuidFlat : String = uuid.replaceAll("-", "")
  def uuidUnderscore : String = uuid.replaceAll("-", "_")
  def uniqueId : String = s"ID_${uuidUnderscore}"

  def getTV(str: String) : (String, Any) = {
    str.take(2) match {
      case "n:" => ("number", str.drop(2).toInt)
      case "b:" => ("boolean", str.drop(2).toBoolean)
      case _ => ("string", str)
    }
  }

  def strIsInt(str: String): Int = {
    if(str.take(2) == "n:")
      str.drop(2).toInt
    else
      -1
  }

  def strIsBool(str: String): Boolean = {
    if(str.take(2) == "b:")
      str.drop(2).toBoolean
    else
      false
  }

  def optionsToMap(options: Map[String, String]): Map[String, Any] = {
    options.map(k => (k._1, getTV(k._2)._2))
  }

  def mapToLiteral(options: Map[String, String]): js.Dynamic = {
    val obj = js.Dynamic.literal()
    options.foreach(k =>
      obj.updateDynamic(k._1)(getTV(k._2)._2.asInstanceOf[scalajs.js.Any])
    )
    obj
  }
}

