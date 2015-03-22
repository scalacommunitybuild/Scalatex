package scalatex.scrollspy

import org.scalajs.dom
import org.scalajs.dom.ext._
import org.scalajs.dom.html

import scalajs.js
import scalajs.js.annotation.JSExport
import scalatags.JsDom.all._
import scalatags.JsDom.tags2

@JSExport
object Controller{
  lazy val styleTag = tags2.style.render
  dom.document.head.appendChild(styleTag)
  styleTag.textContent += Styles.styleSheetText

  def munge(name: String) = {
    name.replace(" ", "")
  }

  @JSExport
  def main(data: scala.scalajs.js.Any,
           menu: html.Element,
           menuLink: html.Element) = {

    val structure = upickle.readJs[Tree[String]](upickle.json.readJs(data))


    val snippets = dom.document.getElementsByClassName("highlight-me")

    snippets.foreach(js.Dynamic.global.hljs.highlightBlock(_))

    val scrollSpy = new ScrollSpy(structure)
    val list = ul(
      cls := "menu-item-list",
      margin := 0,
      padding := 0,
      scrollSpy.domTrees.value.frag
    ).render

    def updateScroll() = scrollSpy()
    val expandIcon = i(
      cls := "fa fa-caret-down",
      color := "white",
      padding := "15px 10px"
    ).render

    val expandLink = a(
      expandIcon,
      href := "javascript:",
      marginLeft := "0px",
      paddingLeft := "15px",
      paddingRight := "15px",
      position.absolute,
      top := "0px",
      right := "0px",
      cls := "pure-menu-selected",
      onclick := { (e: dom.Event) =>
        expandIcon.classList.toggle("fa-caret-down")
        expandIcon.classList.toggle("fa-caret-up")
        scrollSpy.toggleOpen()
      }
    ).render


    menu.appendChild(
      div(
        list,
        expandLink
      ).render
    )

    dom.addEventListener("scroll", (e: dom.UIEvent) => updateScroll())
    updateScroll()

  }
}
