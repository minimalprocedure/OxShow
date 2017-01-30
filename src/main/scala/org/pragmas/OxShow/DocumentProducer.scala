/******************************************************************************
 
 App: OxShow
 source: DocumentProducer.scala
 Developers: Massimo Maria Ghisalberti <massimo.ghisalberti@pragmas.org> 
 Date: 2017-01-25
 Licence: Apache 2.0

 ******************************************************************************/


package org.pragmas.OxShow

import org.scalajs.dom
import org.scalajs.dom.raw.{ Element, Event, HTMLDivElement, Node, UIEvent }
import scalatags.JsDom.all._
import scala.scalajs.js
import js.JSConverters._
import org.scalajs.dom.ext._

class DocumentProducer(doc : Document) {
  
  def prepareStyle(asset: DocAsset) = {
    val empty = List[String]()
    asset.style match {
      case Some(s) => s.foldLeft(empty) (
        (acc, v) => acc ++: List(s"${v._1}:${v._2}")).mkString(";")
      case _ => ""
    }
  }

  def prepareClass(asset: DocAsset) = {
    asset.classNames match {
      case Some(s) => s.mkString(" ")
      case _ => ""
    }
  }

  def prepareVimeo(asset: DocAsset, parent: HTMLDivElement) = {
    dom.document.head.appendChild(
      script(src:="https://player.vimeo.com/api/player.js").render)

    val options = Utils.mapToLiteral(asset.options.get)
    val uniqueId = Utils.uniqueId
    if (options.fullsize == true) {
      options.width = dom.document.body.clientWidth
      options.height = dom.document.body.clientHeight
    }

    parent.appendChild(div(id:=uniqueId, cls:="vimeo-player", data.vimeo.id:=Utils.strIsInt(asset.content)).render)

    val trigger = div(cls:="vimeo-trigger", onshow:= {
      val player = new VimeoPlayer(uniqueId, options)
    }).render
    parent.appendChild(trigger)

    dom.window.onresize = (ev: dom.UIEvent) => {
      val h = dom.window.innerHeight
      val w = dom.window.innerWidth
      val players = dom.document.getElementsByClassName("vimeo-player")
      players.foreach { node: Node =>
        val videoWrap = node.asInstanceOf[Element]
        videoWrap.setAttribute("style", s"height:${h}; width:${w};")
        val iframe = videoWrap.firstChild.asInstanceOf[Element]
        iframe.setAttribute("height", h.toString)
        iframe.setAttribute("width", w.toString)
      }
 
    }
    
  }

  def prepareAsset(asset: DocAsset, parent: HTMLDivElement) = {

    val assetContainer = div(cls:=prepareClass(asset),
      data.name:=asset.name,
      data.T:=asset.T.mkString("_"),
      data.UUID:=Utils.uuid,
      style:= prepareStyle(asset)).render

    parent.render.appendChild(assetContainer)

    asset.T match {
      case DocAsset.TIMAGE =>
        assetContainer.appendChild(img(src:= asset.content).render)
      case DocAsset.TTEXT =>
        assetContainer.appendChild(div(asset.content).render)
      case DocAsset.TVIDEO =>
        assetContainer.appendChild(div(asset.content).render)
      case DocAsset.TVIMEO =>
        prepareVimeo(asset, assetContainer)
      case _ => div()
    }
  }

  def assets(node: DocNode, parent: HTMLDivElement) = {
    val nodeContainer = div(cls:="node-container").render
    parent.appendChild(nodeContainer)
    node.assets.foreach(asset => prepareAsset(asset, nodeContainer))
  }

  def pageContent(parent: HTMLDivElement) = {
    doc.nodes.foreach( node => {
      assets(node, parent)
    })
  }


}
