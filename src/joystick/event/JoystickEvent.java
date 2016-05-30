package joystick.event;

import joystick.Joystick;

/**
 * @author Daniel J. Rivers
 *         2015
 *
 * Created: Jul 12, 2015, 10:05:35 PM 
 */
public class JoystickEvent {
	
	public static enum EventType { CONNECTED, DISCONNECTED, BUTTON_PRESSED, BUTTON_RELEASED, BUTTON_HELD, AXIS_MOVED }

	protected EventType eventType;
	
	protected Joystick joystick;
	
	protected int button;
	
	protected float axisValue;
	
	protected float axisDelta;
	
	protected int axis;
	
	public JoystickEvent( Joystick joystick, EventType eventType ) {
		this.joystick = joystick;
		this.eventType = eventType;
	}
	
	public JoystickEvent( Joystick joystick, EventType et, int button ) {
		this( joystick, et );
		this.button = button;
	}
	
	public JoystickEvent( Joystick joystick, EventType et, int axis, float axisValue, float axisDelta ) {
		this( joystick, et );
		this.axisValue = axisValue;
		this.axisDelta = axisDelta;
		this.axis = axis;
	}
	
	public Joystick getJoystick() {
		return joystick;
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	public int getButton() {
		return button;
	}
	
	public float getAxisValue() {
		return axisValue;
	}
	
	public float getAxisDelta() {
		return axisDelta;
	}
	
	public int getAxis() {
		return axis;
	}
}