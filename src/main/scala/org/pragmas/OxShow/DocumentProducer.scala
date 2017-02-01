/**
  * ****************************************************************************
  *
  * App: OxShow
  * source: DocumentProducer.scala
  * Developers: Massimo Maria Ghisalberti <massimo.ghisalberti@pragmas.org>
  * Date: 2017-01-25
  * Licence: Apache 2.0
  *
  * ****************************************************************************
  */

package org.pragmas.OxShow

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}
import org.scalajs.dom.raw.{HTMLDivElement, HTMLElement, HTMLIFrameElement, Node, UIEvent}
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._
import org.scalajs.dom
import org.scalajs.dom.ext._

class DocumentProducer(doc: Document) {

  def prepareStyle(asset: DocAsset) = {
    val empty = List[String]()
    asset.style match {
      case Some(s) => s.foldLeft(empty)((acc, v) => {
        val (key, value) = v
        acc ++: List(s"${key}:${value}")
      }).mkString(";")        
      case _ => ""
    }
  }

  def prepareClass(asset: DocAsset) = {
    asset.classNames match {
      case Some(s) => s.mkString(" ")
      case _ => ""
    }
  }

  def prepareActionAsset(asset: DocAsset, parent: HTMLDivElement) = {
    val options = asset.options.get
    val TTEXT = DocAsset.TTEXT.head
    val TIMAGE = DocAsset.TIMAGE.head

    val (fullsize, styleDim, w, h) = isFullSize(asset)

    val assetContainer = asset.T.head match {
      case TTEXT => div(data.fullsize:=fullsize.toString, style:=styleDim)(asset.content).render
      case TIMAGE => img(data.fullsize:=fullsize.toString, style:=styleDim)(src := asset.content).render
      case _ => div(data.fullsize:=fullsize.toString, style:=styleDim)(asset.content).render
    }

    parent.appendChild(assetContainer)
    options.foreach(t => {
      val (event, code) = t
      assetContainer.addEventListener(event,
        (event: UIEvent) => {
          val f = new js.Function("event", s"${code}")
          f.call(event.target, event)
        },
        false)
    })
  }

  def prepareVimeo(asset: DocAsset, parent: HTMLDivElement) = {
    dom.document.head.appendChild(script(src := "https://player.vimeo.com/api/player.js").render)
    val options = Utils.mapToLiteral(asset.options.get)
    val uniqueId = Utils.uniqueId

    val fullsize = Utils.strIsBool(asset.fullsize.getOrElse(DocAsset.BFALSE))
    if (fullsize) {
      val (w, h) = Utils.fullAreaDim
      options.width = w
      options.height = h
    }

    parent.appendChild(div(id := uniqueId, cls := "vimeo-player",
      data.video.fullsize:=fullsize.toString, data.vimeo.id := Utils.strIsInt(asset.content)).render)

    val trigger = div(cls := "vimeo-trigger",
      onshow :=  { VideoPlayer.newVimeoPlayer(uniqueId, asset.name, parent, options) }).render
    parent.appendChild(trigger)

  }

  def isFullSize(asset: DocAsset): (Boolean, String, Double, Double)= {
    println(asset.fullsize.getOrElse(DocAsset.BFALSE))
    val fullsize = Utils.strIsBool(asset.fullsize.getOrElse(DocAsset.BFALSE))
    if(fullsize) {
      val (w, h) = Utils.fullAreaDim
      (fullsize, s"width:${w}px;height:${h}px;", w, h)
    } else (fullsize, "", 0, 0)
  }

  def resizeFullsizes = {
    dom.window.onresize = (ev: dom.UIEvent) => {
      val (w, h) = Utils.fullAreaDim
      val assets = dom.document.getElementsByClassName("doc-asset")

      assets.foreach { node: Node =>
        val asset = node.firstChild.asInstanceOf[HTMLElement]

        if (asset.getAttribute("data-video-fullsize") == "true") {
          asset.setAttribute("style", s"height:${h}; width:${w};")
          val iframe = asset.firstChild.asInstanceOf[HTMLIFrameElement]
          iframe.width = w.toString
          iframe.height = h.toString
        }

        if (asset.getAttribute("data-fullsize") == "true") {
          asset.style.width = s"${w}px"
          asset.style.height = s"${h}px"
        }
      }
    }
  }

  def insertByHttp(uid: String, path: String) = {
    Ajax.get(s"${Paths.public}/${path}")
      .andThen {
        case Success(xhr) =>
          dom.document.getElementById(uid).innerHTML = xhr.responseText
        case Failure(f) =>
          dom.document.getElementById(uid).innerHTML = ""
      }
  }

  def prepareSimpleAsset(asset: DocAsset, parent: HTMLDivElement) = {
    val (fullsize, styleDim, w, h) = isFullSize(asset)
    val assetContainer = asset.T match {
      case DocAsset.TTEXT => {
        val (prot, content) = Utils.getContentProtocol(asset.content)
        prot match {
          case ContentProtocols.HTTP =>
            val uid = Utils.uniqueId
            val dv = div(id:=uid, cls:="text-asset-prot-http")
            insertByHttp(uid, content)
            div(data.fullsize:=fullsize.toString, style:=styleDim)(dv).render
          case ContentProtocols.STRING =>
            div(data.fullsize:=fullsize.toString, style:=styleDim)(content).render
        }
      }
      case DocAsset.THTML =>
        div(data.fullsize:=fullsize.toString, style:=styleDim)(raw(asset.content)).render
      case DocAsset.TIMAGE =>
        img(data.fullsize:=fullsize.toString, style:=styleDim)(src := asset.content).render
      case _ =>
        div(data.fullsize:=fullsize.toString, style:=styleDim)("UNIMPLEMENTED").render
    }
    parent.appendChild(assetContainer)
  }

  def prepareAsset(asset: DocAsset, parent: HTMLDivElement, zIndex: Int = 0) = {

    val assetContainer = div(cls := s"doc-asset ${asset.name} " + prepareClass(asset),
      data.name := asset.name,
      data.T := asset.T.mkString("."),
      data.UUID := Utils.uuid,
      style := prepareStyle(asset) + s";z-index:${zIndex}",
      title := asset.tooltip.getOrElse("")
    ).render

    parent.appendChild(assetContainer)

    asset.T match {
      case DocAsset.TIMAGE | DocAsset.TTEXT | DocAsset.THTML | DocAsset.TVIDEO =>
        prepareSimpleAsset(asset, assetContainer)
      case DocAsset.TIMAGE_ACTION =>
        prepareActionAsset(asset, assetContainer)
      case DocAsset.TTEXT_ACTION =>
        prepareActionAsset(asset, assetContainer)
      case DocAsset.TVIDEO_VIMEO =>
        prepareVimeo(asset, assetContainer)
      case _ => div()
    }

    resizeFullsizes
  }

  def assets(node: DocNode, parent: HTMLDivElement, zIndex: Int = 0) = {    
    val nodeContainer = div(cls:=s"node-container ${node.name}", data.name:=node.name, style:=s"z-index:${zIndex}").render
    nodeContainer.appendChild(
      a(cls:="node-container-anchor", data.t:="node-container-anchor", name:=node.name, style:="display:block;").render)
    parent.appendChild(nodeContainer)
    node.assets.foldLeft(node.assets.length + 10)((index, asset) => {
      prepareAsset(asset, nodeContainer, index + zIndex * 10)
      index - 1
    })
    nodeContainer
  }

  def pageContent(parent: HTMLDivElement) = {
    doc.nodes.foldLeft(doc.nodes.length)((index, node) => {
      parent.appendChild(assets(node, parent, index))
      index - 1
    })
  }

}
