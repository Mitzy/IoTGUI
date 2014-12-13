package iotGUI

import scala.swing._

import scala.swing.Orientation._
import scala.collection.mutable.ListBuffer
import scala.reflect.runtime
import scala.tools.reflect.ToolBox
import scala.swing.event._
import iotGUI.RepositoryPanel._
import iotGUI.StagePanel._
import iotGUI.ExecutionEngine._

import iotGUI.ExecutionEngine.eRLType._

// Input module should return ListBuffer[cRLObject] that is created in the same way as in rlObjectListMaker
object rlObjectListMaker
{
	var	rlObjectList: ListBuffer[ProcessNode] = ListBuffer[ProcessNode]()
///	var	rlList: ListBuffer[RepositoryLabel] = ListBuffer[RepositoryLabel]()

	addToList(1, eRLSection , "---------------------")
	addToList(2, eRLSection , "Repository")
	addToList(3, eRLSection , "---------------------")
	addToList(4, eRLItem, "Start", "Start node", 1)
	addToList(5, eRLItem, "Sensor Conn.", "Connect or disconnect a sensor", 2)
	addToList(6, eRLItem, "Sensor Read/Write", "Read from or write to a connected sensor", 3)
	addToList(7, eRLItem, "Sensor Group Conn.", "Connect or disconnect a group of sensors", 4)
	addToList(8, eRLItem, "Sensor Group Read/Write", "Read from or write to a group of connected sensors", 5)
	addToList(9, eRLSection , "---------------------")
	addToList(10, eRLSection , "Flow Control")
	addToList(11, eRLSection , "---------------------")
	addToList(12, eRLIfThenElse , "If...then...else", "\"If...then...else\" flow control", 6)
	addToList(13, eRLItem, "For", "\"For\" flow control", 7)
	addToList(14, eRLItem, "While", "\"While\" flow control", 8)
	addToList(15, eRLItem, "End", "End block to mark termination of a loop or If statement", 9)
	addToList(16, eRLItem, "Wait", "Pause for a specified amount of time before continuing", 10)
	addToList(17, eRLSection , "---------------------")
	addToList(18, eRLSection , "Output")
	addToList(19, eRLSection , "---------------------")
	addToList(20, eRLItem, "Tweet", "Tweet a message via a Twitter account", 11)
	addToList(21, eRLItem, "Debug", "Debug node", 12)
	
	def addToList(inUID: Long, inRLType: eRLType, inRIName: String, inRLToolTip: String = "", inRIType: Int = 0)
	{
		var	rlItem: ProcessNode = new ProcessNode()
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
	var	rlObjectList: ListBuffer[ProcessNode] = rlObjectListMaker.rlObjectList

	def makeRLList(inRLObjectList: ListBuffer[ProcessNode]): ListBuffer[RepositoryLabel] =
	{
		var	repositoryLabelList: ListBuffer[RepositoryLabel] = ListBuffer()

		for (rlItem <- inRLObjectList)
		{
			var myRepoLabel = rlItem.rlType match
			{
				case `eRLSection` => new RepositorySectionLabel(rlItem.riName)
				case `eRLItem` => new RepositoryItemLabel(rlItem.riName)
				case `eRLIfThenElse` => new RIL_IfThenElse(rlItem.riName)
			}
			myRepoLabel.setProcessNode(rlItem)
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
			renderer = ListView.Renderer(_.getProcessNode().riName)
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
			
			val button = new Button
			{
				text = "RUN"
            }
            add(button,500,50)
            listenTo(button)

            reactions += {
            case ButtonClicked(button) =>
                 println("Event Trigerred:")
                 background=java.awt.Color.GREEN
                 var processnode1= new iotGUI.ExecutionEngine.ProcessNode_Sensor()
                 var connect1=processnode1.ConnectSensor
                 println(connect1)
                 if(connect1==0) {
                	 var step1=new iotGUI.ExecutionEngine.ProcessNode_IfElse()
                     var checkcondtion=step1.action
                 } else {
                	 printf("Error Occured while connecting to sensor")
                	 sys.exit
                 }
            }
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
