object regex1 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  import scala.util.matching.Regex
  
  
  val pattern = "([A-Za-z]+) ([0-9]+)".r          //> pattern  : scala.util.matching.Regex = ([A-Za-z]+) ([0-9]+)
  val pattern(sensor, sensorThreshold) = "Temp 25"//> sensor  : String = Temp
                                                  //| sensorThreshold  : String = 25


   val sensornames = List("Temp", "Gas", "Moisture", "Vibration")
                                                  //> sensornames  : List[String] = List(Temp, Gas, Moisture, Vibration)
   
   val sensorthresholds = List(10, 30, 5, 10)     //> sensorthresholds  : List[Int] = List(10, 30, 5, 10)
   
   
   
   
   //we have to substitute values of the resp sensors and do the comparisons and return respective results.
   
   
val m = Map(("Temp",25),("Gas","33"), ("Moisture","99"),("Vibration","10"))
                                                  //> m  : scala.collection.immutable.Map[String,Any] = Map(Temp -> 25, Gas -> 33,
                                                  //|  Moisture -> 99, Vibration -> 10)

var i = 0;                                        //> i  : Int = 0
var j = 0;                                        //> j  : Int = 0
for(i <- 0 until sensorthresholds.length){
   for(j <- 0 until sensornames.length){
       if(sensorthresholds.indexOf(i) < sensornames.indexOf(j))
       			print(s"$sensornames.indexOf(j) alert!!")
      
           
      
 			
}
}
  
}