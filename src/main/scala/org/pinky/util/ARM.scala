package org.pinky.util

import jdbc.Query

/**
 *  based on <a href="http://www.saager.org/2007/12/30/automatic-resource-management-blocks.html">this article</a>
 * <br><br>
 *  generic ARM block to support calls like
 * <pre>
 * for (conn &lt;- using (ds.getConnection)    { //do something with datasource }
 * </pre>
 * or a nested one
 * <pre>
 * for (outer <- using (new PipeStream())    {
 *  for (inner <- using (new PipeStream())    {
 * //do something with outer and inner
 * }
 * }
 * </pre>
 */
trait ARM {
    def managed[T](conn:  {def close()})(f:  {def close()} => T): Either[T, Exception] = {
    try {
      Left(f(conn))
    } catch {
      case (e: Exception) => Right(e)
    } finally {
      conn.close()
    }
  }

  def managed[T](query: Query)(f: Query => T): Either[T, Exception] = {
    try {
      Left(f(query))
    } catch {
      case (e: Exception) => Right(e)
    } finally {
      query.connection.close()
    }
  }
  private var ex: Exception = _
  /**
   * @param resource the reasource that needs to be wrapped around
   */
  case class using[T <: {def close()}](resource: T) {
    /**
     * execute block in the proper scope
     */
     def foreach(f: T => Unit): Unit =
      try {
        f(resource)
      } catch  {
        case (e:Exception) => ex = e
      } finally {
        resource.close()
        if (ex != null) {
          println("The following exception occured in an ARM block:\n-------------------")
          ex.printStackTrace
          println("-------------------\n")
          throw ex
        }
      } 
  }
}

object ARM extends ARM
