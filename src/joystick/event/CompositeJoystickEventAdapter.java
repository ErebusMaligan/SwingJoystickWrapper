package joystick.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Daniel J. Rivers
 *         2015
 *
 * Created: Aug 2, 2015, 10:20:40 PM 
 */
public class CompositeJoystickEventAdapter implements JoystickEventListener {

	private List<JoystickEventListener> listeners = new CopyOnWriteArrayList<>();
	
	public CompositeJoystickEventAdapter( List<JoystickEventListener> jel ) {
		listeners.addAll( jel );
	}
	
	@Override
	public void axisMoved( JoystickEvent e ) {
		listeners.forEach( l -> l.axisMoved( e ) );
	}

	@Override
	public void buttonHeld( JoystickEvent e ) {
		listeners.forEach( l -> l.buttonHeld( e ) );
	}

	@Override
	public void buttonPressed( JoystickEvent e ) {
		listeners.forEach( l -> l.buttonPressed( e ) );
	}

	@Override
	public void buttonReleased( JoystickEvent e ) {
		listeners.forEach( l -> l.buttonReleased( e ) );
	}

	@Override
	public void connected( JoystickEvent e ) {
		listeners.forEach( l -> l.connected( e ) );
	}

	@Override
	public void disconnected( JoystickEvent e ) {
		listeners.forEach( l -> l.disconnected( e ) );
	}
}