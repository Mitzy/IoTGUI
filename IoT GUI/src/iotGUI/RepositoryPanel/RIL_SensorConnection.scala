package iotGUI.RepositoryPanel

//remove if not needed
//import scala.collection.JavaConversions._

class RIL_SensorConnection(inText: String, inBorderWeight: Int = 1) extends RepositoryItemLabel(inText, inBorderWeight) {

	var selectedSensor: String = ""
  var _uID: Long = 0
  var _rLType: String = null
  var _rLName: String = null
  var _rLToolTip: String = null
  var _rIType: String = null
  
  def getuID() = _uID  
  def setuID(uid:Long) = this._uID = uid 
  
  def getrLtype() = _rLType  
  def setrLtype(rltype:String) = this._rLType = rltype 
  
  def getrLName() = _rLName  
  def setrLName(rlname:String) = this._rLName = rlname 
  
  def getrLToolTip() = _rLToolTip  
  def setrLToolTip(rltooltip:String) = this._rLToolTip = rltooltip 
  
  def getrIType() = _rIType  
  def setrIType(ritype:String) = this._rIType = ritype 

  def setAll(uid: Long, 
      rltype: String, 
      rlname: String, 
      rltooltip: String, 
      ritype: String) {
    this.setuID(uid)
    this.setrLtype(rltype)
    this.setrLName(rlname)
    this.setrLToolTip(rltooltip)
    this.setrIType(ritype)
  }

	override def clone(): RepositoryItemLabel =
	{
		var newRIL = new RIL_SensorConnection(text, borderWeight)

		newRIL.setProcessNode(myProcessNode.clone())

		return newRIL
	}


	import java.awt._
	import javax.swing.JPanel
	import javax.swing.JTextField
	import scala.swing.Dialog
	import scala.swing.Dialog.Message

	override def promptForParameters()
	{
		var	tempSensorList = Array[Object]("Temp - San Jose", "Temp - Santa Clara", "Gas01", "Gas02")
		var myCheckBox: java.awt.Checkbox = new java.awt.Checkbox("Test")
    // create a JTextArea
//		var jTextInput: JTextField = new JTextField
//		var textInput: TextField = new TextField
		var myJPanel: JPanel = new JPanel

	myJPanel = new JPanel()

    myJPanel.setLayout(new GridBagLayout());
    // creates a constraints object
//    var c: GridBagConstraints = new GridBagConstraints();
//    c.insets = new Insets(2, 2, 2, 2); // insets for all components


		// display them in a message dialog

		var answer = Dialog.showInput(this,
			myJPanel,
	        "Select sensor",
	        Message.Plain,
	        null,
	        tempSensorList,
			selectedSensor);

		println("Answer is '" + answer + "'")
		
		if (answer != None)
			selectedSensor = answer match { case Some(s) => s.toString(); case None => selectedSensor }
	}
}