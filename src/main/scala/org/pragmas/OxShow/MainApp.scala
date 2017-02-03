/******************************************************************************
 
 App: OxShow
 source: MainApp.scala
 Developers: Massimo Maria Ghisalberti <massimo.ghisalberti@pragmas.org> 
 Date: 2017-01-25
 Licence: Apache 2.0

 ******************************************************************************/

package org.pragmas.OxShow

import macrocompat.bundle
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation._
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom
import scalatags.JsDom.all._

object Paths {
  val origin = dom.window.location.origin
  val public = s"${origin}/public"
  val defs = s"${public}/defs"
}

object MainApp extends JSApp {

  var container: HTMLElement = _

  def preparePage(defJson: String): Unit = {
    val ddp = new DocumentDefinitionParser(defJson)
    val dp = new DocumentProducer(ddp.doc)
    val dpc = div(cls:="document-producer-container").render    
    this.container.appendChild(dpc)
    dp.pageContent(dpc)
  }

  @JSExport
  def load(id: String, document: String) = {
    this.container = dom.document.getElementById(id).asInstanceOf[HTMLElement]
    preparePage(document)
  }

  def main() : Unit = {
  }

}
