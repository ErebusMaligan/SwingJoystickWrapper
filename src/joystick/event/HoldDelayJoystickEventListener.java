package joystick.event;


/**
 * @author Daniel J. Rivers
 *         2015
 *
 * Created: Aug 3, 2015, 12:54:25 AM 
 */
public class HoldDelayJoystickEventListener extends JoystickEventAdapter {

	private final int HOLD = 15;
	
	protected int holdCount = 0;
	
	private int heldButton = -1;
	
	private JoystickEventListener l;
	
	public HoldDelayJoystickEventListener( JoystickEventListener l ) {
		this.l = l;
	}
	
	@Override
	public void buttonHeld( JoystickEvent e ) {
		held( e );
	}
	
	public void held( JoystickEvent e ) {
		if ( heldButton == e.getButton() ) {
			holdCount++;
		} else {
			resetHoldCount();
		}
		heldButton = e.getButton();
		if ( holdCount == HOLD ) {
			l.buttonHeld( e );
			resetHoldCount();
		}
	}
	
	public void resetHoldCount() {
		holdCount = 0;
	}
	
}
