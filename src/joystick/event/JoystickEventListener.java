package joystick.event;

/**
 * @author Daniel J. Rivers
 *         2015
 *
 * Created: Jul 12, 2015, 11:45:25 PM 
 */
public interface JoystickEventListener {

	public void buttonPressed( JoystickEvent e );
	
	public void buttonReleased( JoystickEvent e );
	
	public void buttonHeld( JoystickEvent e );
	
	public void connected( JoystickEvent e );
	
	public void disconnected( JoystickEvent e );
	
	public void axisMoved( JoystickEvent e );
}