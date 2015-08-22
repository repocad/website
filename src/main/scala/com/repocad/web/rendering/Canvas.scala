package com.repocad.web.rendering

import com.repocad.reposcript.Printer
import com.repocad.reposcript.parsing.Expr
import com.repocad.web.{Reposcript, CanvasPrinter, Vector2D}
import org.scalajs.dom._
import org.scalajs.dom.raw.HTMLCanvasElement

import scala.scalajs.js

/**
 * A canvas that can draw a drawing
 */
class Canvas(canvas : HTMLCanvasElement, editor : Editor, printer : CanvasPrinter) {

  var landscape = printer.landscape
  var center : Vector2D = printer.windowCenter

  var zoomLevel : Double = 0.5 // the current zoom-level

  var mousePosition = Vector2D(0, 0)
  var mouseDown = false

  val mouseExit = (e : MouseEvent) => {
    mouseDown = false
  }

  canvas.onmouseleave = mouseExit
  canvas.onmouseup = mouseExit

  def zoom(delta : Double, e : MouseEvent) = {
    printer.zoom(delta, e.clientX, e.clientY)
    zoomLevel = zoomLevel + delta.toInt //update the zoom level
    render(editor.getAst)
  }

  canvas.onmousedown = (e : MouseEvent) => {
    mouseDown = true
    mousePosition = Vector2D(e.clientX, e.clientY)
  }

  canvas.onmousemove = (e : MouseEvent) => {
    if (mouseDown) {
      val zoomFactor = zoomLevel.toDouble.abs
      val newZ1 = math.pow(zoomFactor,1.1)
      val newZ2 = math.pow(zoomFactor,0.5)

      val newV = Vector2D(e.clientX, e.clientY)
      if(zoomLevel < 0) { //zooming out
        printer.translate((newV - mousePosition).x * newZ1, (newV - mousePosition).y * newZ1)
      } else if(zoomLevel > 0){//zooming in
        printer.translate((newV - mousePosition).x / newZ2, (newV - mousePosition).y / newZ2)
      } else printer.translate((newV - mousePosition).x, (newV - mousePosition).y)
      mousePosition = newV

      render(editor.getAst)
    }
  }

  def render(ast : Expr) {
    render(ast, printer)
  }

  def render(ast : Expr, printer : Printer[_]): Unit = {
    printer.prepare() //redraw the canvas
    Reposcript.evaluate(ast, printer)
    printer.execute()
  }

}
