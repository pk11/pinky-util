package org.pinky.util.jdbc


import java.sql._
import collection.mutable.{Map=>MMap, ListBuffer}

private[jdbc] trait PreparedStatementCollection {

    def collect(ps: PreparedStatement): List[Map[String, AnyRef]] = {
      val lb = new ListBuffer[Map[String, AnyRef]]
      val rs = ps.executeQuery
      while (rs.next()) {
        val map = MMap[String, AnyRef]()
        for (i <- 1 to rs.getMetaData.getColumnCount) map(rs.getMetaData.getColumnName(i).toLowerCase) = rs.getObject(i)
        lb += map.toMap
      }
      rs.close()
      lb.toList
    }

    //TODO: refactor this once default params in 2.8 get released
    def collectFor[T](manifest: scala.reflect.Manifest[T], ps: PreparedStatement): List[T] = {
      val lb = new ListBuffer[T]
      val rs = ps.executeQuery
      while (rs.next) {
        val args = new ListBuffer[AnyRef]()
        for (i <- 1 to rs.getMetaData.getColumnCount) args += rs.getObject(i)
        val constructor = manifest.erasure.getConstructors()(0)
        lb += constructor.newInstance(args.toArray: _*).asInstanceOf[T]
      }
      rs.close()
      lb.toList
    }
    def addStatementParams(ps: PreparedStatement, params: Any*) = {
      var count = 1
      for (param <- params) {
        ps.setObject(count, param)
        count += 1
      }
      ps
    }

  }

private[jdbc] object PreparedStatementCollection extends PreparedStatementCollection
