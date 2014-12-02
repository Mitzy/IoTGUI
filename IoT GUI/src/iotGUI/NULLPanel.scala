/**
 *
 */
package iotGUI

import swing._
import iotGUI.RepositoryPanel.RepositoryLabel

/**
 * @author Robert Abatecola
 *
 */
class NULLPanel extends Panel
{
	peer.setLayout(null)

	def add(comp: Component, x: Int, y: Int, hPad: Int = 0, vPad: Int = 0): Unit =
	{
		val p = comp.peer

		if (hPad != 0 || vPad != 0)
		{
			val prefSize: Dimension = p.getPreferredSize()
			prefSize.setSize(prefSize.getWidth() + hPad * 2, prefSize.getHeight() + vPad * 2)
			p.setPreferredSize(prefSize)
		}

		p.setLocation(x, y)
		p.setSize(p.getPreferredSize)
		peer.add(p)
	}
}
