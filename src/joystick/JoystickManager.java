package joystick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import joystick.event.JoystickEvent;
import joystick.event.JoystickEventListener;

import org.lwjgl.glfw.GLFW;

/**
 * @author Daniel J. Rivers
 *         2015
 *
 * Created: Jul 12, 2015, 5:22:43 PM 
 */
public class JoystickManager {
	
	private List<JoystickEventListener> listeners = new ArrayList<>();
	
	private Map<Integer, Joystick> joysticks = new HashMap<>();
	
	private List<JoystickEventListener> removeMe = new ArrayList<>();
	
	private List<JoystickEventListener> addMe = new ArrayList<>();
	
	private Integer[] ids = new Integer[] { GLFW.GLFW_JOYSTICK_1, GLFW.GLFW_JOYSTICK_2, GLFW.GLFW_JOYSTICK_3, GLFW.GLFW_JOYSTICK_4, GLFW.GLFW_JOYSTICK_5, GLFW.GLFW_JOYSTICK_6, GLFW.GLFW_JOYSTICK_7, GLFW.GLFW_JOYSTICK_8,
			GLFW.GLFW_JOYSTICK_9, GLFW.GLFW_JOYSTICK_10, GLFW.GLFW_JOYSTICK_11, GLFW.GLFW_JOYSTICK_12, GLFW.GLFW_JOYSTICK_13, GLFW.GLFW_JOYSTICK_14, GLFW.GLFW_JOYSTICK_14, GLFW.GLFW_JOYSTICK_15, GLFW.GLFW_JOYSTICK_16 };
	
	private long pollrate = 10;
	
	private boolean polling = false;
	
	public JoystickManager() {
		GLFW.glfwInit();
	}
	
	public void setPollRate( long pollrate ) {
		this.pollrate = pollrate;
	}
	
	public synchronized Map<Integer, Joystick> enumerateJoysticks() {
		for ( int i = 0; i < ids.length; i++ ) {
			int p = GLFW.glfwJoystickPresent( i );
			Joystick oj = joysticks.get( i );
			if ( p != 0 ) {
				Joystick nj = new Joystick( this, i, getSystemName( i ), getSystemButtonCount( i ) );
				if ( oj == null ) {
					//signal connected event here
//					System.out.println( p );
					joysticks.put( i, nj );
					notifyListeners( new JoystickEvent( nj, JoystickEvent.EventType.CONNECTED ) );
				} else if ( oj.equals( nj ) ) {
					//do nothing
				} else if ( !oj.equals( nj ) ) {
					//...not sure if this should actually happen
					joysticks.put( i, nj );
				}
			} else {
				if ( oj != null ) {
					//signal disconnected event here
					notifyListeners( new JoystickEvent( oj, JoystickEvent.EventType.DISCONNECTED ) );
					joysticks.put( i, null );
				}
			}
			if ( i == GLFW.GLFW_JOYSTICK_LAST ) {
				break;
			}
		}
		return joysticks;
	}
	
	private int getSystemButtonCount( int joy ) {
		return GLFW.glfwGetJoystickButtons( joy ).capacity();
	}
	
	private String getSystemName( int joy ) {
		return GLFW.glfwGetJoystickName( joy );
	}
	
	public void destroy() {
		GLFW.glfwTerminate();
	}
	
	private void updateButtonState( int joy ) {
		if ( joysticks.get( joy ) != null ) {
			joysticks.get( joy ).updateButtonState( GLFW.glfwGetJoystickButtons( joy ) );
		}
	}
	
	private void updateAxisState( int joy ) {
		if ( joysticks.get( joy ) != null ) {
			joysticks.get( joy ).updateAxisState( GLFW.glfwGetJoystickAxes( joy ) );
		}
	}
	
	public List<Joystick> getJoysticks() {
		return new ArrayList<>( joysticks.values() );
	}
	
	public void startPolling() {
		new Thread( () -> {
//			System.out.println( "POLLING STARTED" );
			polling = true;
			while ( polling ) {
				
				//this must be done before any notifications can happen
				removeMe.forEach( l -> reallyRemoveListener( l ) );
				removeMe.clear();
				addMe.forEach( l -> reallyAddListener( l ) );
				addMe.clear();
				//notifications can only be generated after this point
				
				Map<Integer, Joystick> m = enumerateJoysticks();
				m.forEach( ( i, j )  -> { 
					updateButtonState( i );
					updateAxisState( i );
				} );
				try {
					Thread.sleep( pollrate );
				} catch ( InterruptedException e ) {
					e.printStackTrace();
				}
			}
		} ).start();
	}
	
	public void stopPolling() {
//		System.out.println( "POLLING STOPPED" );
		polling = false;
	}
	
	
	public void addListener( JoystickEventListener l ) {
		addMe.add( l );
	}
	
	public void removeListener( JoystickEventListener l ) {
		removeMe.add( l );
	}
	
	protected void reallyAddListener( JoystickEventListener l ) {
//		System.out.println( "LISTENER ADDED: " + l );
		if ( !listeners.contains( l ) ) {
			listeners.add( l );
		}
	}
	
	protected void reallyRemoveListener( JoystickEventListener l ) {
//		System.out.println( "LISTENER REMOVED: " + l );
		listeners.remove( l );
	}
	
	public void notifyListeners( JoystickEvent e ) {
//		System.out.println( "NOTIFY CALLED" );
		listeners.forEach( l -> {
			switch ( e.getEventType() ) {
				case BUTTON_PRESSED: l.buttonPressed( (JoystickEvent)e ); break;
				case BUTTON_HELD: l.buttonHeld( (JoystickEvent)e ); break;
				case BUTTON_RELEASED: l.buttonReleased( (JoystickEvent)e ); break;
				case DISCONNECTED: l.disconnected( e ); break;
				case CONNECTED: l.connected( e ); break;
				case AXIS_MOVED: l.axisMoved( e ); break;
				default: break;
			}
		} );
	}
}