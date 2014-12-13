package iotGUI.Input

import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.util.ArrayList
import java.util.List
import java.util.Scanner
import java.util.StringTokenizer
import iotGUI.RepositoryPanel
import scala.reflect.runtime.{universe => ru}
//remove if not needed
import scala.collection.JavaConversions._

class RepositoryItemsInput {

 private def inputScanner(): List[String] = {
    var sc: Scanner = null
    val repositoryLabelList = new ArrayList[String]()
    val objectList = new ArrayList[Object]()
    try {
      sc = new Scanner(new BufferedReader(new FileReader("/Users/adithiathreya/Desktop/RepositoryItems.txt")).asInstanceOf[Readable])
      while (sc.hasNextLine()) {
        val scanText = new Scanner(sc.nextLine()).useDelimiter("\n")
        val currentLine = scanText.nextLine()
        if (currentLine.contains("\"Repository\"") || currentLine.contains("\"Flow Control\"") || 
          currentLine.contains("\"Output\"")) {
          //continue
        } else {
          val stk = new StringTokenizer(currentLine, ",")
          val uID = java.lang.Long.parseLong(stk.nextToken())
          val rLType = stk.nextToken()
          val rLName = stk.nextToken().replace("\"", "")
          val rLToolTip = stk.nextToken().replace("\"", "")
          val rIType = stk.nextToken().replace("\"", "")
          repositoryLabelList.add(rLName)
          try {
        	  val instObj = Class.forName(rIType).newInstance.asInstanceOf[{ def setAll(uid: Long, 
        			  		rltype: String, 
        			  		rlname: String, 
        			  		rltooltip: String, 
        			  		ritype: String) }]
        	   instObj.setAll(uID, rLType, rLName, rLToolTip, rIType)
        	   objectList.add(instObj)
        	 
          } catch {
            case e: ClassNotFoundException => 
          }
        }
      }
    } catch {
      case e: FileNotFoundException => e.printStackTrace()
    }
    def getRIObjects() = objectList
    repositoryLabelList
 }
}