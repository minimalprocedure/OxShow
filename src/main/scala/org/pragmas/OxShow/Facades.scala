/******************************************************************************
 
 App: OxShow
 source: Facades.scala
 Developers: Massimo Maria Ghisalberti <massimo.ghisalberti@pragmas.org> 
 Date: 2017-01-25
 Licence: Apache 2.0

 ******************************************************************************/


package org.pragmas.OxShow

import org.scalajs.dom.raw.Element
import scala.scalajs.js
import scala.scalajs.js.annotation._
import org.scalajs.dom.raw.HTMLDivElement
import org.scalajs.dom.raw.HTMLElement
//import scalatags.JsDom.all._
import org.scalajs.dom
import org.scalajs.dom.ext._

@JSName("Vimeo.Player")
@js.native
class VimeoPlayer(uniqueId: String, options: js.Dynamic = ???) extends js.Object {

  var parent: HTMLDivElement = js.native

  def play(): js.Promise[Number] = js.native
  def pause(): js.Promise[Number]  = js.native

}

@ScalaJSDefined
@JSExport
object VideoPlayer extends js.Object {
  
  val vimeoPlayers = js.Dictionary[VimeoPlayer]()

  def newVimeoPlayer(uniqueId: String, name: String, parent: HTMLDivElement, options: js.Dynamic = ???): VimeoPlayer = {
    vimeoPlayers += (name -> new VimeoPlayer(uniqueId, options))
    vimeoPlayers(name).parent = parent
    vimeoPlayers(name)
  }

  def play(name: String): js.Promise[Number] = {
    vimeoPlayers(name).play()
  }

  def pause(name: String): js.Promise[Number] = {
    vimeoPlayers(name).pause()
  }

  def show(name: String) = {
    vimeoPlayers(name).parent.style.display = "block"
  }

  def hide(name: String) = {
    vimeoPlayers(name).parent.style.display = "none"
  }
 
}

@ScalaJSDefined
@JSExport
object OxUtils extends js.Object {

  
  def showElement(dataName: String) = {
    val e = dom.document.querySelector(s"[data-name=${dataName}]")
    e.asInstanceOf[HTMLElement].style.display = "block"
  }

  def hideElement(dataName: String) = {
    val e = dom.document.querySelector(s"[data-name=${dataName}]")
    e.asInstanceOf[HTMLElement].style.display = "none"
  }

  def toggleElement(dataName: String) = {
    val e = dom.document.querySelector(s"[data-name=${dataName}]")
    e.asInstanceOf[HTMLElement].style.display match {
      case "none" => e.asInstanceOf[HTMLElement].style.display = "block"
      case "block" => e.asInstanceOf[HTMLElement].style.display = "none"
      case _ => e.asInstanceOf[HTMLElement].style.display = "none"
    }
  }

}
