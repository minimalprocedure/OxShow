/******************************************************************************
 
 App: OxShow
 source: TestJSON.scala
 Developers: Massimo Maria Ghisalberti <massimo.ghisalberti@pragmas.org> 
 Date: 2017-01-25
 Licence: Apache 2.0

 ******************************************************************************/

package org.pragmas.OxShow

object TestJSON {

    val json = """
{
      "name": "document",
      "nodes": [
        {
            "links": [],
            "title": "pag1",
            "name": "node1",
            "assets": [
                {
                    "t": "text",
                    "name": "asset1",
                    "content": "I'm a text",
                    "bound": {
                        "pos": "absolute",
                        "left": "100px",
                        "top": "100px",
                        "height": "100px",
                        "width": "100px"
                    }
                },
                {
                    "t": "image",
                    "name": "asset2",
                    "content": "http://www.scala-js.org/assets/img/scala-js-site-logo.svg",
                    "bound": {
                        "pos": "absolute",
                        "left": "0px",
                        "top": "0px",
                        "height": "100px",
                        "width": "100px"
                    }
                }
            ],
            "background": {
                "t": "image",
                "name": "back",
                "content": "http://pippo.png",
                "bound": {
                    "pos": "absolute",
                    "left": "0px",
                    "top": "0px",
                    "height": "100px",
                    "width": "100px"
                }
            }
        }
    ]
}
"""
}
