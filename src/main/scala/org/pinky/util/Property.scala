package org.pinky.util

import java.io.FileInputStream
import java.io.File
import java.io.InputStream
import java.util.Properties
import ARM._
trait Property {

    private val properties = new Properties

    protected lazy val discardFileRead = false
    protected lazy val sysVariable="pinky.util"
    protected lazy val propertyName=File.separator+"project.properties"
    
    protected def is: InputStream =
        if (System.getProperty(sysVariable) != null && System.getProperty(sysVariable) != "") 
          new FileInputStream(System.getProperty(sysVariable)+File.separator+propertyName)
        else
          this.getClass().getResourceAsStream(propertyName)
    
    if (discardFileRead == false) for (in <- using(is)) properties.load(in)


    def key(key: String) = {
       val property = properties.getProperty(key)
       if (property == null || property =="") None else Some(property)
    }
}


// vim: set ts=4 sw=4 et:
object Property extends Property
