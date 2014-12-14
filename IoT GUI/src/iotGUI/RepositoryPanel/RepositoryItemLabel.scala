/**
 * 
 */
package iotGUI.RepositoryPanel

import scala.swing._
import scala.swing.event._
import scala.collection.mutable.ListBuffer
import java.awt.datatransfer.{DataFlavor, Transferable}
//import iotGUI.StagePanel._
//import iotGUI.ExecutionEngine._

/**
 * @author Robert Abatecola
 *
 */

object eRLZoneType extends Enumeration
{
	type eRLZoneType = Value
	val	eRLZBody, eRLZInputHandle, eRLZOutputHandle, eRLZElseHandle, eRLZNone = Value
}

import eRLZoneType._

object RepositoryItemLabel
{
	var riDataFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=iotGUI.RepositoryPanel.RepositoryItemLabel", "Repository Item Flavor");
	var myDataFlavors: Array[DataFlavor] = Array(riDataFlavor)
}

class RILEvent(inEvent: InputEvent, inSource: RepositoryItemLabel) extends scala.swing.event.Event
{
	var	myEvent: InputEvent = inEvent
	var source: RepositoryItemLabel = inSource
}

class RepositoryItemLabel(inText: String, inBorderWeight: Int = 1) extends RepositoryLabel(inText, inBorderWeight) with Transferable
{
	protected var	zoneHandleWidth: Int = 10
	protected var	zoneHandleHeight: Int = 10

	var dragstart: Point = null
	var	mouseClickedZone: eRLZoneType = eRLZNone
	var	mouseReleasedZone: eRLZoneType = eRLZNone
	var outputChainLabel: RepositoryItemLabel = null
	var inputChainLabel: RepositoryItemLabel = null

	override def clone(): RepositoryItemLabel =
	{
		var newRIL = new RepositoryItemLabel(text, borderWeight)

		newRIL.setProcessNode(myProcessNode.clone())

		return newRIL
	}

	override def getTransferData(flavor: DataFlavor): Object =
	{
		var	newRILabel: RepositoryItemLabel = this.clone()

		return newRILabel
	}

	override def getTransferDataFlavors(): Array[java.awt.datatransfer.DataFlavor] =
	{
		return RepositoryItemLabel.myDataFlavors
	}

	override def isDataFlavorSupported(flavor: DataFlavor): Boolean =
	{
		return flavor.equals(RepositoryItemLabel.riDataFlavor)
	}

	def addInputConnection(inRIL: RepositoryItemLabel)
	{
		inputChainLabel = inRIL
		getProcessNode.addInputConnection(inRIL.getProcessNode)
	}

	def addOutputConnection(inRIL: RepositoryItemLabel, inZone: eRLZoneType = eRLZNone)
	{
		if (inZone == eRLZOutputHandle)
		{
			outputChainLabel = inRIL
			getProcessNode.addOutputConnection(inRIL.getProcessNode)
		}
	}

	def getZone(inPoint: Point): eRLZoneType =
	{
		var peerBounds: Rectangle = peer.bounds()
		var peerLoc: Point = peer.location()
//		println("point: " + inPoint)
		if (inPoint.getX() > peer.bounds.getWidth() - zoneHandleWidth)
		{
			return eRLZOutputHandle
		}

		if (inPoint.getX() < + zoneHandleWidth)
		{
			return eRLZInputHandle
		}

		return eRLZBody
	}

	def getZoneByConnectedItem(inRIL: RepositoryItemLabel): eRLZoneType =
	{
		if (inRIL == outputChainLabel)
			return eRLZOutputHandle

		if (inRIL == inputChainLabel)
			return eRLZInputHandle

		return eRLZNone
	}

	def getAllOutputs(): ListBuffer[RepositoryItemLabel] =
	{
		var	myOutputs: ListBuffer[RepositoryItemLabel] = new ListBuffer[RepositoryItemLabel]

		myOutputs += outputChainLabel
	}

	def getZoneLocation(inZone: eRLZoneType): Point =
	{
		var	zoneLoc: Point = new Point

		inZone match
		{
			case `eRLZInputHandle` =>
				zoneLoc.setLocation(1, getHeight() / 2)

			case `eRLZOutputHandle` =>
				zoneLoc.setLocation(getWidth(), getHeight() / 2)

			case _ =>
				zoneLoc.setLocation(this.location)
		}

		return zoneLoc
	}

	def getZoneLocationByConnectedItem(inRIL: RepositoryItemLabel): Point =
	{
		var	theZone: eRLZoneType = getZoneByConnectedItem(inRIL)

		return getZoneLocation(theZone)
	}

