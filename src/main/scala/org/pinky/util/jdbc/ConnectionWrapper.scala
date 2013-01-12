package org.pinky.util.jdbc

import java.sql._

trait Query {
  def execute(sql: String): Unit

  //execute statements
  def execute(sql: Seq[String]): Unit

  //prepared statements
  def execute(sql: String, params: Any*)

  //prepared query for a List of Map
  def query(sql: String, params: Any*): List[Map[String, AnyRef]]

  //prepared query for a List of Data classes
  def queryFor[T](sql: String, params: Any*)(implicit manifest: scala.reflect.Manifest[T]): List[T]

  def connection: Connection
}


class ConnectionWrapper(conn: Connection) extends Query {
  def execute(sql: String) {
    val s = conn.createStatement
    s.execute(sql)
    s.close()
  }

  //execute statements
  def execute(sql: Seq[String]) {
    val s = conn.createStatement
    for (x <- sql) s.execute(x)
    s.close()
  }

  //prepared statements
  def execute(sql: String, params: Any*) {
    val ps = PreparedStatementCollection.addStatementParams(conn.prepareStatement(sql), params: _*)
    ps.execute()
    ps.close()
  }

  //prepared query for a List of Map
  def query(sql: String, params: Any*): List[Map[String, AnyRef]] = {
    val ps = PreparedStatementCollection.addStatementParams(conn.prepareStatement(sql), params: _*)
    val result = PreparedStatementCollection.collect(ps)
    ps.close()
    result
  }

  //prepared query for a List of Data classes
  def queryFor[T](sql: String, params: Any*)(implicit manifest: scala.reflect.Manifest[T]): List[T] = {
    val ps = PreparedStatementCollection.addStatementParams(conn.prepareStatement(sql), params: _*)
    val result = PreparedStatementCollection.collectFor[T](manifest, ps)
    ps.close()
    result
  }

  def connection = conn
}
