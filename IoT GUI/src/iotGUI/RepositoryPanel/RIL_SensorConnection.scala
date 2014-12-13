package iotGUI.RepositoryPanel

//remove if not needed
import scala.collection.JavaConversions._

class RIL_SensorConnection(inText: String, inBorderWeight: Int = 1) extends RepositoryItemLabel(inText, inBorderWeight) {

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
}