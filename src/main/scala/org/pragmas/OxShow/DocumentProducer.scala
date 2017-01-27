/******************************************************************************
 
 App: OxShow
 source: DocumentProducer.scala
 Developers: Massimo Maria Ghisalberti <massimo.ghisalberti@pragmas.org> 
 Date: 2017-01-25
 Licence: Apache 2.0

 ******************************************************************************/


package org.pragmas.OxShow

import scala.collection.mutable.ArrayBuffer

import org.scalajs.dom
import org.scalajs.dom.raw.Element
import scalatags.Text.TypedTag
import scalatags.Text.all._
import scalatags.stylesheet._

class DocumentProducer(doc : Document) {
 
  def pageContent = {
    val content = assets(doc.nodes(0))
    dom.document.body.innerHTML = div(content).render
  }

  def prepareStyle(asset: DocAsset) = {
    val empty = List[String]()
    asset.style match {
      case Some(s) => s.foldLeft(empty) (
        (acc, v) => acc ++: List(s"${v._1}:${v._2}")).mkString(";")
    case _ => ""
  }
}

  def prepareClass(asset: DocAsset) = {
    println(asset.classNames)
    asset.classNames match {
      case Some(s) => s.mkString(" ")
      case _ => ""
    }
  }

  def prepareAsset(asset: DocAsset) = {
    div(cls:=prepareClass(asset), data.name:=asset.name, data.t:=asset.t, data.UUID:= Utils.uuid, style:= prepareStyle(asset))(
      asset.t match {
        case DocAsset.IMAGE => img(src:= asset.content)
        case DocAsset.TEXT => div(asset.content)
        case _ => ""
      }
    )
  }

  def assets(node: DocNode): List[TypedTag[String]] = {    
    node.assets.map(prepareAsset)
  }

}
