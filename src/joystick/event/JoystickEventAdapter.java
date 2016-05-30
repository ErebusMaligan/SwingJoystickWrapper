package joystick.event;

/**
 * @author Daniel J. Rivers
 *         2015
 *
 * Created: Jul 16, 2015, 7:16:42 AM 
 */
public class JoystickEventAdapter implements JoystickEventListener {

	@Override
	public void buttonPressed( JoystickEvent e ) {}

	@Override
	public void buttonReleased( JoystickEvent e ) {}

	@Override
	public void buttonHeld( JoystickEvent e ) {}

	@Override
	public void connected( JoystickEvent e ) {}
	
	@Override
	public void disconnected( JoystickEvent e ) {}
	@Override
	public void axisMoved( JoystickEvent e ) {}
}