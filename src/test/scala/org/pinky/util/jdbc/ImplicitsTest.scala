package org.pinky.code.util.jdbc




import java.sql.DriverManager
import DriverManager.{getConnection => connect}


import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import org.pinky.util.ARM.using
import org.pinky.util.jdbc.Implicits._


/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Aug 4, 2009
 * Time: 1:02:42 PM
 * To change this template use File | Settings | File Templates.
 */

class ImplicitsTest extends Spec with ShouldMatchers {

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
           for (conn <- using (connect("jdbc:h2:mem:", "sa", ""))) {
              conn execute setup
              val insertPerson = conn prepareStatement "insert into person(tp, name) values(?, ?)"
              insertPerson  << 1 << "john" execute
              val ret = conn.query("SELECT * FROM PERSON WHERE ID=?",1)
              ret.foreach( row => {row("name") should equal("john") } )
              conn execute("insert into person(tp, name) values(?, ?)",2,"peter")
              val ret2 = conn.query("SELECT * FROM PERSON WHERE ID=?", 2)
              ret2.toList(0)("name") should equal("peter")
              val selectStatement = conn.prepareStatement("SELECT * FROM PERSON WHERE ID=?")
              selectStatement << 2
              val selectResult = selectStatement.query.toList(0)
              val autoCastID:Long = selectResult("id")
              selectResult("name") should equal("peter")
              autoCastID should equal(2)
              val people = conn.queryFor[Person]("SELECT * FROM PERSON")
              people.size should be (2)
              people(0).id should be (1)
             //there is a weird bug with scalatest when trying to use should with people(0).name
              assert(people(0).name=="john")
              people(1).id should be (2)

           }
       }
     }   
}
case class Person(id:Long,tp:Int,name:String)
