package org.pinky.util

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import DisjointBoundedView._

/**
 * based on
 * http://cleverlytitled.blogspot.com/2009/03/disjoint-bounded-views-redux.html
 */

class Foo {

  def bar[T <% Int orType String orType Double](t: Option[T]): String = {
    t match {
      case Some(x: Int) => "Int"
      case Some(x: String) => "String"
      case Some(x: Double) => "Double"
      case None => "None"
    }
  }

  def baz[T <% String orType Int](t: List[T]):String = {
    t(0) match {
      case x: String => "String"
      case x: Int => "Int"
    }
  }
}

class DisjointBoundedViewSpec extends FlatSpec with ShouldMatchers {

   "disjoint bounded type" should "work" in {
        val f = new Foo()
        f.bar(None) should be ("None")
        f.bar(Some(1))  should be ("Int")
        f.bar(Some(1.45))  should be ("Double")
        f.bar(Some("String"))  should be ("String")
        f.baz(List(1,1,2,3,5,8,13)) should be ("Int")
        f.baz(List("boogie", "woogie"))  should be("String")
   }
}