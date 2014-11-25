/**
 *
 */
package iotGUI

import swing._

/**
 * @author Robert Abatecola
 *
 */
class NULLPanel extends Panel
{
	peer.setLayout(null)

	def add(comp: Component, x: Int, y: Int): Unit =
	{
		val p = comp.peer

		p.setLocation(x, y)
		p.setSize(p.getPreferredSize)
		peer.add(p)
	}
}
