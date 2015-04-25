package com.repocad.web

import com.repocad.web.evaluating.Evaluator
import com.repocad.web.lexing.Lexer
import com.repocad.web.parsing.{Expr, Parser, UnitExpr}
import org.scalajs.dom.Event
import org.scalajs.dom.KeyboardEvent
import org.scalajs.dom.MouseEvent
import org.scalajs.dom._
import org.scalajs.dom.raw._
import com.repocad.web.rendering.{Canvas, Editor, Omnibox}
import org.scalajs.dom._
import org.scalajs.dom.raw.{HTMLButtonElement, HTMLCanvasElement, HTMLDivElement, HTMLInputElement}

import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.scalajs.js.annotation.JSExport

/**
 * The entry point for compiling and evaluating repocad code
 *
 *              TODO: Version numbers for AST!
 *              TODO: Import versioned compilers on request
 *              TODO: Use optimised JS
 */
@JSExport("Repocad")
class Repocad(canvasElement : HTMLCanvasElement, editorDiv : HTMLDivElement, title : HTMLInputElement,
              searchDrawing : HTMLButtonElement, newDrawing : HTMLButtonElement) {

  val view = new CanvasPrinter(canvasElement)
  val editor = new Editor(editorDiv, view)
  val canvas = new Canvas(canvasElement, editor, view)
  val omnibox = new Omnibox(title, editor, canvas)

  @JSExport
  def init() : Unit = {
    searchDrawing.onclick = (e : MouseEvent) => {
      omnibox.loadDrawing(title.value)
    }

    newDrawing.onclick = (e : MouseEvent) => {
      val title = scala.scalajs.js.Dynamic.global.prompt("Name the new drawing").toString
      if (title != null && !title.isEmpty) {
        omnibox.loadDrawing(title)
      }
    }

    view.init()
    render()
  }

  @JSExport
  def getDrawings() : js.Array[String] = new JSRichGenTraversableOnce[String](Drawing.drawings).toJSArray

  @JSExport
  def render() : Unit = {
    canvas.render(editor.getAst)
  }

  @JSExport
  def save() : Unit = {
    displaySuccess(editor.module().toString)
  }

  @JSExport
  def printPdf(name : String) : Unit = {
    val printer = new PdfPrinter()
    canvas.render(editor.getAst, printer)
    printer.save(name)
  }

  @JSExport
  def zoom(delta : Double, e : MouseEvent) : Unit = {
    canvas.zoom(delta, e)
  }

  def displayError(error : String): Unit = {
    //debug.innerHTML = error
  }

  def displaySuccess(success : String = ""): Unit = {
    //debug.innerHTML = success
  }

}