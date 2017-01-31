/******************************************************************************
 
 App: OxShow
 source: Facades.scala
 Developers: Massimo Maria Ghisalberti <massimo.ghisalberti@pragmas.org> 
 Date: 2017-01-25
 Licence: Apache 2.0

 ******************************************************************************/


package org.pragmas.OxShow

import scala.scalajs.js
import scala.scalajs.js.annotation._
import org.scalajs.dom.raw.HTMLDivElement

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
  
  val vimeoPlayers = js.Dictionary[VimeoPlayer]() //mutable.Map[String, VimeoPlayer]()

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
