package org.pinky.util

/**
 * taken from
 * http://cleverlytitled.blogspot.com/2009/03/disjoint-bounded-views-redux.html
 */

object DisjointBoundedView {

  type orType[A,B] = Either[A,B]

  // implicit defs
  implicit def l[T](t: T) = Left(t)
  implicit def r[T](t: T) = Right(t)
  implicit def ll[T](t: T) = Left(Left(t))
  implicit def lr[T](t: T) = Left(Right(t))
  implicit def lll[T](t: T) = Left(Left(Left(t)))
  implicit def llr[T](t: T) = Left(Left(Right(t)))
  implicit def llll[T](t: T) = Left(Left(Left(Left(t))))
  implicit def lllr[T](t: T) = Left(Left(Left(Right(t))))

}