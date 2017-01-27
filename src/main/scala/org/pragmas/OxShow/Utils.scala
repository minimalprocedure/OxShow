/******************************************************************************
 
 App: OxShow
 source: DocumentProducer.scala
 Developers: Massimo Maria Ghisalberti <massimo.ghisalberti@pragmas.org> 
 Date: 2017-01-25
 Licence: Apache 2.0

 ******************************************************************************/


package org.pragmas.OxShow

object Utils {

  def randomString(str: String, size: Int) : String = {
    val r = scala.util.Random.shuffle(('A' to 'Z') ++ ('a' to 'z') ++ ('0' to '9'))
    r.slice(0, size).mkString
  }

  def uuid : String = java.util.UUID.randomUUID.toString

}
