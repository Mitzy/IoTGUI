package iotGUI.RepositoryPanel

import scala.swing._
import scala.collection.mutable.ListBuffer

import eRLZoneType._

class RIL_For(inText: String, inBorderWeight: Int = 1) extends RepositoryItemLabel(inText, inBorderWeight)
{
	private var forCount: Int = 0

	def setForCount(inForCount: Int) = { forCount = inForCount }
	def getForCount(): Int = forCount

	override def clone(): RepositoryItemLabel =
	{
		var newRIL = new RIL_For(text, borderWeight)

		newRIL.setProcessNode(myProcessNode.clone())
		newRIL.forCount = forCount

		return newRIL
	}
}
