/**
 *
 */
package iotGUI.RepositoryPanel

//import javax.swing._
//import scala.swing.Alignment
//import scala.swing.Swing._

import scala.swing.Label
import scala.swing.Swing
import java.awt.Color
import iotGUI.ExecutionEngine._

/**
 * @author Robert Abatecola
 *
 */

class RepositoryLabel(inText: String, inBorderWeight: Int) extends Label
{
	var	borderWeight: Int = 1
	var selectedBorderWeight: Int = 3
	var unSelectedBorderWeight: Int = 1
	var nextOutputChainLabel: RepositoryLabel = null

	protected var myRLItem: ProcessNode = null

	setBorderWeight(inBorderWeight)

	def getHeight(): Int =
	{
		return peer.getPreferredSize().getHeight().toInt
	}

	def getWidth(): Int =
	{
		return peer.getPreferredSize().getWidth.toInt
	}

	def setBorderWeight(inBorderWeight: Int)
	{
		borderWeight = inBorderWeight
		border = Swing.LineBorder(java.awt.Color.BLACK, borderWeight)
	}

	def setRLItem(inRLItem: ProcessNode)
	{
		myRLItem  = inRLItem
		if (myRLItem != null)
			tooltip = myRLItem.rlToolTip
		else
			tooltip = ""
	}

	def getRLItem(): ProcessNode = return myRLItem

	text = inText
	setBorderWeight(inBorderWeight)
	background = Color.WHITE
	opaque = true

	override def clone(): RepositoryLabel =
	{
		var newRL = new RepositoryLabel(text, borderWeight)

		newRL.setRLItem(myRLItem.clone())

		return newRL
	}

	def selectMe(inSelected: Boolean = true)
	{
		if (inSelected)
		{
//			setBorderWeight(selectedBorderWeight)
			background = Color.CYAN
		}
		else
		{
//			setBorderWeight(unSelectedBorderWeight)
			background = Color.WHITE
		}
	}

	def addInputConnection(inIC: ProcessNode)
	{
		myRLItem.addInputConnection(inIC)
	}
}
