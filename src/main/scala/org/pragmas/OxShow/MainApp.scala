/******************************************************************************
 
 App: OxShow
 source: MainApp.scala
 Developers: Massimo Maria Ghisalberti <massimo.ghisalberti@pragmas.org> 
 Date: 2017-01-25
 Licence: Apache 2.0

 ******************************************************************************/

package org.pragmas.OxShow

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.JSApp
import scala.util.{Failure, Success}

import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.raw.XMLHttpRequest
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object Paths {
  val origin = dom.window.location.origin
  val defs = s"${origin}/public/defs"
}

object MainApp extends JSApp {

  def loadDefPage(name: String = "page"): Future[XMLHttpRequest] = {
    Ajax.get(s"${Paths.defs}/${name}.json")
      .andThen {
      case Success(xhr) => {
        preparePage(xhr.responseText)
      }
      case Failure(f) => prepareErrorPage
    }
  }

  def prepareErrorPage: Unit = {
    dom.document.body.innerHTML = "ERROR"
  }

  def preparePage(defJson: String): Unit = {
    val ddp = new DocumentDefinitionParser(defJson)
    val dp = new DocumentProducer(ddp.doc)
    val dpc = div(cls:="document-producer-container").render
    dom.document.body.appendChild(dpc)
    dp.pageContent(dpc)
  }

  def main() : Unit = {
    loadDefPage("page")
  }

}
