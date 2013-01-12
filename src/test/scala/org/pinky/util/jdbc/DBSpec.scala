package org.pinky.code.util.jdbc




import java.sql.DriverManager
import DriverManager.{getConnection => connect}


import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import org.pinky.util.ARM.using
import org.pinky.util.jdbc.ConnectionWrapper


/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Aug 4, 2009
 * Time: 1:02:42 PM
 * To change this template use File | Settings | File Templates.
 */

class DBSpec extends Spec with ShouldMatchers {

     describe("a jdbc utility") {
       it ("should create connection a table and query from table using a prepared statement") {
          val setup = Array(
            """
                drop table if exists person
            ""","""
                create table person(
                    id identity,
                    tp int,
                    name varchar not null)
            """)
           val h2driver = Class.forName("org.h2.Driver")
           val db = new ConnectionWrapper(connect("jdbc:h2:mem:", "sa", ""))
           for (conn <- using (db.connection)) {
              db execute setup
              val insertPerson = db execute("insert into person(tp, name) values(?, ?)",1,"john")
              val ret = db.query("SELECT * FROM PERSON WHERE ID=?",1)
              ret.foreach( row => {row("name") should equal("john") } )
              db execute("insert into person(tp, name) values(?, ?)",2,"peter")
              val ret2 = db.query("SELECT * FROM PERSON WHERE ID=?", 2)
              ret2.toList(0)("name") should equal("peter")
              val people = db.queryFor[PersonDB]("SELECT * FROM PERSON")
              people.size should be (2)
              people(0).id should be (1)
              assert(people(0).name=="john")
              people(1).id should be (2)

           }
       }
     }
}
case class PersonDB(id:Long,tp:Int,name:String)
