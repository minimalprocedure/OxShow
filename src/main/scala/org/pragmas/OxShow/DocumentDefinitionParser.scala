/******************************************************************************

 App: OxShow
 source: DocumentDefinitionParser.scala
 Developers: Massimo Maria Ghisalberti <massimo.ghisalberti@pragmas.org>
 Date: 2017-01-25
 Licence: Apache 2.0

 ******************************************************************************/


package org.pragmas.OxShow

object DocAsset {
  val EMPTY = ""
  val BFALSE = "b:false"
  val BTRUE = "b:true"
  val EMPTY_LIST = List()
  val EMPTY_MAP = Map[String, Any]()

  val TDOC = List[String]("document")

  val TIMAGE = List[String]("image")
  val TIMAGE_ACTION = List[String](TIMAGE(0), "action")

  val THTML = List[String]("html")
  val THTML_CONTENT = List[String](THTML(0), "content")
  val THTML_ACTION = List[String](THTML(0), "action")
  val TTEXT = List[String]("text")
  val TTEXT_ACTION = List[String](TTEXT(0), "action")
  val TTEXT_LOAD = List[String](TTEXT(0), "load")

  val TAUDIO = List[String]("audio")
  val TVIDEO = List[String]("video")

  val TVIDEO_VIMEO = List[String](TVIDEO(0), "vimeo")
  val TBACKGROUND = List[String]("background")
  val TGENERIC = List[String]("generic")
}

object DocStyle {
  val EMPTY = Map[String, String]()
}

object DocNode {
  val EMPTY = ""
  val EMPTY_ASSETS = List[DocAsset]()
  val EMPTY_NODES = List[DocNode]()
}

object Document {
  val EMPTY = ""
  val EMPTY_NODES = List[DocNode]()
}

case class DocNode(
  name: String = DocNode.EMPTY,
  title: String = DocNode.EMPTY,
  assets : List[DocAsset] = DocNode.EMPTY_ASSETS,
  links : Option[List[Map[String, String]]] = Some(DocAsset.EMPTY_LIST) //Some(DocStyle.EMPTY)
)

case class Document(
  name: String = Document.EMPTY,
  nodes: List[DocNode] = Document.EMPTY_NODES
)

case class DocAsset(
  T: List[String] = DocAsset.TGENERIC,
  name: String = DocAsset.EMPTY,
  content: String =  DocAsset.EMPTY,
  tooltip: Option[String] = Some(DocAsset.EMPTY),
  fullsize: Option[String] = Some(DocAsset.BFALSE),
  classNames: Option[List[String]] = Some(DocAsset.EMPTY_LIST),
  style: Option[Map[String, String]] = Some(DocStyle.EMPTY),
  options: Option[Map[String, String]] = Some(DocStyle.EMPTY)
)

class DocumentDefinitionParser(jsonDoc: String = "{}") {

  import io.circe._
  import io.circe.parser._
  import io.circe.generic.auto._
  import io.circe.Decoder

  val doc = open

  def open : Document = {
    val json = parse(jsonDoc).getOrElse(Json.Null)
    json.as(Decoder[Document]) match {
      case Right(doc) => doc.asInstanceOf[Document]
      case Left(e) => Document("document", List[DocNode]())
    }
  }

}
