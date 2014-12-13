package iotGUI.RepositoryPanel

import scala.swing._
import scala.collection.mutable.ListBuffer

import eRLZoneType._

class RIL_IfThenElse(inText: String, inBorderWeight: Int = 1) extends RepositoryItemLabel(inText, inBorderWeight)
{
	var elseOutputChainLabel: RepositoryItemLabel = null

	override def clone(): RepositoryItemLabel =
	{
		var newRIL = new RIL_IfThenElse(text, borderWeight)

		newRIL.setProcessNode(myProcessNode.clone())

		return newRIL
	}

	override def getZone(inPoint: Point): eRLZoneType =
	{
		var	elseClickRect: Rectangle = new Rectangle()

		// Make it a 15x15 square, horizontally centered at the bottom of this item
		elseClickRect.setBounds((peer.bounds.getWidth() / 2 - 8).toInt, (peer.bounds.getHeight() - 15).toInt, 15, 15)
		if (elseClickRect.contains(inPoint))
			return eRLZElseHandle

		return super.getZone(inPoint)
	}

	override def addOutputConnection(inRIL: RepositoryItemLabel, inZone: eRLZoneType = eRLZNone)
	{
		if (inZone == eRLZElseHandle)
		{
			elseOutputChainLabel = inRIL
//			getProcessNode.addOutputConnection(inRIL.getProcessNode)
		}
		else
		{
			outputChainLabel = inRIL
			getProcessNode.addOutputConnection(inRIL.getProcessNode)
		}
	}

	override def getZoneByConnectedItem(inRIL: RepositoryItemLabel): eRLZoneType =
	{
		if (inRIL == elseOutputChainLabel)
			return eRLZElseHandle

		return super.getZoneByConnectedItem(inRIL)
	}

	override def getAllOutputs(): ListBuffer[RepositoryItemLabel] =
	{
		var	myOutputs: ListBuffer[RepositoryItemLabel] = super.getAllOutputs

		myOutputs += elseOutputChainLabel

		return myOutputs
	}

	override def getZoneLocation(inZone: eRLZoneType): Point =
	{
		var	zoneLoc: Point = new Point

		inZone match
		{
			case `eRLZElseHandle` =>
				zoneLoc.setLocation(getWidth() /2, getHeight() - 1)

			case _ =>
				return super.getZoneLocation(inZone)
		}

		return zoneLoc
	}
}