	import java.awt.Color
	import java.awt._
	import java.awt.event._
	import javax.swing.BoxLayout
	import javax.swing.GroupLayout
	import javax.swing.GroupLayout._
	import javax.swing.JOptionPane
	import javax.swing.JFrame
	import javax.swing.JPanel
	import javax.swing.JScrollPane
	import javax.swing.JButton
	import javax.swing.JTextArea
	import javax.swing.JTextField
	import javax.swing.border._
	import scala.collection.JavaConverters._
	import scala.swing.Dialog
	import scala.swing.Dialog.Message
	import iotGUI._
	def promptForParameters()
	{
//		var	tempSensorList = Array[Object]("Temp01", "Temp02", "Gas01", "Gas02")
		var	tempSensorList = Array[Object]("")
		// create a simple JPanel
//		var panel: BoxPanel = new BoxPanel(Orientation.Vertical)
		var panel: NULLPanel = new NULLPanel
		panel.background = java.awt.Color.BLUE;
//		panel.minimumSize = new Dimension(200,200);

		var jList: ListBuffer[String] = ListBuffer[String]("Testing...", "More stuff", "Even more stuff")
		var jListView: ListView[String] = new ListView(jList)
        var myScrollPane: ScrollPane = new ScrollPane()
		var myCheckBox: java.awt.Checkbox = new java.awt.Checkbox("Test")
	// display the jpanel in a joptionpane dialog, using showMessageDialog
//		var frame: JFrame = new JFrame("JOptionPane showMessageDialog component example");

/*
		myScrollPane.contents = jListView
//		panel.contents += myScrollPane
		var answer = Dialog.showInput(this,
		        panel,
		        "Select sensor",
		        Message.Question,
		        null,
		        tempSensorList,
		        "");
*/

    // create a JTextArea
		var jTextInput: JTextField = new JTextField
		var textInput: TextField = new TextField
		var myJPanel: JPanel = new JPanel
//		var textArea: JTextArea = new JTextArea(6, 25)
//		var longMessage = "This is a long text message that I would like to display inside a dialog.  Let's see if it works.  It seems to be OK as long as Java is used for the dialog and components; however, in Scala it does not work."

//        textArea.setText(longMessage)
//        textArea.setEditable(false)

        // wrap a scrollpane around it
//        var scrollPane: JScrollPane = new JScrollPane(textInput)

//		var myLayout: GroupLayout = new GroupLayout(myJPanel)
//		var myLayout: BoxLayout = new BoxLayout(myJPanel, BoxLayout.PAGE_AXIS)

//		myJPanel.setLayout(myLayout)
//		myJPanel.setLayout(null)


//		myJPanel.add(jTextInput)
//		myJPanel.add(myCheckBox)

//		jTextInput.setLocation(10, 10)
//		jTextInput.setSize(50, 10)


//		myScrollPane.add(jTextInput)
//		myScrollPane.add(myCheckBox)
//		panel.add(textInput, 200, 200)

	myJPanel = new JPanel()

    myJPanel.setLayout(new GridBagLayout());
    // creates a constraints object
    var c: GridBagConstraints = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2); // insets for all components
    c.gridx = 0; // column 0
    c.gridy = 0; // row 0
    c.ipadx = 20; // increases components width by 40 pixels
    c.ipady = 5; // increases components height by 10 pixels
    myJPanel.add(jTextInput, c); // constraints passed in
    c.gridx = 1; // column 1
    // c.gridy = 0; // comment out this for reusing the obj
    c.ipadx = 0; // resets the pad to 0
    c.ipady = 0;
    myJPanel.add(myCheckBox, c);
    c.gridx = 0; // column 0
    c.gridy = 1; // row 1
    myJPanel.add(new JButton("and"), c);
    c.gridx = 1; // column 1
    myJPanel.add(new JButton("Support."), c);


		// display them in a message dialog
//        var answer = JOptionPane.showMessageDialog(this.peer, scrollPane)

		myJPanel.setMinimumSize(new Dimension(200, 600))

		var myDialog: Dialog = new Dialog
    	myDialog.minimumSize = new Dimension(200, 600)

		var answer = Dialog.showInput(this,
			myJPanel,
	        "Select sensor",
	        Message.Plain,
	        null,
	        tempSensorList,
			tempSensorList(0));

//		JOptionPane.showMessageDialog(this.peer, panel);

		println("Answer is '" + answer + "'")
		println("textArea text is '" + jTextInput.getText + "'")
	}

	listenTo(mouse.clicks, mouse.moves)
	reactions +=
	{
		case e:MouseClicked =>
//			println("Got clicked!")
			if (e.clicks == 2)
			{
				promptForParameters()
				publish(new RILEvent(e, this))
			}

		case e:MouseDragged =>
			publish(new RILEvent(e, this))

		case e:MousePressed =>
			this.mouseClickedZone = getZone(e.point)
			publish(new RILEvent(e, this))

		case e:MouseReleased =>
			this.mouseReleasedZone = getZone(e.point)
			publish(new RILEvent(e, this))
	}
}
