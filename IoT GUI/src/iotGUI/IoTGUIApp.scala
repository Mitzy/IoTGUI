package iotGUI

import scala.swing._
import scala.swing.Orientation._
import scala.collection.mutable.ListBuffer
import scala.reflect.runtime
import scala.tools.reflect.ToolBox
import iotGUI.RepositoryPanel._
import iotGUI.StagePanel._

object eRLType extends Enumeration
{
	type eRLType = Value
	val	eRLSection, eRLItem = Value
}

import eRLType._

class cRIObject
{
	var	UID: Long = 0
	// Repository list information
	var rlType: eRLType = eRLItem
	var	rlSectionID: String = ""
	var rlToolTip: String = ""
	// Repository item details
	var	riName: String = ""
	var riType: Int = 0

	var	riInputConn: ListBuffer[cRIObject] = new ListBuffer[cRIObject]
	var riOutputConn: cRIObject = null

	override def clone(): cRIObject =
	{
		var newRLO = new cRIObject

		newRLO.UID = UID
		newRLO.rlType = rlType
		newRLO.rlSectionID = rlSectionID
		newRLO.rlToolTip = rlToolTip
		newRLO.riName = riName
		newRLO.riType =riType

		return newRLO
	}

	def addInputConnection(inIC: cRIObject)
	{
		riInputConn += inIC
	}

	def addOutputConnection(inOC: cRIObject)
	{
		riOutputConn = inOC
	}
}

// Input module should return ListBuffer[cRLObject] that is created in the same way as in rlObjectListMaker
object rlObjectListMaker
{
	var	rlObjectList: ListBuffer[cRIObject] = ListBuffer[cRIObject]()
	var	rlList: ListBuffer[RepositoryLabel] = ListBuffer[RepositoryLabel]()

	addToList(1, eRLSection , "Repository")
	addToList(2, eRLItem, "Sensor Conn.", "Connect or disconnect a sensor", 1)
	addToList(3, eRLItem, "Sensor Read/Write", "Read from or write to a connected sensor", 2)

	addToList(4, eRLSection , "Flow Control")
	addToList(5, eRLItem, "If...then...else", "\"If...then...else\" flow control", 3)
	addToList(6, eRLItem, "For", "\"For\" flow control", 4)
	addToList(7, eRLItem, "While", "\"While\" flow control", 5)
	addToList(8, eRLItem, "End", "End block to mark termination of a loop or If statement", 6)
	addToList(9, eRLItem, "Wait", "Pause for a specified amount of time before continuing", 7)

	addToList(10, eRLSection , "Output")
	addToList(11, eRLItem, "Tweet", "Tweet a message via a Twitter account", 8)

	def addToList(inUID: Long, inRLType: eRLType, inRIName: String, inRLToolTip: String = "", inRIType: Int = 0)
	{
		var	rlItem: cRIObject = new cRIObject()
		{
			UID = inUID
			rlType = inRLType
			rlToolTip  = inRLToolTip
			riName = inRIName
			riType = inRIType
		}
		rlObjectList += rlItem
	}
}

object IoTGUIApp extends SimpleSwingApplication
{
	var	repositoryLabelList: ListBuffer[RepositoryLabel] = null
	var	rlObjectList: ListBuffer[cRIObject] = rlObjectListMaker.rlObjectList

	def makeRLList(inRLObjectList: ListBuffer[cRIObject]): ListBuffer[RepositoryLabel] =
	{
		var	repositoryLabelList: ListBuffer[RepositoryLabel] = ListBuffer()

		for (rlItem <- inRLObjectList)
		{
			var myRepoLabel = rlItem.rlType match
			{
				case `eRLSection` => new RepositorySectionLabel(rlItem.riName)
				case `eRLItem` => new RepositoryItemLabel(rlItem.riName)
			}
			myRepoLabel.setRLItem(rlItem)
			repositoryLabelList += myRepoLabel
		}

		return repositoryLabelList
	}

	val ui = new SplitPane(Vertical)
	{
        oneTouchExpandable = true
        continuousLayout = true
		repositoryLabelList = makeRLList(rlObjectList)

		val repositoryPanel = new RepositoryPanelUsingList(repositoryLabelList)
		{
			peer.setDragEnabled(true)
			visible = true
			peer.setSize(10, 100)
			renderer = ListView.Renderer(_.getRLItem().riName)
		}

		val	stagePanel = new StagePanel
		{
//			peer.setBounds(0, 0, 100, 100)
//			border = Swing.LineBorder(java.awt.Color.BLACK)
			object label extends Label
			{
				border = Swing.LineBorder(java.awt.Color.BLACK)
				text = "Stage"
			}
//			add(label, 10, 20)
			add(label, peer.getPreferredSize().getWidth().toInt / 2, (peer.getHeight() - label.peer.getHeight()) / 2 + peer.getLocation().y)
		}

		leftComponent = new ScrollPane(repositoryPanel)
//		leftComponent = repositoryPanel
//		rightComponent = stagePanel
		rightComponent = new ScrollPane(stagePanel)
	}

	def top = new MainFrame
	{
		title = "COEN 296 IoT GUI"
		contents = ui
		bounds = new Rectangle(0, 0, 900, 600)
		centerOnScreen()
	}
}